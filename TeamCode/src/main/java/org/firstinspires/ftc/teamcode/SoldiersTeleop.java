package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// internal imports


@TeleOp(name = "SoldiersTeleop", group = "Drive")
public class SoldiersTeleop extends LinearOpMode {


    TestBenchColor bench = new TestBenchColor();
    Soldiers_Shared functions = new Soldiers_Shared(telemetry);




    @Override
    public void runOpMode() {
        // Initialize the hardware variables. The names must match your configuration file
        // Declare motors

        waitForStart();

        while (opModeIsActive()) {


            //Drive Code

            double speed_fraction = 1.;
            if (gamepad2.left_trigger > 0.5) {
                speed_fraction *= 0.25;
            }


            if (gamepad2.right_trigger > 0.5) {
               speed_fraction *= 0.5;
            }

            functions.doDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, speed_fraction);





            telemetry.update();
        }


    }
}