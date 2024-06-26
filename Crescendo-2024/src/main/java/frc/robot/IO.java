package frc.robot;

import frc.robot.lib.controls.ControlScheme;
import frc.robot.lib.controls.ControlSchemeCommand;
import frc.robot.lib.controls.ControlSchemeOnPressedCommand;
import frc.robot.lib.controls.ControlSchemeOnReleasedCommand;
import frc.robot.lib.controls.XBoxController;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Utils.Values;
import frc.robot.commands.*;

public class IO { 
    private static IO instance; 
    private XBoxController driverController;
    private XBoxController manipulatorController;
    private ControlScheme closedLoopControlScheme;
    private ControlScheme manualControlScheme;
    private double maxFeederSpeed;
    private double kScorerMaxPosition;
    private double ampPosition;
    private double shooterMaxSpeed;

    private IO() {
        driverController = new XBoxController(0);
        manipulatorController = new XBoxController(1);

        maxFeederSpeed = Values.getInstance().getDoubleValue("maxFeederSpeed");
        kScorerMaxPosition = Values.getInstance().getDoubleValue("kScorerMaxPosition");
        ampPosition = Values.getInstance().getDoubleValue("ampPosition");
        shooterMaxSpeed = Values.getInstance().getDoubleValue("shooterMaxSpeed");

        
    }
public static IO getInstance() {
    if(instance == null) instance = new IO();

    return instance;
}

public void switchControlScheme() {
    if (manipulatorController.getControlScheme() == closedLoopControlScheme) {
        manipulatorController.setControlScheme(manualControlScheme);
    } else{
        manipulatorController.setControlScheme(closedLoopControlScheme);
    }
}

public boolean isManualMode() {
    return manipulatorController.getControlScheme() == manualControlScheme;
}
public void teleopInit(){
//put commands here
        Command intakeNote = new SequentialCommandGroup(
            new ParallelCommandGroup(new RunAndExtendIntakeCommand(), new MoveToTransferPositionCommand()),
            new RetractIntakeCommand(),
            new TransferNoteCommand(),
            new MoveToTransferPositionCommand()
        );

        Command retractNote = new SequentialCommandGroup(
            new ParallelCommandGroup(new RetractIntakeCommand(), new MoveToTransferPositionCommand()),
            new TransferNoteCommand()
        );

        Command ampIntake = new SequentialCommandGroup(
            new MoveIntakeToUnJamPositionCommand(),
            new RunIntakeOutCommand()
        );

        Command flail = new ParallelCommandGroup(
            // new ExtendIntakeCommand(),
            new MoveIntakeToUnJamPositionCommand(),
            new MoveToAmpPositionCommand(),
            new TestRunScorer(1000),
            new TestRunIntake(Values.getInstance().getDoubleValue("intakeMaxSpeed")),
            new TestRunFeeder(maxFeederSpeed)
        );

        Command unFlail = new ParallelCommandGroup(
            new RetractIntakeCommand(),
            new MoveToTransferPositionCommand(),
            new TestRunScorer(0),
            new TestRunIntake(0),
            new TestRunFeeder(0)
        );

        Command sourceLoad = new SequentialCommandGroup(new MoveToSourcePositionCommand(), new MoveToTransferPositionCommand());

        Command handOff = new ParallelCommandGroup(new TestRunFeeder(maxFeederSpeed), new TestRunIntake(-maxFeederSpeed));
        Command handOver = new ParallelCommandGroup(new TestRunFeeder(0), new TestRunIntake(0));

        List <ControlSchemeCommand> closedLoopCommands = new ArrayList<>();
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("B", new RunScorerCommand()));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(kScorerMaxPosition)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(ampPosition)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("A", intakeNote));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("A", retractNote));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("SELECT", new ToggleControlSchemeCommand()));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("X", sourceLoad));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("X", new MoveToTransferPositionCommand()));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("E", new AdjustShooterPositionCommand(0.001)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("W", new MoveShooterToPositionCommand(0.700)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("Y", flail));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("Y", unFlail));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("S", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("speakerPosition"))));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("N", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("shooterLongShot"))));
        // closedLoopCommands.add(new ControlSchemeOnPressedCommand("S", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("shooterWingShot"))));
        closedLoopControlScheme = new ControlScheme(closedLoopCommands);

        List <ControlSchemeCommand> manualCommands = new ArrayList<>();
        manualCommands.add(new ControlSchemeOnPressedCommand("B", new TestRunScorer(shooterMaxSpeed)));
        manualCommands.add(new ControlSchemeOnReleasedCommand("B", new TestRunScorer(0)));
        manualCommands.add(new ControlSchemeOnPressedCommand("X", new ExtendIntakeCommand()));
        manualCommands.add(new ControlSchemeOnReleasedCommand("X", new RetractIntakeCommand()));
        manualCommands.add(new ControlSchemeOnPressedCommand("A", new TestRunIntake(maxFeederSpeed)));
        manualCommands.add(new ControlSchemeOnReleasedCommand("A", new TestRunIntake(0)));
        manualCommands.add(new ControlSchemeOnPressedCommand("Y", handOff));
        manualCommands.add(new ControlSchemeOnReleasedCommand("Y", handOver));
        manualCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(kScorerMaxPosition)));
        manualCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(ampPosition)));
        manualCommands.add(new ControlSchemeOnReleasedCommand("SELECT", new ToggleControlSchemeCommand()));
        manualCommands.add(new ControlSchemeOnPressedCommand("E", new AdjustShooterPositionCommand(0.01)));
        manualCommands.add(new ControlSchemeOnPressedCommand("W", new AdjustShooterPositionCommand(-0.01)));
        manualCommands.add(new ControlSchemeOnPressedCommand("N", new MoveIntakeToUnJamPositionCommand()));
        manualCommands.add(new ControlSchemeOnReleasedCommand("N", new RetractIntakeCommand()));
        manualCommands.add(new ControlSchemeOnPressedCommand("S", new RunIntakeOutCommand()));
        manualCommands.add(new ControlSchemeOnReleasedCommand("S", new TestRunIntake(0)));
        manualControlScheme = new ControlScheme(manualCommands);

driverController.whenButtonPressed("SELECT", new ChangeFieldRelativeCommand());
driverController.whenButtonPressed("Y", new ResetGyro());


manipulatorController.setControlScheme(closedLoopControlScheme);
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}