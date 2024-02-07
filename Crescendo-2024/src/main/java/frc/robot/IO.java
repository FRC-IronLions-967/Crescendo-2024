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
// Command intakeNote = new SequentialCommandGroup(
//     new ParallelCommandGroup(new ExtendIntakeCommand(), new MoveToSpeakerPositionCommand()),
//     new RunIntakeInCommand(),
//     new RetractIntakeCommand(),
//     new TransferNoteCommand()
// );
// Command scoreIntoAmp = new SequentialCommandGroup(new MoveToAmpPositionCommand(), new RunScorerCommand());
// manipulatorController.whenButtonPressed("B", intakeNote);
// manipulatorController.whenButtonReleased("B", new RetractIntakeCommand());
// manipulatorController.whenButtonPressed("Y", new RunScorerCommand());
// manipulatorController.whenButtonPressed("X", scoreIntoAmp);
// manipulatorController.whenButtonPressed("A", new TestRunScorer(5600));
// manipulatorController.whenButtonReleased("A", new TestRunScorer(0));
// manipulatorController.whenButtonPressed("B", new TestRunFeeder(2000));
// manipulatorController.whenButtonReleased("B", new TestRunFeeder(0));
// manipulatorController.whenButtonPressed("X", new TestMoveScorer(75));
// manipulatorController.whenButtonReleased("X", new TestMoveScorer(52));
manipulatorController.whenButtonPressed("Y", new RunIntakeInCommand());
manipulatorController.whenButtonPressed("A", new TestRunIntake(0));
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}