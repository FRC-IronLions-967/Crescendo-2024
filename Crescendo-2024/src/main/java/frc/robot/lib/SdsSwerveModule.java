// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Utils.Constants;
import frc.robot.Utils.Values;

public class SdsSwerveModule {

  private double swerveTurningP;
  private double swerveTurningI;
  private double swerveTurningD;
  private double swerveDriveMotorP;
  private double swerveDriveMotorI;
  private double swerveDriveMotorD;
  private double swerveDriveMotorFF;

  private CANSparkMax driveMotor;
  private SparkPIDController driveMotorController;
  private CANSparkMax turningMotor;

  private int driveID;

  private ThriftyEncoder turningEncoder;

  private int i;
  // Gains are for example purposes only - must be determined for your own robot!
  private final PIDController turningPIDController =
       new PIDController(
          0,0,0);


  // Gains are for example purposes only - must be determined for your own robot!
  //private final SimpleMotorFeedforward m_driveFeedforward = new SimpleMotorFeedforward(1, 3);
  //private final SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(1, 0.5);
  
  /**
   * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
   *
   * @param driveMotorCANId PWM output for the drive motor.
   * @param turningMotorCANId PWM output for the turning motor.
   * @param turningEncoderAnalogPort Analog input for the turning encoder channel A
   */
  public SdsSwerveModule(
      int driveMotorCANId,
      int turningMotorCANId,
      int turningEncoderAnalogPort) {
    driveMotor = new CANSparkMax(driveMotorCANId, MotorType.kBrushless);
    turningMotor = new CANSparkMax(turningMotorCANId, MotorType.kBrushless);
    turningMotor.setIdleMode(IdleMode.kBrake);
    driveMotor.setIdleMode(IdleMode.kCoast);
    driveID = driveMotorCANId;

    turningPIDController.setTolerance(0.02,0.0);

    swerveTurningP = Values.getInstance().getDoubleValue("swerveTurningP");
    swerveTurningI = Values.getInstance().getDoubleValue("swerveTurningI");
    swerveTurningD = Values.getInstance().getDoubleValue("swerveTurningD");
    swerveDriveMotorP = Values.getInstance().getDoubleValue("swerveDriveMotorP");
    swerveDriveMotorI = Values.getInstance().getDoubleValue("swerveDriveMotorI");
    swerveDriveMotorD = Values.getInstance().getDoubleValue("swerveDriveMotorD");
    swerveDriveMotorFF = Values.getInstance().getDoubleValue("swerveDriveMotorFF");
    

    //REVPhysicsSim.getInstance().addSparkMax(driveMotor, DCMotor.getNEO(1));
    //REVPhysicsSim.getInstance().addSparkMax(turningMotor, DCMotor.getVex775Pro(1));

    turningEncoder = new ThriftyEncoder(turningEncoderAnalogPort);

    /*
     * native units of rpm to m/s
     */
    driveMotor.getEncoder().setVelocityConversionFactor(Constants.kMaxSpeed/5700);
    //driveMotor.getEncoder().setVelocityConversionFactor((2 * Math.PI * kWheelRadius) / (kSecondsPerMinute * kGearRatio));
    //driveMotor.getEncoder().setPositionConversionFactor((2 * Math.PI * kWheelRadius) / (kSecondsPerMinute * kGearRatio));
    driveMotorController = driveMotor.getPIDController();
    driveMotorController.setP(swerveDriveMotorP);
    driveMotorController.setI(swerveDriveMotorI);
    driveMotorController.setD(swerveDriveMotorD);
    driveMotorController.setFF(swerveDriveMotorFF);

    turningPIDController.reset();
    turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
    turningPIDController.setPID(
      swerveTurningP,
      swerveTurningI,
      swerveTurningD);
  }


  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        driveMotor.getEncoder().getVelocity(), new Rotation2d(turningEncoder.getAbsolutePosition()));
  }

  /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        driveMotor.getEncoder().getPosition(), new Rotation2d(turningEncoder.getAbsolutePosition()));
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    // Optimize the reference state to avoid spinning further than 90 degrees
    SwerveModuleState state =
        SwerveModuleState.optimize(desiredState, new Rotation2d(turningEncoder.getAbsolutePosition()));
    // Calculate the turning motor output from the turning PID controller.
    final double turnOutput =
      turningPIDController.calculate(turningEncoder.getAbsolutePosition(), MathUtil.angleModulus(state.angle.getRadians()));
      
    if (i == 0) {
      // System.out.println("Measured Angle   " + iCanId + ":   " + turningEncoder.getAbsolutePosition());
      // System.out.println("Commanded Angle  " + iCanId + ":   " + state.angle.getRadians());
      // System.out.println("Commanded Speed " + iCanId + ":   " + state.speedMetersPerSecond);
      // System.out.println("Motor Speed     " + iCanId + ": " + driveMotor.getEncoder().getVelocity());
    }
    i = (i + 1) % 100;

    SmartDashboard.putNumber("Drive RPM" + driveID, driveMotor.getEncoder().getVelocity());

    driveMotorController.setReference(state.speedMetersPerSecond, ControlType.kVelocity);
    turningMotor.setVoltage(turnOutput);
  }
}