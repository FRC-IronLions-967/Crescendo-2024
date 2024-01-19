// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeSubsystem extends SubsystemBase {

  private CANSparkMax intakeMotor;
  private SparkPIDController intakeMotorPID;
  private CANSparkMax intakePivotMotor;
  private SparkPIDController intakePivotMotorPID;
  private DigitalInput isNoteIn;
  private double speed;

  public boolean intakePostionOut;
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    intakeMotorPID = intakeMotor.getPIDController();
    intakeMotorPID.setP(1.0);
    intakeMotorPID.setI(0.0);
    intakeMotorPID.setD(0.0);
    intakeMotorPID.setFF(0.0);

    intakePivotMotor = new CANSparkMax(10, MotorType.kBrushless);
    intakePivotMotorPID = intakePivotMotor.getPIDController();
    intakePivotMotorPID.setP(1.0);
    intakePivotMotorPID.setI(0.0);
    intakePivotMotorPID.setD(0.0);
    intakePivotMotorPID.setFF(0.0);

    isNoteIn = new DigitalInput(2);
  }
  
  public void runIntake(double speed) {
    intakeMotorPID.setReference(speed, ControlType.kVelocity);
  }

  public void moveIntake(double speed) {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (isNoteIn.get() && speed > 0) {
      intakeMotorPID.setReference(0, ControlType.kVelocity);
    }
  }
}
