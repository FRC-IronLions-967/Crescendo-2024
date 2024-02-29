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
    private double speakerPostion;
    private double ampPosition;
    private double shooterMaxSpeed;

    private IO() {
        driverController = new XBoxController(0);
        manipulatorController = new XBoxController(1);

        maxFeederSpeed = Values.getInstance().getDoubleValue("maxFeederSpeed");
        speakerPostion = Values.getInstance().getDoubleValue("speakerPostion");
        ampPosition = Values.getInstance().getDoubleValue("ampPosition");
        shooterMaxSpeed = Values.getInstance().getDoubleValue("shooterMaxSpeed");

        Command intakeNote = new SequentialCommandGroup(
            new ParallelCommandGroup(new ExtendIntakeCommand(), new MoveToSpeakerPositionCommand()),
            new RunIntakeInCommand(),
            new RetractIntakeCommand(),
            new TransferNoteCommand(),
            new MoveToSpeakerPositionCommand()
        );

        Command handOff = new ParallelCommandGroup(new TestRunFeeder(maxFeederSpeed), new TestRunIntake(-maxFeederSpeed));
        Command handOver = new ParallelCommandGroup(new TestRunFeeder(0), new TestRunIntake(0));

        List <ControlSchemeCommand> closedLoopCommands = new ArrayList<>();
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("B", new RunScorerCommand()));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(speakerPostion)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(ampPosition)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("A", intakeNote));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("A", new RetractIntakeCommand()));

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
        manualCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(speakerPostion)));
        manualCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(ampPosition)));

        manualControlScheme = new ControlScheme(manualCommands);
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
public void teleopInit(){
//put commands here
driverController.whenButtonPressed("SELECT", new ChangeFieldRelativeCommand());

manipulatorController.setControlScheme(closedLoopControlScheme);
manipulatorController.whenButtonPressed("SELECT", new ToggleControlSchemeCommand());

Command handOff = new ParallelCommandGroup(new TestRunFeeder(maxFeederSpeed), new TestRunIntake(-maxFeederSpeed));
Command handOver = new ParallelCommandGroup(new TestRunFeeder(0), new TestRunIntake(0));
Command scoreIntoAmp = new SequentialCommandGroup(new MoveToAmpPositionCommand(), new RunScorerCommand());
manipulatorController.whenButtonPressed("B", new RunScorerCommand());
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}