package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SubsystemsInstance;

public class RunIntakeCommand extends Command {
    
public RunIntakeCommand() {

}

@Override
public void execute() {
    SubsystemsInstance.getInstance().intakesubsystem.runIntake(1);
}

@Override
  public boolean isFinished() {
    return true;
  }

}
