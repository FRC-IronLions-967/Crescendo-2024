// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class RunIntakeInCommand extends Command {
  private double kIntakeMaxPosition;
  private double tolerance;
  private double kMaxNEOSpeed;
  /** Creates a new RunIntakeInCommand. */
  public RunIntakeInCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(SubsystemsInstance.getInstance().intakesubsystem);
    kIntakeMaxPosition = Values.getInstance().getDoubleValue("kIntakeMaxPosition");
    tolerance = Values.getInstance().getDoubleValue("intakePositionTolerance");
    kMaxNEOSpeed = Values.getInstance().getDoubleValue("kMaxNEOSpeed");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (SubsystemsInstance.getInstance().intakesubsystem.getIntakePosition() <= kIntakeMaxPosition + tolerance && SubsystemsInstance.getInstance().intakesubsystem.getIntakePosition() >= kIntakeMaxPosition - tolerance) {
      SubsystemsInstance.getInstance().intakesubsystem.runIntake(kMaxNEOSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SubsystemsInstance.getInstance().intakesubsystem.runIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return SubsystemsInstance.getInstance().intakesubsystem.isNoteIn();
  }
}
