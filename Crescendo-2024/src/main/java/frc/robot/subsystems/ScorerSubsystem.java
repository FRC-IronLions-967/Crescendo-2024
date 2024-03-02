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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.Utils.Values;

public class ScorerSubsystem extends SubsystemBase {

  private CANSparkMax scorerMotor;
  private CANSparkMax pivotMotor;
  private CANSparkMax feederMotor;
  private SparkPIDController scorerMotorPID;
  private SparkPIDController pivotMotorPID;
  private SparkPIDController feederMotorPID;
  private DigitalInput feederLimit1; 
  private boolean hasNote;

  private double kScorerMaxPosition;
  private double kScorerMinPosition;
  private double pivotPosition;
  private double speedTolerance;
  private double kMaxNEOSpeed;

  private ScorerStates state;
  private boolean startScorer;
  private double speed;

  private Timer timer;
  /** Creates a new ScorerSubsystem. */
  public ScorerSubsystem() {
    state = ScorerStates.IDLE;
    startScorer = false;

    timer = new Timer();

    kScorerMaxPosition = Values.getInstance().getDoubleValue("kScorerMaxPosition");
    kScorerMinPosition = Values.getInstance().getDoubleValue("kScorerMinPosition");
    speedTolerance = Values.getInstance().getDoubleValue("scorerSpeedTolerance");
    kMaxNEOSpeed = Values.getInstance().getDoubleValue("kMaxNEOSpeed");


    scorerMotor = new CANSparkMax(11, MotorType.kBrushless);
    pivotMotor = new CANSparkMax(12, MotorType.kBrushless);
    feederMotor = new CANSparkMax(13, MotorType.kBrushless);
    // pivotMotor.setInverted(true);
    scorerMotorPID = scorerMotor.getPIDController();
    scorerMotorPID.setP(Values.getInstance().getDoubleValue("scorerMotorP"));
    scorerMotorPID.setI(Values.getInstance().getDoubleValue("scorerMotorI"));
    scorerMotorPID.setD(Values.getInstance().getDoubleValue("scorerMotorD"));
    scorerMotorPID.setFF(Values.getInstance().getDoubleValue("scorerMotorFF"));
    scorerMotorPID.setIMaxAccum(10000, 0);
    scorerMotor.setClosedLoopRampRate(0.25);

    pivotMotorPID = pivotMotor.getPIDController();
    pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).setInverted(true);
    pivotMotorPID.setFeedbackDevice(pivotMotor.getAbsoluteEncoder(Type.kDutyCycle));
    pivotMotorPID.setOutputRange(-0.7, 0.7);
    pivotMotorPID.setP(Values.getInstance().getDoubleValue("scorerPivotMotorP"));
    pivotMotorPID.setI(Values.getInstance().getDoubleValue("scorerPivotMotorI"));
    pivotMotorPID.setD(Values.getInstance().getDoubleValue("scorerPivotMotorD"));
    pivotMotorPID.setFF(Values.getInstance().getDoubleValue("scorerPivotMotorFF"));
    pivotMotor.setClosedLoopRampRate(0.5);
    pivotMotorPID.setPositionPIDWrappingEnabled(false);
    
    feederMotorPID = feederMotor.getPIDController();
    feederMotorPID.setP(Values.getInstance().getDoubleValue("feederMotorP"));
    feederMotorPID.setI(Values.getInstance().getDoubleValue("feederMotorI"));
    feederMotorPID.setD(Values.getInstance().getDoubleValue("feederMotorD"));
    feederMotorPID.setFF(Values.getInstance().getDoubleValue("feederMotorFF"));
    feederLimit1 = new DigitalInput(Values.getInstance().getIntValue("feederLimit1"));
    feederMotor.setClosedLoopRampRate(0.25);
  }

  public void runScorer(double speed) {
    SmartDashboard.putBoolean("runScorer Called", true);
    if (!startScorer) {
      startScorer = true;
      this.speed = speed;
    }
  }

  public void runFeeder(double speed) {
    if(speed < -kMaxNEOSpeed) speed = -kMaxNEOSpeed;
    if(speed > kMaxNEOSpeed) speed = kMaxNEOSpeed;

    feederMotorPID.setReference(speed, ControlType.kVelocity);
  }

  public void moveShooter(double position) {
    if(position < kScorerMinPosition) position = kScorerMinPosition;
    if(position > kScorerMaxPosition) position = kScorerMaxPosition;
  
    pivotMotorPID.setReference(position, ControlType.kPosition);
    pivotPosition = position;
  }

  public double getScorerPosition() {
    return pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).getPosition();
  }

  public void adjustShooter(double delta) {
    pivotPosition += delta; 

    if(pivotPosition < kScorerMinPosition) pivotPosition = kScorerMinPosition;
    if(pivotPosition > kScorerMaxPosition) pivotPosition = kScorerMaxPosition;
  
    pivotMotorPID.setReference(pivotPosition, ControlType.kPosition);
  }

  public boolean isNoteIn() {
    return hasNote;
  }

  public void moveFlyWheel(double speed) {
    scorerMotorPID.setReference(speed, ControlType.kVelocity);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("startScorer", startScorer);
    // This method will be called once per scheduler run
    if (!IO.getInstance().isManualMode()) {
      switch (state) {
        case IDLE:
          feederMotorPID.setReference(0, ControlType.kVelocity);
          scorerMotorPID.setReference(0, ControlType.kVelocity);
          if (startScorer) {
            state = ScorerStates.RAMP_UP;
          }
          break;
        case RAMP_UP:
        scorerMotorPID.setReference(speed, ControlType.kVelocity);
          if ((speed - speedTolerance <= scorerMotor.getEncoder().getVelocity() && 
              speed + speedTolerance >= scorerMotor.getEncoder().getVelocity() && 
              IO.getInstance().getManipulatorController().getLeftTrigger() > 0.5) ||
              (speed - speedTolerance <= scorerMotor.getEncoder().getVelocity() && 
              speed + speedTolerance >= scorerMotor.getEncoder().getVelocity() && 
              DriverStation.isAutonomousEnabled()))
            state = ScorerStates.SHOOT;
          break;
        case SHOOT:
        if (feederLimit1.get()) {
          feederMotorPID.setReference(Values.getInstance().getDoubleValue("maxFeederSpeed"), ControlType.kVelocity);
          if (!feederLimit1.get()) {
            state = ScorerStates.DELAY;
            timer.start();
          }
        } else {
          scorerMotorPID.setReference(0, ControlType.kVelocity);
          state = ScorerStates.IDLE;
        }
          break;
        case DELAY:
          if (timer.hasElapsed(0.5)) {
            state = ScorerStates.IDLE;
            startScorer = false;
          }
          break;
        default:
          state = ScorerStates.IDLE;
          startScorer = false;
      }
    }
    hasNote = feederLimit1.get();
    SmartDashboard.putNumber("Shooter Angle", pivotMotor.getAbsoluteEncoder(Type.kDutyCycle).getPosition());
    SmartDashboard.putNumber("Shooter Speed", scorerMotor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Feeder Speed", feederMotor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Pivot Output", pivotMotor.getAppliedOutput());
    SmartDashboard.putBoolean("Feeder Limit", hasNote);
  }
}
