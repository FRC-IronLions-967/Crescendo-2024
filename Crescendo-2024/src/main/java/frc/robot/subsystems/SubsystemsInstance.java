package frc.robot.subsystems;

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