package frc.robot.lib.controls;

import java.util.List;

public class ControlScheme {

    private List<ControlSchemeCommand> commandList;

    public ControlScheme(List<ControlSchemeCommand> commandList) {
        this.commandList = commandList;
    }

    public List<ControlSchemeCommand> getControlSchemeCommands() {
        return commandList;
    }
}
