package frc.robot;

import frc.robot.lib.controls.XBoxController;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Utils.Values;
import frc.robot.commands.*;

public class IO { 
    private static IO instance; 
    private XBoxController driverController;
    private XBoxController manipulatorController;
    private double maxFeederSpeed;
    private double kScorerMaxPosition;
    private double ampPosition;

    private IO() {
        driverController = new XBoxController(0);
        manipulatorController = new XBoxController(1);

        maxFeederSpeed = Values.getInstance().getDoubleValue("maxFeederSpeed");
        kScorerMaxPosition = Values.getInstance().getDoubleValue("kScorerMaxPosition");
        ampPosition = Values.getInstance().getDoubleValue("ampPosition");
        
    }
public static IO getInstance() {
    if(instance == null) instance = new IO();

    return instance;
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
        new TestRunIntake(-4000),
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

    manipulatorController.whenButtonPressed("B", new RunScorerCommand());
    manipulatorController.whenButtonPressed("LBUMP", new TestMoveScorer(kScorerMaxPosition));
    manipulatorController.whenButtonPressed("RBUMP", new TestMoveScorer(ampPosition));
    manipulatorController.whenButtonPressed("A", intakeNote);
    manipulatorController.whenButtonReleased("A", retractNote);
    manipulatorController.whenButtonPressed("X", sourceLoad);
    manipulatorController.whenButtonReleased("X", new MoveToTransferPositionCommand());
    // manipulatorController.whenPOVButtonPressed("E", new VisualAimCommand()));
    // manipulatorController.whenPOVButtonPressed("E", new AdjustShooterPositionCommand(0.01)));
    // manipulatorController.whenPOVButtonPressed("W", new AdjustShooterPositionCommand(-0.01)));
    manipulatorController.whenButtonPressed("Y", flail);
    manipulatorController.whenButtonReleased("Y", unFlail);
    manipulatorController.whenPOVButtonPressed("S", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("speakerPosition")));
    manipulatorController.whenPOVButtonPressed("N", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("shooterLongShot")));
    // manipulatorController.whenButtonPressed("S", new MoveShooterToPositionCommand(Values.getInstance().getDoubleValue("shooterWingShot"))));

    driverController.whenButtonPressed("SELECT", new ChangeFieldRelativeCommand());
    driverController.whenButtonPressed("Y", new ResetGyro());
    driverController.whenButtonPressed("RBUMP", new VisualAimCommand());
    driverController.whenButtonReleased("RBUMP", new DefaultMoveCommand());
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}