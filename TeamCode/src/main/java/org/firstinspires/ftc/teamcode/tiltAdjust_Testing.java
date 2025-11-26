package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Tilt Adjust Servo Control")
public class tiltAdjust_Testing extends LinearOpMode {

    private Servo tiltAdjust;

    @Override
    public void runOpMode() {

        tiltAdjust = hardwareMap.get(Servo.class, "tiltAdjust");

        waitForStart();

        while (opModeIsActive()) {

            // A button → move to 0
            if (gamepad1.a) {
                tiltAdjust.setPosition(0.0);
            }

            // B button → move to 1
            if (gamepad1.b) {
                tiltAdjust.setPosition(1.0);
            }

            telemetry.addData("TiltAdjust Position", tiltAdjust.getPosition());
            telemetry.update();
        }
    }
}
