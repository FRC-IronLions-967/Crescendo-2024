package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SubsystemsInstance;

public class ChangeFieldRelativeCommand extends CommandBase {
    
public ChangeFieldRelativeCommand() {

}

@Override
public void execute() {
    SubsystemsInstance.getInstance().drivetrain.fieldRelative = !SubsystemsInstance.getInstance().drivetrain.fieldRelative;
}

@Override
  public boolean isFinished() {
    return true;
  }

}
