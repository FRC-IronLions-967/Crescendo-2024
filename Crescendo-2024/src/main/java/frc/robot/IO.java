package frc.robot;

import frc.robot.lib.controls.XBoxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;

public class IO { 
    private static IO instance; 
    private XBoxController driverController;
    private XBoxController manipulatorController;

    private IO() {
        driverController = new XBoxController(0);
        manipulatorController = new XBoxController(1);
    }
public static IO getInstance() {
    if(instance == null) instance = new IO();

    return instance;
}
public void teleopInit(){
//put commands here
driverController.whenButtonPressed("SELECT", new ChangeFieldRelativeCommand());
Command intakeNote = new SequentialCommandGroup(
    new ParallelCommandGroup(new RunAndExtendIntakeCommand(), new MoveToSpeakerPositionCommand()),
    new RetractIntakeCommand(),
    new TransferNoteCommand(),
    new MoveToSpeakerPositionCommand()
);
// Command intakeNote = new SequentialCommandGroup(
//     new ExtendIntakeCommand(),
//     new RunIntakeInCommand(),
//     new RetractIntakeCommand()
// );
Command handOff = new ParallelCommandGroup(new TestRunFeeder(2000), new TestRunIntake(-3000));
Command handOver = new ParallelCommandGroup(new TestRunFeeder(0), new TestRunIntake(0));
// Command scoreIntoAmp = new SequentialCommandGroup(new MoveToAmpPositionCommand(), new RunScorerCommand());
// manipulatorController.whenButtonPressed("B", intakeNote);
// manipulatorController.whenButtonReleased("B", new RetractIntakeCommand());
manipulatorController.whenButtonPressed("B", new RunScorerCommand());
// manipulatorController.whenButtonPressed("X", scoreIntoAmp);
// manipulatorController.whenButtonPressed("X", new TestRunScorer(5600));
// manipulatorController.whenButtonReleased("X", new TestRunScorer(0));
// manipulatorController.whenButtonReleased("A", new TestRunScorer(0));
// manipulatorController.whenButtonPressed("B", handOff);
// manipulatorController.whenButtonReleased("B", handOver);
// manipulatorController.whenButtonPressed("Y", new TestRunFeeder(3000));
// manipulatorController.whenButtonReleased("Y", new TestRunFeeder(0));
// manipulatorController.whenButtonPressed("LBUMP", new TestRunScorer(1100));//1100
// manipulatorController.whenButtonReleased("LBUMP", new TestRunScorer(0));

manipulatorController.whenButtonPressed("RBUMP", new AdjustShooterPositionCommand(0.01));//0.56
manipulatorController.whenButtonPressed("LBUMP", new AdjustShooterPositionCommand(-0.01));
// manipulatorController.whenButtonPressed("Y", new RunIntakeInCommand());
// manipulatorController.whenButtonPressed("A", new TestRunIntake(0));
// manipulatorController.whenButtonPressed("B", new TestMoveIntake(0.66));
// manipulatorController.whenButtonReleased("B", new TestMoveIntake(0.16));
manipulatorController.whenButtonPressed("A", intakeNote);
manipulatorController.whenButtonReleased("A", new RetractIntakeCommand());
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}