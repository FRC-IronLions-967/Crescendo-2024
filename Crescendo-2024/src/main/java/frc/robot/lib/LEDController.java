package frc.robot.lib;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;

public class LEDController {

    private final int kRedHue = 0;
    private final int kBlueHue = 160;
    private final int kMinPulse = 105;
    private final int kMaxPulse = 255;
    private final int kCyclesPerFlash = 25;
    
    private AddressableLED strip;
    private AddressableLEDBuffer stripBuffer;

    private int allianceColorHue;
    private int disabledColorValue;
    private boolean disabledColorValueAscending;

    private int alternatingFlashCount;
    private Color alternatingColor1;
    private Color alternatingColor2;

    private static BooleanSupplier intakeSupplier;
    private static BooleanSupplier readySupplier;

    public LEDController(int pwmPort, int numLeds) {
        strip = new AddressableLED(pwmPort);
        stripBuffer = new AddressableLEDBuffer(numLeds);
        strip.setLength(numLeds);

        disabledColorValue = kMinPulse;
        allianceColorHue = kRedHue;
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            if(alliance.get() == DriverStation.Alliance.Blue); {
                allianceColorHue = kBlueHue;
            }

        }

        alternatingColor1 = Color.kGreen;
        alternatingColor2 = Color.kBlack;
        alternatingFlashCount = 1;
    }

    public static void ConfigureLedEvents(BooleanSupplier intakeSupplier, BooleanSupplier readySupplier) {
        LEDController.intakeSupplier = intakeSupplier;
        LEDController.readySupplier = readySupplier;
    }

    public void disabledPeriodic() {
        //Is it getting brighter or dimmer
        if (disabledColorValue >= kMaxPulse){
            disabledColorValueAscending = false;
        } else if (disabledColorValue <= kMinPulse) {
            disabledColorValueAscending = true;
        }
        //Adjust color value
        if (disabledColorValueAscending) {
            disabledColorValue += 1;
        } else {
            disabledColorValue -= 1;
        }
        //Set color on all LEDs
        for (int i = 0; i < stripBuffer.getLength(); i++){
            stripBuffer.setHSV(i, allianceColorHue, 255, disabledColorValue);
        }
    }

    public void enabledPeriodic() {
        boolean intakeIn = intakeSupplier.getAsBoolean();
        boolean scorerIn = readySupplier.getAsBoolean();
        if(intakeIn) {
            //Set all to Green
            for (int i = 0; i < stripBuffer.getLength(); i++){
                stripBuffer.setLED(i, Color.kGreen);
            }
        } else if (scorerIn) {
            
            //Swap colors every half second
            if (alternatingFlashCount >= kCyclesPerFlash ) {
                alternatingFlashCount = 1;
                Color swapColor = alternatingColor1;
                alternatingColor1 = alternatingColor2;
                alternatingColor2 = swapColor;
            } else { alternatingFlashCount++; }

            //Set to alternatingly flash Green
            for (int i = 0; i < stripBuffer.getLength(); i++){
                if ( i % 2 == 0) {
                    stripBuffer.setLED(i, alternatingColor1);
                } else {
                    stripBuffer.setLED(i, alternatingColor2);
                }
                
            }
        } else {
            //Set all to Red
            for (int i = 0; i < stripBuffer.getLength(); i++){
                stripBuffer.setLED(i, Color.kRed);
            }
        }
    }

    private Color halfBrightness(Color color) {
        return new Color(color.red/2, color.green/2, color.blue/2);
    }

}
