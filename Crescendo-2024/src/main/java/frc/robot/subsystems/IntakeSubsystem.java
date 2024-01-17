// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeSubsystem extends SubsystemBase {

  private CANSparkMax intakeMotor;
  private SparkMaxPIDController intakeMotorPID;
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    intakeMotorPID = intakeMotor.getPIDController();
    intakeMotorPID.setP(1.0);
    intakeMotorPID.setI(0.0);
    intakeMotorPID.setD(0.0);
    intakeMotorPID.setFF(0.0);
  }
  
  public void runIntake(double speed) {
    intakeMotorPID.setReference(speed, ControlType.kVelocity);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
