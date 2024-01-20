// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Utils.Constants;

public class ScorerSubsystem extends SubsystemBase {

  private CANSparkMax scorerMotor;
  private CANSparkMax pivotMotor;
  private CANSparkMax feederMotor;
  private SparkPIDController scorerMotorPID;
  private SparkPIDController pivotMotorPID;
  private SparkPIDController feederMotorPID;
  private DigitalInput feederLimit1; 
  private DigitalInput feederLimit2;
  private boolean hasNote;
  /** Creates a new ScorerSubsystem. */
  public ScorerSubsystem() {
    scorerMotor = new CANSparkMax(11, MotorType.kBrushless);
    pivotMotor = new CANSparkMax(12, MotorType.kBrushless);
    feederMotor = new CANSparkMax(13, MotorType.kBrushless);
    scorerMotorPID = scorerMotor.getPIDController();
    scorerMotorPID.setP(1);
    scorerMotorPID.setI(0);
    scorerMotorPID.setD(0);
    scorerMotorPID.setFF(0);
    pivotMotorPID = pivotMotor.getPIDController();
    pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).setPositionConversionFactor(360.0);
    pivotMotorPID.setFeedbackDevice(pivotMotor.getAbsoluteEncoder(Type.kDutyCycle));
    pivotMotorPID.setP(1);
    pivotMotorPID.setI(0);
    pivotMotorPID.setD(0);
    pivotMotorPID.setFF(0);
    feederMotorPID = feederMotor.getPIDController();
    feederMotorPID.setP(1);
    feederMotorPID.setI(0);
    feederMotorPID.setD(0);
    feederMotorPID.setFF(0);
    feederLimit1 = new DigitalInput(3);
    feederLimit2 = new DigitalInput(4);
  }

  public void runScorer(double speed) {
    if(speed < -Constants.kMaxNEOSpeed) speed = -Constants.kMaxNEOSpeed;
    if(speed > Constants.kMaxNEOSpeed) speed = Constants.kMaxNEOSpeed;

    scorerMotorPID.setReference(speed, ControlType.kVelocity);
    new WaitCommand(0.5);
    feederMotorPID.setReference(1, ControlType.kVelocity);
    new WaitCommand(0.5);
    scorerMotorPID.setReference(0, ControlType.kVelocity);
    feederMotorPID.setReference(0, ControlType.kVelocity);
  }

  public void runFeeder(double speed) {
    if(speed < -Constants.kMaxNEOSpeed) speed = -Constants.kMaxNEOSpeed;
    if(speed > Constants.kMaxNEOSpeed) speed = Constants.kMaxNEOSpeed;

    feederMotorPID.setReference(1, ControlType.kVelocity);
  }

  public void moveShooter(double position) {
    if(position < Constants.kScorerMinPosition) position = Constants.kScorerMinPosition;
    if(position > Constants.kScorerMaxPosition) position = Constants.kScorerMaxPosition;
  
    pivotMotorPID.setReference(position, ControlType.kPosition);
  }

  public double getScorerPosition() {
    return pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).getPosition();
  }

  public boolean isNoteIn() {
    return hasNote;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (feederLimit1.get() || feederLimit2.get()) hasNote = true;
  }
}
