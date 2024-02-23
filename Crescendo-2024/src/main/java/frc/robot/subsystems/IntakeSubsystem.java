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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Utils.Values;


public class IntakeSubsystem extends SubsystemBase {

  private CANSparkMax intakeMotor;
  private SparkPIDController intakeMotorPID;
  private CANSparkMax pivotMotor;
  private SparkPIDController pivotMotorPID;
  private DigitalInput isNoteIn;
  private boolean hasNote;  
  private double kIntakeMaxPosition;
  private double kIntakeMinPosition;
  private double kMaxNEOSpeed;

  public boolean intakePostionOut;
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    kIntakeMaxPosition = Values.getInstance().getDoubleValue("kIntakeMaxPosition");
    kIntakeMinPosition = Values.getInstance().getDoubleValue("kIntakeMinPosition");
    kMaxNEOSpeed = Values.getInstance().getDoubleValue("kMaxNEOSpeed");


    intakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    intakeMotorPID = intakeMotor.getPIDController();
    intakeMotorPID.setP(Values.getInstance().getDoubleValue("intakeMotorP"));
    intakeMotorPID.setI(Values.getInstance().getDoubleValue("intakeMotorI"));
    intakeMotorPID.setD(Values.getInstance().getDoubleValue("intakeMotorD"));
    intakeMotorPID.setFF(Values.getInstance().getDoubleValue("intakeMotorFF"));
    intakeMotor.setClosedLoopRampRate(0.75);
    intakeMotor.setOpenLoopRampRate(0.75);

    pivotMotor = new CANSparkMax(10, MotorType.kBrushless);
    pivotMotorPID = pivotMotor.getPIDController();
    pivotMotorPID.setFeedbackDevice(pivotMotor.getAbsoluteEncoder(Type.kDutyCycle));
    pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).setPositionConversionFactor(1);
    pivotMotorPID.setOutputRange(-0.7, 0.7);
    pivotMotorPID.setP(Values.getInstance().getDoubleValue("intakePivotMotorP"));
    pivotMotorPID.setI(Values.getInstance().getDoubleValue("intakePivotMotorI"));
    pivotMotorPID.setD(Values.getInstance().getDoubleValue("intakePivotMotorD"));
    pivotMotorPID.setFF(Values.getInstance().getDoubleValue("intakePivotMotorFF"));
    pivotMotor.setClosedLoopRampRate(0.5);
    pivotMotorPID.setPositionPIDWrappingEnabled(false);

    isNoteIn = new DigitalInput(2);
    hasNote = false;
  }


  
  public void runIntake(double speed) {
    if(speed < -kMaxNEOSpeed) speed = -kMaxNEOSpeed;
    if(speed > kMaxNEOSpeed) speed = kMaxNEOSpeed;

    intakeMotorPID.setReference(speed, ControlType.kVelocity);
  }

  public void moveIntake(double position) {
    if(position < kIntakeMinPosition) position = kIntakeMinPosition;
    if(position > kIntakeMaxPosition) position = kIntakeMaxPosition;
  
    pivotMotorPID.setReference(position, ControlType.kPosition);
  }

  public double getIntakePosition() {
    return pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).getPosition();
  }

  public boolean isNoteIn() {
    return hasNote;
  }

  public void setVoltage(double voltage) {
    intakeMotor.set(voltage);
  }

  public void registerCanLogging() {
    SubsystemsInstance.getInstance().canLogger.registerSpark(9, intakeMotor);
    SubsystemsInstance.getInstance().canLogger.registerSpark(10, pivotMotor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    hasNote = isNoteIn.get();
    SmartDashboard.putNumber("Intake Speed", intakeMotor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Intake Angle", getIntakePosition());
    SmartDashboard.putBoolean("Limit Switch", hasNote);
  }
}
