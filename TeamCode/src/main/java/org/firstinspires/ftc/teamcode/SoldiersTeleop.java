package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "SoldiersTeleop", group = "Drive")
public class SoldiersTeleop extends LinearOpMode {

    // Declare motors
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;

    private DcMotor intake = null;
    private DcMotor sorter = null;

    private DcMotor leftShoot = null;
    private DcMotor rightShoot = null;


    @Override
    public void runOpMode() {
        // Initialize the hardware variables. The names must match your configuration file
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        sorter = hardwareMap.get(DcMotor.class, "sorter");
        leftShoot = hardwareMap.get(DcMotor.class, "leftShoot");
        rightShoot = hardwareMap.get(DcMotor.class, "rightShoot");


        // Reverse the right side motors. Adjust if needed based on your robot’s setup
        //frontRight.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // init intake state machine
        int intake_state = 0;
        long intake_time = System.currentTimeMillis();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            // Get the gamepad inputs4
            double y = gamepad1.left_stick_y; // Forward/Backward
            double x = -gamepad1.left_stick_x;  // Strafe Left/Right
            double rx = -gamepad1.right_stick_x; // Rotation

            // Calculate motor powers
            double frontLeftPower = y + x + rx;
            double backLeftPower = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower = y + x - rx;

            // Normalize powers if any value is greater than 1
            double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower),
                    Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
            if (max > 1.0) {
                frontLeftPower /= max;
                backLeftPower /= max;
                frontRightPower /= max;
                backRightPower /= max;
            }

            // Set the motor powers
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // Telemetry for debugging
            if (intake_state == 0) {
                // action
                intake.setPower(0);

                // transition
                if (gamepad2.a) {
                    intake_state = 1;
                    intake_time = System.currentTimeMillis();
                }

                telemetry.addData("intake_state = ", intake_state);

            } else if (intake_state == 1) {
                if (System.currentTimeMillis() - intake_time > 500) {
                    intake_state = 2;
                }

                telemetry.addData("intake_state = ", intake_state);
            } else if (intake_state == 2) {
                // action
                intake.setPower(-0.1);

                if (gamepad2.a) {
                    intake_state = 3;
                    intake_time = System.currentTimeMillis();
                }

                telemetry.addData("intake_state = ", intake_state);
            } else if (intake_state == 3) {
                if (System.currentTimeMillis() - intake_time > 200) {
                    intake_state = 0;
                }

                telemetry.addData("intake_state = ", intake_state);
            } else {
                telemetry.addData("Invalid Intake State", intake_state);
            }


            telemetry.addData("Front Left", frontLeftPower);
            telemetry.addData("Back Left", backLeftPower);
            telemetry.addData("Front Right", frontRightPower);
            telemetry.addData("Back Right", backRightPower);
            telemetry.update();
        }
    }
}