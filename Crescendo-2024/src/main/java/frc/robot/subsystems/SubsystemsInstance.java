package frc.robot.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;

public class SubsystemsInstance {
    public Drivetrain drivetrain;
    public IntakeSubsystem intakesubsystem;
    public ScorerSubsystem scorersubsystem;
   
    private static SubsystemsInstance inst;

    private SubsystemsInstance() {
        drivetrain = new Drivetrain();
        intakesubsystem = new IntakeSubsystem();
        scorersubsystem = new ScorerSubsystem();

        drivetrain.setupPathPlanner();
        

        //CommandScheduler.getInstance().registerSubsystem(drivetrain);
        // CommandScheduler.getInstance().registerSubsystem(intakesubsystem);
        // CommandScheduler.getInstance().registerSubsystem(scorersubsystem);
       

    }

    public static SubsystemsInstance getInstance () {
        if(inst == null) inst = new SubsystemsInstance();

        return inst;

    }
    
}