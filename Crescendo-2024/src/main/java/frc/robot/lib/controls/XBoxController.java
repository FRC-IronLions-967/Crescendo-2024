package frc.robot.lib.controls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DoNothingCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;


public class XBoxController extends Joystick implements ControlSchemeVisitor {
    public static final int LEFT_X_AXIS = 0;
    public static final int LEFT_Y_AXIS = 1;
    public static final int LEFT_TRIGGER = 2;
    public static final int RIGHT_TRIGGER = 3;
    public static final int RIGHT_X_AXIS = 4;
    public static final int RIGHT_Y_AXIS = 5;

    //this controls how far you have to press the trigger for it to register as pressed
    private double triggerTolerance = 0.25;

    private HashMap<String, CustomTrigger> buttonMap;
    private HashMap<String, CustomTrigger> povMap;

    private ControlScheme controlScheme;

    public XBoxController(int id) {
        super(id);
        buttonMap = new HashMap<>();
        povMap = new HashMap<>();

        //initialize the buttons on the controller
        buttonMap.put("A", new CustomJoystickButton(this, 1));
        buttonMap.put("B", new CustomJoystickButton(this, 2));
        buttonMap.put("X", new CustomJoystickButton(this, 3));
        buttonMap.put("Y", new CustomJoystickButton(this, 4));
        buttonMap.put("LBUMP", new CustomJoystickButton(this, 5));
        buttonMap.put("RBUMP", new CustomJoystickButton(this, 6));
        buttonMap.put("SELECT", new CustomJoystickButton(this, 7));
        buttonMap.put("START", new CustomJoystickButton(this, 8));
        buttonMap.put("LTRIG", new CustomTrigger(this::isLeftTriggerPressed));
        buttonMap.put("RTRIG", new CustomTrigger(this::isRightTriggerPressed));

        for(int i = 1; i <= 8; i++) {
            Trigger button = new JoystickButton(this, i);
            button.onTrue(new DoNothingCommand());
            button.onFalse(new DoNothingCommand());
        }

        //initialize the POV buttons (the buttons on the dpad of the controller)
        //these constructor calls could be modified to take a 3rd argument that is their "POV value", which returns from another wpilib function, which I'm not using
        povMap.put("N", new CustomPOVButton(this, 0));
        povMap.put("NE", new CustomPOVButton(this, 45));
        povMap.put("E", new CustomPOVButton(this, 90));
        povMap.put("SE", new CustomPOVButton(this, 135));
        povMap.put("S", new CustomPOVButton(this, 180));
        povMap.put("SW", new CustomPOVButton(this, 225));
        povMap.put("W", new CustomPOVButton(this, 270));
        povMap.put("NW", new CustomPOVButton(this, 315));

        for(int i = 0; i <= 315; i += 45) {
            Trigger button = new POVButton(this, i);
            button.onTrue(new DoNothingCommand());
            button.onFalse(new DoNothingCommand());
        }
    }

    public XBoxController(int id, ControlScheme controlScheme) {
        super(id);
        
        setControlScheme(controlScheme);
    }

    public void setControlScheme(ControlScheme controlScheme) {
        this.controlScheme = controlScheme;

        // clear out the current command mapping
        // for(Entry<String, Trigger> e : buttonMap.entrySet()) {
        //     e.getValue().onTrue(new DoNothingCommand());
        //     e.getValue().onFalse(new DoNothingCommand());
        // }
        Set<String> buttons = buttonMap.keySet();
        for(String b : buttons) {
            // System.out.println("Setting " + b + " to DoNothing");
            buttonMap.get(b).clear();
            buttonMap.get(b).onTrue(new DoNothingCommand());
            buttonMap.get(b).onFalse(new DoNothingCommand());
        }

        // for(Entry<String, Trigger> e : povMap.entrySet()) {
        //     e.getValue().onTrue(new DoNothingCommand());
        //     e.getValue().onFalse(new DoNothingCommand());
        // }

        Set<String> povButtons = povMap.keySet();
        for(String b : povButtons) {
            // System.out.println("Setting " + b + " to DoNothing");
            povMap.get(b).clear();
            povMap.get(b).onTrue(new DoNothingCommand());
            povMap.get(b).onFalse(new DoNothingCommand());
        }

        // assign all of the commands in the control scheme to buttons
        for(ControlSchemeCommand c : controlScheme.getControlSchemeCommands()) {
            // System.out.println("Setting " + c.getButton() + " to new Command");
            c.accept(this);
        }
    }

    public ControlScheme getControlScheme() {
        return controlScheme;
    }

    //assigns commands to buttons
    public void whenButtonPressed(String button, Command command) {
        buttonMap.get(button).onTrue(command);
    }

    public void whenButtonReleased(String button, Command command) {
        buttonMap.get(button).onFalse(command);
    }

    public void whenPOVButtonPressed(String button, Command command) {
        povMap.get(button).onTrue(command);
    }

    public void whenPOVButtonReleased(String button, Command command) {
        povMap.get(button).onFalse(command);
    }

    //returns whether the specified button is pressed
    public boolean isButtonPressed(String button) {
        return buttonMap.get(button).getAsBoolean();
    }

    public boolean isAngleMatched(String povButton) {
        return povMap.get(povButton).getAsBoolean();
    }

    //these get the value between -1.0 and 1.0 that represent the various joysticks
    //please note that the triggers only range between 0.0 and 1.0
    public double getRightStickX() {
        return this.getRawAxis(RIGHT_X_AXIS);
    }

    public double getRightStickY() {
        return this.getRawAxis(RIGHT_Y_AXIS);
    }

    public double getRightTrigger() {
        return this.getRawAxis(RIGHT_TRIGGER);
    }

    public double getLeftTrigger() {
        return this.getRawAxis(LEFT_TRIGGER);
    }

    public double getLeftStickX() {
        return this.getRawAxis(LEFT_X_AXIS);
    }

    public double getLeftStickY() {
        return this.getRawAxis(LEFT_Y_AXIS);
    }

    public boolean isLeftTriggerPressed() {
        return (this.getRawAxis(LEFT_TRIGGER) > triggerTolerance);
    }

    public boolean isRightTriggerPressed() {
        return (this.getRawAxis(RIGHT_TRIGGER) > triggerTolerance);
    }

    public void setTriggerTolerance(double newTolerance) {
        newTolerance = (newTolerance < 0.0) ? 0.0 : (newTolerance > 1.0) ? 1.0 : newTolerance;
        triggerTolerance = newTolerance;
    }

    public double getTriggerTolerance() {
        return triggerTolerance;
    }

    //the triggers on the controller show up as axes, but a common function is to check whether they are pressed or not
    //triggerTolerance can be set with setTriggerTolerance, and if the trigger is pressed that point, this returns true, otherwise false
    public boolean isTriggerPressed(int trigger) {
        if(trigger != 2 && trigger != 3) return false;
        return (this.getRawAxis(trigger) > triggerTolerance) ? true : false;
    }

    /**
     * Overridden method from ControlSchemeVisitor interface, provides a clean way to set commands to run on pressed.
     */
    public void visit(ControlSchemeOnPressedCommand command) {
        if(buttonMap.containsKey(command.getButton())) {
            buttonMap.get(command.getButton()).onTrue(command.getCommand());
        } else if(povMap.containsKey(command.getButton())) {
            povMap.get(command.getButton()).onTrue(command.getCommand());
        }
    }

    /**
     * Overridden method from ControlSchemeVisitor interface, provides a clean way to set commands to run on released.
     */
    public void visit(ControlSchemeOnReleasedCommand command) {
        if(buttonMap.containsKey(command.getButton())) {
            buttonMap.get(command.getButton()).onFalse(command.getCommand());
        } else if(povMap.containsKey(command.getButton())) {
            povMap.get(command.getButton()).onFalse(command.getCommand());
        }
    }


}