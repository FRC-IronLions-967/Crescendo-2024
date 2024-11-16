// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ScorerSubsystem;
import frc.robot.subsystems.SubsystemsInstance;
import frc.robot.subsystems.VisionSubsystem;

public class ObjectVisionCommand extends Command {

  private VisionSubsystem visionSubsystem;
  private Drivetrain drivetrain;
  private IntakeSubsystem intakeSubsystem;
  private ScorerSubsystem scorerSubsystem;
  private double kIntakeMinPosition;
  private double intakeMaxSpeed;

  /** Creates a new ObjectVisionCommand. */
  public ObjectVisionCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    visionSubsystem = SubsystemsInstance.getInstance().visionSubsystem;
    drivetrain = SubsystemsInstance.getInstance().drivetrain;
    intakeSubsystem = SubsystemsInstance.getInstance().intakesubsystem;
    scorerSubsystem = SubsystemsInstance.getInstance().scorersubsystem;

    kIntakeMinPosition = Values.getInstance().getDoubleValue("kIntakeMinPosition");
    intakeMaxSpeed = Values.getInstance().getDoubleValue("intakeMaxSpeed");

    addRequirements(drivetrain, visionSubsystem, intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {} 

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.lockonMoveTowardsObject(visionSubsystem.getObjectYaw());
    intakeSubsystem.moveIntake(kIntakeMinPosition);
    intakeSubsystem.runIntake(intakeMaxSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.runIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intakeSubsystem.isNoteIn() || scorerSubsystem.isNoteIn();
  }
}

