package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SubsystemsInstance;

public class AutoDriveCommand extends CommandBase{

    private SubsystemsInstance inst;
    private double desiredPose;

    public AutoDriveCommand (Float Amount) {
        inst = SubsystemsInstance.getInstance();
    }
    
    
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    inst.drivetrain.drive(0.0, 0.0, 0.0, true);
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}