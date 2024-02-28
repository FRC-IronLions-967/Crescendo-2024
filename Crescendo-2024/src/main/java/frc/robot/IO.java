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
import frc.robot.commands.*;

public class IO { 
    private static IO instance; 
    private XBoxController driverController;
    private XBoxController manipulatorController;
    private ControlScheme closedLoopControlScheme;
    private ControlScheme manualControlScheme;

    private IO() {
        driverController = new XBoxController(0);
        manipulatorController = new XBoxController(1);

        Command intakeNote = new SequentialCommandGroup(
            new ParallelCommandGroup(new ExtendIntakeCommand(), new MoveToSpeakerPositionCommand()),
            new RunIntakeInCommand(),
            new RetractIntakeCommand(),
            new TransferNoteCommand(),
            new MoveToSpeakerPositionCommand()
        );

        Command handOff = new ParallelCommandGroup(new TestRunFeeder(2000), new TestRunIntake(-3000));
        Command handOver = new ParallelCommandGroup(new TestRunFeeder(0), new TestRunIntake(0));

        List <ControlSchemeCommand> closedLoopCommands = new ArrayList<>();
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("B", new RunScorerCommand()));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(0.56)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(0.82)));
        closedLoopCommands.add(new ControlSchemeOnPressedCommand("A", intakeNote));
        closedLoopCommands.add(new ControlSchemeOnReleasedCommand("A", new RetractIntakeCommand()));

        closedLoopControlScheme = new ControlScheme(closedLoopCommands);

        List <ControlSchemeCommand> manualCommands = new ArrayList<>();
        manualCommands.add(new ControlSchemeOnPressedCommand("B", new TestRunScorer(5000)));
        manualCommands.add(new ControlSchemeOnReleasedCommand("B", new TestRunScorer(0)));
        manualCommands.add(new ControlSchemeOnPressedCommand("X", new ExtendIntakeCommand()));
        manualCommands.add(new ControlSchemeOnReleasedCommand("X", new RetractIntakeCommand()));
        manualCommands.add(new ControlSchemeOnPressedCommand("A", new RunIntakeInCommand()));
        manualCommands.add(new ControlSchemeOnPressedCommand("Y", handOff));
        manualCommands.add(new ControlSchemeOnReleasedCommand("Y", handOver));
        manualCommands.add(new ControlSchemeOnPressedCommand("LBUMP", new TestMoveScorer(0.56)));
        manualCommands.add(new ControlSchemeOnPressedCommand("RBUMP", new TestMoveScorer(0.82)));

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
// Command intakeNote = new SequentialCommandGroup(
//     new ExtendIntakeCommand(),
//     new RunIntakeInCommand(),
//     new RetractIntakeCommand()
// );
// Command scoreIntoAmp = new SequentialCommandGroup(new MoveToAmpPositionCommand(), new RunScorerCommand());
// manipulatorController.whenButtonPressed("B", intakeNote);
// manipulatorController.whenButtonReleased("B", new RetractIntakeCommand());
// manipulatorController.whenButtonPressed("B", new RunScorerCommand());
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

// manipulatorController.whenButtonPressed("LBUMP", new TestMoveScorer(0.56));//0.56
// manipulatorController.whenButtonPressed("RBUMP", new TestMoveScorer(0.82));
// manipulatorController.whenButtonPressed("Y", new RunIntakeInCommand());
// manipulatorController.whenButtonPressed("A", new TestRunIntake(0));
// manipulatorController.whenButtonPressed("B", new TestMoveIntake(0.66));
// manipulatorController.whenButtonReleased("B", new TestMoveIntake(0.16));
}
public XBoxController getDriverController(){
    return driverController;
}
public XBoxController getManipulatorController(){
    return manipulatorController;

}
}