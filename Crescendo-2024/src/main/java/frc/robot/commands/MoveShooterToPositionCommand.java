// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class MoveShooterToPositionCommand extends Command {
  /** Creates a new MoveShooterToPositionCommand. */
  private double angle;
  private double tolerance;
  public MoveShooterToPositionCommand(double angle) {
    this.angle = angle;
    tolerance = Values.getInstance().getDoubleValue("intakePositionTolerance");
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(SubsystemsInstance.getInstance().scorersubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SubsystemsInstance.getInstance().scorersubsystem.adjustShooter(angle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("At position");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return angle - tolerance <= SubsystemsInstance.getInstance().scorersubsystem.getScorerPosition() && angle + tolerance >= SubsystemsInstance.getInstance().scorersubsystem.getScorerPosition();
  }
}
