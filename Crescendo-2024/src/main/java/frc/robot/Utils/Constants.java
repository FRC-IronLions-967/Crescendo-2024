package frc.robot.Utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DoNothingCommand;

public final class Constants {

    public static final double kDriveRadius = Math.sqrt(0.308 * 0.308 + 0.308 * 0.308); //radius from center of drive to one module
    public static final double kMaxSpeed = 4.42; // 4.42 meters per second / 14.5 ft per second
    public static final double kMaxAcceleration = 10.0; // 6.0 meters per second per second
    public static final double kMaxAngularSpeed = kMaxSpeed / kDriveRadius; // Maximum angular velocity 
    public static final double kMaxAngularAcceleration = kMaxAcceleration / kDriveRadius; // Maximum angular acceleration
    public static final double kWheelRadius = 0.0483;
    //the number above is acurate
    public static final double kGearRatio = 6.75;
    //needs tunings
    public static final double kSecondsPerMinute = 60.0;
  
    // private static final double kModuleMaxAngularVelocity = kMaxAngularSpeed;
    // private static final double kModuleMaxAngularAcceleration =
    //     2 * Math.PI; // radians per second squared
    public static final Translation2d m_frontLeftLocation = new Translation2d(0.308, 0.308);
    public static final Translation2d m_frontRightLocation = new Translation2d(0.308, -0.308);
    public static final Translation2d m_backLeftLocation = new Translation2d(-0.308, 0.308);
    public static final Translation2d m_backRightLocation = new Translation2d(-0.308, -0.308);
      //real numbers xare put in above
    public static final SwerveDriveKinematics m_kinematics =
    new SwerveDriveKinematics(
       m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
    new TrapezoidProfile.Constraints(
        kMaxAngularSpeed, kMaxAngularAcceleration);

    public static final Map<String, Command> autoEventMap = new HashMap<>();
    static {
      autoEventMap.put("autoEvent1", new DoNothingCommand());
      autoEventMap.put("autoEvent2", new DoNothingCommand());
      autoEventMap.put("autoEvent3", new DoNothingCommand());
    }

    public static final Map<Integer, Double> kSwerveOffsets = new HashMap<>();
    static {
      kSwerveOffsets.put(0, 2.017568133691152);
      kSwerveOffsets.put(1, 1.6252273289298291);
      kSwerveOffsets.put(2, -2.7157190558997755);
      kSwerveOffsets.put(3, 0.23217007031624007);
    }

    public static final Map<Double, Double> swerveRotMap = new HashMap<>();
    static {
      swerveRotMap.put(0.00, 0.00);
      swerveRotMap.put(0.01, 0.00);
      swerveRotMap.put(0.02, 0.00);
      swerveRotMap.put(0.03, 0.00);
      swerveRotMap.put(0.04, 0.00);
      swerveRotMap.put(0.05, 0.00);
      swerveRotMap.put(0.06, 0.00);
      swerveRotMap.put(0.07, 0.00);
      swerveRotMap.put(0.08, 0.00);
      swerveRotMap.put(0.09, 0.00);
      swerveRotMap.put(0.10, 0.00);
      double y;
      y = 0.00;
      for (double x = 0.00; x < 1.01; x = x + 0.01) {
        if (x < 0.10) {
          swerveRotMap.put(x, y);
        }
        if (x >= 0.1 && x < 0.2) {
          y = y + 0.0005;
          swerveRotMap.put(x, y);
        }
        if (x >= 0.2 && x < 0.3) {
          y = y + 0.001;
          swerveRotMap.put(x, y);
        }
        if (x >= 0.3 && x < 0.4) {
          y = y + 0.005;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.3 && x < 0.4) {
          y = y + 0.01;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.4 && x < 0.5) {
          y = y + 0.015;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.5 && x < 0.6) {
          y = y + 0.02;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.6 && x < 0.7) {
          y = y + 0.025;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.7 && x < 0.75) {
          y = y + 0.03;
          swerveRotMap.put(x, y);        
        }
        if (x >= 0.75) {
          y = 1;
          swerveRotMap.put(x, y);          
        }
      }
    }

}
