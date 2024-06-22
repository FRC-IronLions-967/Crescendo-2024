package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DefaultMoveCommand;
import frc.robot.lib.LEDController;

public class SubsystemsInstance {
    public Drivetrain drivetrain;
    public IntakeSubsystem intakesubsystem;
    public ScorerSubsystem scorersubsystem;
   
    private static SubsystemsInstance inst;

    private SubsystemsInstance() {
        drivetrain = new Drivetrain();
        intakesubsystem = new IntakeSubsystem();
        scorersubsystem = new ScorerSubsystem();

        
        
        LEDController.ConfigureLedEvents(() -> intakesubsystem.isNoteIn(), () -> scorersubsystem.isNoteIn());
        //CommandScheduler.getInstance().registerSubsystem(drivetrain);
        // CommandScheduler.getInstance().registerSubsystem(intakesubsystem);
        // CommandScheduler.getInstance().registerSubsystem(scorersubsystem);
       

    }

    public static SubsystemsInstance getInstance () {
        if(inst == null) inst = new SubsystemsInstance();

        return inst;

    }
    
}