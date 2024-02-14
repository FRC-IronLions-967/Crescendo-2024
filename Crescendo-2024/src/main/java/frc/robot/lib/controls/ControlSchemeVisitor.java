package frc.robot.lib.controls;

public interface ControlSchemeVisitor {
    
    public void visit(ControlSchemeOnPressedCommand command);

    public void visit(ControlSchemeOnReleasedCommand command);

}
