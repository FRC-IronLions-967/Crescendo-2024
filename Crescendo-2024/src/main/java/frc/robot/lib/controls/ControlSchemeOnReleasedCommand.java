package frc.robot.lib.controls;

import edu.wpi.first.wpilibj2.command.Command;

public class ControlSchemeOnReleasedCommand extends ControlSchemeCommand {
    
    public ControlSchemeOnReleasedCommand(String button, Command command) {
        super(button, command);
    }

    @Override
    public void accept(ControlSchemeVisitor visitor) {
        visitor.visit(this);
    }

}
