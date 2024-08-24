// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class VisualAimCommand extends Command {
  double pitch;
  private double tolerance;
  /** Creates a new VisualAimCommand. */
  public VisualAimCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(SubsystemsInstance.getInstance().scorersubsystem);
    tolerance = Values.getInstance().getDoubleValue("intakePositionTolerance");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pitch = SubsystemsInstance.getInstance().scorersubsystem.getPitch();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SubsystemsInstance.getInstance().scorersubsystem.moveShooter(SubsystemsInstance.getInstance().scorersubsystem.getScorerPositionBasedOnPitch(pitch));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return SubsystemsInstance.getInstance().scorersubsystem.getScorerPositionBasedOnPitch(pitch) - tolerance <= SubsystemsInstance.getInstance().scorersubsystem.getScorerPosition() && SubsystemsInstance.getInstance().scorersubsystem.getScorerPositionBasedOnPitch(pitch) + tolerance >= SubsystemsInstance.getInstance().scorersubsystem.getScorerPosition();
  }
}
