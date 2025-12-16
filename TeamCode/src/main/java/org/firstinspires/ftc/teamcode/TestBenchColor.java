package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestBenchColor {
    NormalizedColorSensor colorSensor;

    public enum DetectedColor {
        RED,
        BLUE,
        YELLOW,
        PURPLE,
        GREEN,
        UNKNOWN
    }

 public void init(HardwareMap hwMap) {
     colorSensor = hwMap.get(NormalizedColorSensor.class, "sensor-color-distance");
 }

 public DetectedColor getDetectedColor(Telemetry telemetry) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;


        telemetry.addData("red", normRed);
        telemetry.addData("green", normGreen);
        telemetry.addData("blue", normBlue);

     //TODO add rgb values for purple and green

        /*
        red, green, blue

        purple = .0059, 0.0077, 0.01

        green = 0.0036, 0.0115, 0.0086

        nothing = 0.0084, 0.0138, 0.0107
        */

        if (normRed <.008 && normGreen < 0.01 && normBlue > 0.008) {
            telemetry.addLine("Purple");
            return DetectedColor.PURPLE;
        }

        else if (normRed <.005 && normGreen < 0.015 && normBlue < 0.01) {
            telemetry.addLine("Green");
            return DetectedColor.GREEN;
     }
        else {
            telemetry.addLine("UNKOWN");
            return DetectedColor.UNKNOWN;

        }

        //telemetry.addData("DetectedColor", DetectedColor);

    }


}