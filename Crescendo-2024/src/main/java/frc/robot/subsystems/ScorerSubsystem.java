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
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ScorerSubsystem extends SubsystemBase {

  private CANSparkMax scorerMotor;
  private CANSparkMax scorerPivotMotor;
  private CANSparkMax feederMotor;
  private SparkPIDController scorerMotorPID;
  private SparkPIDController scorerPivotMotorPID;
  private SparkPIDController feederMotorPID;
  private DigitalInput feederLimit1; 
  private DigitalInput feederLimit2;
  /** Creates a new ScorerSubsystem. */
  public ScorerSubsystem() {
    scorerMotor = new CANSparkMax(11, MotorType.kBrushless);
    scorerPivotMotor = new CANSparkMax(12, MotorType.kBrushless);
    feederMotor = new CANSparkMax(13, MotorType.kBrushless);
    scorerMotorPID = scorerMotor.getPIDController();
    scorerMotorPID.setP(1);
    scorerMotorPID.setI(0);
    scorerMotorPID.setD(0);
    scorerMotorPID.setFF(0);
    scorerPivotMotorPID = scorerPivotMotor.getPIDController();
    scorerPivotMotorPID.setP(1);
    scorerPivotMotorPID.setI(0);
    scorerPivotMotorPID.setD(0);
    scorerPivotMotorPID.setFF(0);
    feederMotorPID = feederMotor.getPIDController();
    feederMotorPID.setP(1);
    feederMotorPID.setI(0);
    feederMotorPID.setD(0);
    feederMotorPID.setFF(0);
    feederLimit1 = new DigitalInput(3);
    feederLimit2 = new DigitalInput(4);
  }

  public void runScorer() {
    scorerMotorPID.setReference(1, ControlType.kVelocity);
    new WaitCommand(0.5);
    feederMotorPID.setReference(1, ControlType.kVelocity);
    new WaitCommand(0.5);
    scorerMotorPID.setReference(0, ControlType.kVelocity);
    feederMotorPID.setReference(0, ControlType.kVelocity);
  }

  public void moveShooter(double speed) {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (feederLimit1.get() || feederLimit2.get()) {
      feederMotorPID.setReference(0, ControlType.kVelocity);
    }
  }
}
