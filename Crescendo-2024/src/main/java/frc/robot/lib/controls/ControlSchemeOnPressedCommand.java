package frc.robot.lib.controls;

import edu.wpi.first.wpilibj2.command.Command;

public class ControlSchemeOnPressedCommand extends ControlSchemeCommand {
    
    public ControlSchemeOnPressedCommand(String button, Command command) {
        super(button, command);
    }

    @Override
    public void accept(ControlSchemeVisitor visitor) {
        visitor.visit(this);
    }

}
