package frc.robot.lib.controls;

import edu.wpi.first.wpilibj2.command.Command;

public abstract class ControlSchemeCommand {
    
    private String button;
    private Command command;

    public ControlSchemeCommand(String button, Command command) {
        this.button = button;
        this.command = command;
    }

    public String getButton() {
        return button;
    }

    public Command getCommand() {
        return command;
    }

    public abstract void accept(ControlSchemeVisitor visitor);

}
