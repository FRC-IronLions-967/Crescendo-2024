// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ScorerSubsystem;
import frc.robot.subsystems.SubsystemsInstance;
import frc.robot.subsystems.VisionSubsystem;

public class VisualAimCommand extends Command {
  private double pitch;
  private double yaw;
  private double tolerance;

  private VisionSubsystem visionSubsystem;
  private ScorerSubsystem scorerSubsystem;
  private Drivetrain drivetrain;
  /** Creates a new VisualAimCommand. */
  public VisualAimCommand() {
    // Use addRequirements() here to declare subsystem dependencies.

    visionSubsystem = SubsystemsInstance.getInstance().visionSubsystem;
    scorerSubsystem = SubsystemsInstance.getInstance().scorersubsystem;
    drivetrain = SubsystemsInstance.getInstance().drivetrain;

    addRequirements(scorerSubsystem);
    addRequirements(drivetrain);
    addRequirements(visionSubsystem);
    tolerance = Values.getInstance().getDoubleValue("intakePositionTolerance");

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    scorerSubsystem.automaticShooting = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    scorerSubsystem.moveShooter(visionSubsystem.getScorerPositionBasedOnPitch());
    drivetrain.drive(0, 0, -0.1 * visionSubsystem.getYaw(), false);
    if(visionSubsystem.isAimed() && scorerSubsystem.isInPosition()) {
      scorerSubsystem.runScorer(Values.getInstance().getDoubleValue("shooterMaxSpeed"));
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    scorerSubsystem.automaticShooting = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !scorerSubsystem.isNoteIn() || !visionSubsystem.lookForTargets();
  }
}
