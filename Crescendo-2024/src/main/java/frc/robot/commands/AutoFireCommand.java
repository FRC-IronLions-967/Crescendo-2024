// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Values;
import frc.robot.subsystems.SubsystemsInstance;

public class AutoFireCommand extends Command {
  /** Creates a new AutoFireCommand. */
  public AutoFireCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(SubsystemsInstance.getInstance().scorersubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(SubsystemsInstance.getInstance().scorersubsystem.autoReadyToFire()) {
      SubsystemsInstance.getInstance().scorersubsystem.runFeeder(Values.getInstance().getDoubleValue("maxFeederSpeed"));
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SubsystemsInstance.getInstance().scorersubsystem.moveFlyWheel(0);
    SubsystemsInstance.getInstance().scorersubsystem.runFeeder(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !SubsystemsInstance.getInstance().scorersubsystem.isNoteIn();
  }
}
