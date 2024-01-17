package frc.robot.subsystems;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Utils.Constants;

public class SubsystemsInstance {
    public Drivetrain drivetrain;
   
    private static SubsystemsInstance inst;

    private SubsystemsInstance() {
        drivetrain = new Drivetrain();
        

        CommandScheduler.getInstance().registerSubsystem(drivetrain);
       

    }
    public static SubsystemsInstance getInstance () {
        if(inst == null) inst = new SubsystemsInstance();

        return inst;

    }
    
}