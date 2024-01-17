// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.Utils.Constants;
import frc.robot.lib.SdsSwerveModule;
import frc.robot.lib.controls.XBoxController;



/** Represents a swerve drive style drivetrain. */
public class Drivetrain extends SubsystemBase {
  private int i;
  public boolean fieldRelative;

  // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
  private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(10);
  private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(10);
  private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(10);

  private final SdsSwerveModule m_frontLeft = new SdsSwerveModule(1, 2, 0);
  private final SdsSwerveModule m_frontRight = new SdsSwerveModule(7, 8, 3);
  private final SdsSwerveModule m_backLeft = new SdsSwerveModule(3, 4, 1);
  private final SdsSwerveModule m_backRight = new SdsSwerveModule(5, 6, 2);
//first two colums above are done
  private final AHRS m_gyro = new AHRS(SerialPort.Port.kMXP);


  private final SwerveDriveOdometry m_odometry =
      new SwerveDriveOdometry(
          Constants.m_kinematics,
          m_gyro.getRotation2d(),
          new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_backLeft.getPosition(),
            m_backRight.getPosition()
          });

  public Drivetrain() {
    m_gyro.reset();
    fieldRelative = true;
    i = 0;
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    var swerveModuleStates =
        Constants.m_kinematics.toSwerveModuleStates(
            fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, Rotation2d.fromDegrees(-m_gyro.getFusedHeading()))
                : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.kMaxSpeed);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_backLeft.setDesiredState(swerveModuleStates[2]);
    m_backRight.setDesiredState(swerveModuleStates[3]);
  }

  public void setModuleStates(SwerveModuleState[] swerveModuleStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.kMaxSpeed);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_backLeft.setDesiredState(swerveModuleStates[2]);
    m_backRight.setDesiredState(swerveModuleStates[3]);
  }

  /** Updates the field relative position of the robot. */
  public void updateOdometry() {
    m_odometry.update(
        m_gyro.getRotation2d(),
        new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_backLeft.getPosition(),
          m_backRight.getPosition()
        });
        new Pose2d(5.0/* change these*/, 13.5, new Rotation2d());
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    //m_odometry.resetPosition(pose, m_gyro.getRotation2d());
  }

  /**
   * Get joystick values 
   * Set motor inputs
   */
  @Override 
    public void periodic(){
      XBoxController driveController = IO.getInstance().getDriverController();
      // Get the x speed. We are inverting this because Xbox controllers return
      // negative values when we push forward.
      final var xSpeed = m_xspeedLimiter.calculate(
      -MathUtil.applyDeadband(driveController.getLeftStickY(), 0.2)
          * Constants.kMaxSpeed);

      // Get the y speed or sideways/strafe speed. We are inverting this because
      // we want a positive value when we pull to the left. Xbox controllers
      // return positive values when you pull to the right by default.
      final var ySpeed = m_yspeedLimiter.calculate(
          MathUtil.applyDeadband(-driveController.getLeftStickX(), 0.2)
              * Constants.kMaxSpeed);

      // Get the rate of angular rotation. We are inverting this because we want a
      // positive value when we pull to the left (remember, CCW is positive in
      // mathematics). Xbox controllers return positive values when you pull to
      // the right by default.
      final var rot = m_rotLimiter.calculate(
          MathUtil.applyDeadband(-driveController.getRightStickX(), 0.2)
              * Constants.kMaxAngularSpeed);

      drive(xSpeed, ySpeed, rot, fieldRelative);
      
      
      // Get the rotation of the robot from the gyro.
      var gyroAngle = m_gyro.getRotation2d();
      // System.out.println(m_gyro.getFusedHeading());

      // Update the pose
      var m_pose = m_odometry.update(gyroAngle,
      new SwerveModulePosition[] {
      m_frontLeft.getPosition(), m_frontRight.getPosition(),
      m_backLeft.getPosition(), m_backRight.getPosition()
      });
    } 
     
}