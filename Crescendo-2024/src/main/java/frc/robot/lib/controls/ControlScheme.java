package frc.robot.lib.controls;

import java.util.Map;

import edu.wpi.first.wpilibj2.command.Command;

public class ControlScheme {
    private Map<String, Command> commandMap;
    public ControlScheme(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    public boolean hasMapping(String button) {
        return commandMap.containsKey(button);
    }
    public Command getMapping(String button) {
        return commandMap.get(button);
    }
}
