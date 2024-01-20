// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Utils.Constants;


public class IntakeSubsystem extends SubsystemBase {

  private CANSparkMax intakeMotor;
  private SparkPIDController intakeMotorPID;
  private CANSparkMax pivotMotor;
  private SparkPIDController pivotMotorPID;
  private DigitalInput isNoteIn;
  private boolean hasNote;  

  public boolean intakePostionOut;
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    intakeMotorPID = intakeMotor.getPIDController();
    intakeMotorPID.setP(1.0);
    intakeMotorPID.setI(0.0);
    intakeMotorPID.setD(0.0);
    intakeMotorPID.setFF(0.0);

    pivotMotor = new CANSparkMax(10, MotorType.kBrushless);
    pivotMotorPID = pivotMotor.getPIDController();
    pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).setPositionConversionFactor(360.0);
    pivotMotorPID.setFeedbackDevice(pivotMotor.getAbsoluteEncoder(Type.kDutyCycle));
    pivotMotorPID.setP(1.0);
    pivotMotorPID.setI(0.0);
    pivotMotorPID.setD(0.0);
    pivotMotorPID.setFF(0.0);

    isNoteIn = new DigitalInput(2);
    hasNote = false;
  }
  
  public void runIntake(double speed) {
    if(speed < -Constants.kMaxNEOSpeed) speed = -Constants.kMaxNEOSpeed;
    if(speed > Constants.kMaxNEOSpeed) speed = Constants.kMaxNEOSpeed;

    intakeMotorPID.setReference(speed, ControlType.kVelocity);
  }

  public void moveIntake(double position) {
    if(position < Constants.kIntakeMinPosition) position = Constants.kIntakeMinPosition;
    if(position > Constants.kIntakeMaxPosition) position = Constants.kIntakeMaxPosition;
  
    pivotMotorPID.setReference(position, ControlType.kPosition);
  }

  public double getIntakePosition() {
    return pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).getPosition();
  }

  public boolean isNoteIn() {
    return hasNote;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    hasNote = isNoteIn.get();
  }
}
