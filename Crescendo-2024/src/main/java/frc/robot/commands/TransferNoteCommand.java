// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class TransferNoteCommand extends Command {
  /** Creates a new TransferNote. */
  private boolean isNote;
  private double maxFeederSpeed;
  private double intakeTransferSpeed;
  public TransferNoteCommand() {
    maxFeederSpeed = Values.getInstance().getDoubleValue("maxFeederSpeed");
    intakeTransferSpeed = Values.getInstance().getDoubleValue("intakeTransferSpeed");
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(SubsystemsInstance.getInstance().intakesubsystem);
    addRequirements(SubsystemsInstance.getInstance().scorersubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isNote = SubsystemsInstance.getInstance().intakesubsystem.isNoteIn();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (isNote) {
      SubsystemsInstance.getInstance().intakesubsystem.runIntake(-intakeTransferSpeed);
      SubsystemsInstance.getInstance().scorersubsystem.runFeeder(maxFeederSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SubsystemsInstance.getInstance().intakesubsystem.runIntake(0);
    SubsystemsInstance.getInstance().scorersubsystem.runFeeder(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return SubsystemsInstance.getInstance().scorersubsystem.isNoteIn() || !isNote;
  }
}
