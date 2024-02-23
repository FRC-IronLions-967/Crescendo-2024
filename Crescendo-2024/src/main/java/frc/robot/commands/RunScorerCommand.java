// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class RunScorerCommand extends Command {
  /** Creates a new RunScorerCommand. */
  private double kMaxNEOSpeed;
  private double tolerance;
  private double kScorerMaxPosition;
  public RunScorerCommand() {
    kMaxNEOSpeed = Values.getInstance().getDoubleValue("kMaxNEOSpeed");
    tolerance = Values.getInstance().getDoubleValue("intakePositionTolerance");
    kScorerMaxPosition = Values.getInstance().getDoubleValue("kScorerMaxPosition");

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(0.75 <= SubsystemsInstance.getInstance().scorersubsystem.getScorerPosition()) {
      SubsystemsInstance.getInstance().scorersubsystem.runScorer(5000);
    }else {
      SubsystemsInstance.getInstance().scorersubsystem.runScorer(5000);   
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !SubsystemsInstance.getInstance().scorersubsystem.isNoteIn();
  }
}
