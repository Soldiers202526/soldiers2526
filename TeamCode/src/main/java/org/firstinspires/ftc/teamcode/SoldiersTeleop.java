package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

// internal imports


@TeleOp(name = "SoldiersTeleop", group = "Drive")
public class SoldiersTeleop extends LinearOpMode {

//    // Declare motors
//    private DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
//    private DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//    private DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
//    private DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
//
//    private DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
//
//    private DcMotor sorter = hardwareMap.get(DcMotor.class, "sorter");
//
//    private DcMotor leftShoot = hardwareMap.get(DcMotor.class, "leftShoot");
//    private DcMotor rightShoot = hardwareMap.get(DcMotor.class, "rightShoot");
//
//    private Servo bootKicker = hardwareMap.get(Servo.class, "bootkicker");
//
//
//    private Servo tiltAdjust = hardwareMap.get(Servo.class, "tiltAdjust");

    // Declare motors
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;

    private DcMotor intake = null;

    private DcMotor sorter = null;

    private DcMotor leftShoot = null;
    private DcMotor rightShoot = null;

    private Servo bootKicker = null;


    private Servo tiltAdjust = null;
    ;
    private double servoPosition = 1; // Start halfway

//    private static final double TICKS_PER_REV = 537.7;   // Example for GoBilda 5203
//    private static final int NUM_POSITIONS = 6;
//    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS;


    private static final double TICKS_PER_REV = 775.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS; // ~116.7 ticks per slot

    // A and B sequences
    private int[] aSequence = {1, 3, 5}; // positions for A
    private int[] bSequence = {2, 4, 6}; // positions for B

    private int aIndex = 0; // tracks current position in A sequence
    private int bIndex = 0; // tracks current position in B sequence

    private boolean aPressed = false;
    private boolean bPressed = false;

    // initial intake state
    private int intake_state = 0;


    private void doIntake() {
        if (intake_state == 0) {
            // action
            intake.setPower(0);

            // transition
            if (gamepad2.a) {
                intake_state = 1;
            } else if (gamepad2.b) {
                intake_state = 4;
            }
        } else if (intake_state == 1) {
            //transition
            if (!gamepad2.a) {
                intake_state = 2;
            }
        } else if (intake_state == 2) {
            // action
            intake.setPower(-0.5);

            // transition
            if (gamepad2.a) {
                intake_state = 3;
            } else if (gamepad2.b) {
                intake_state = 4;
            }
        } else if (intake_state == 3) {
            // transition
            if (!gamepad2.a) {
                intake_state = 0;
            }
        } else if (intake_state == 4) {
            if (!gamepad2.b) {
                intake_state = 5;
            }
        } else if (intake_state == 5) {
            intake.setPower(0.5);

            //transition
            if (gamepad2.b) {
                intake_state = 6;
            } else if (gamepad2.a) {
                intake_state = 1;
            }
        } else if (intake_state == 6) {
            if (!gamepad2.b) {
                intake_state = 0;
            }
        } else {
            telemetry.addData("Invalid Intake State", intake_state);
        }
    }

    private void doSorter() {
        // TO DO
        if (gamepad1.a && !aPressed) {
            aPressed = true;

            // pick next position in A sequence
            aIndex = (aIndex + 1) % aSequence.length;
            int position = aSequence[aIndex];

            // calculate encoder ticks for this position
            int targetTicks = (int) (TICKS_PER_POSITION * (position - 1)); // reverse direction
            sorter.setTargetPosition(targetTicks);
            sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sorter.setPower(0.15);
        }
        if (!gamepad1.a) aPressed = false;

        // --- Handle B button ---
        if (gamepad1.b && !bPressed) {
            bPressed = true;

            // pick next position in B sequence
            bIndex = (bIndex + 1) % bSequence.length;
            int position = bSequence[bIndex];

            // calculate encoder ticks for this position
            int targetTicks = (int) (TICKS_PER_POSITION * (position - 1)); // reverse direction
            sorter.setTargetPosition(targetTicks);
            sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sorter.setPower(0.15);
        }
        if (!gamepad1.b) bPressed = false;

        // telemetry
        telemetry.addData("Current Pos", sorter.getCurrentPosition());
        telemetry.addData("Target Pos", sorter.getTargetPosition());
        telemetry.addData("A index", aIndex);
        telemetry.addData("B index", bIndex);
        telemetry.update();


        sorter.setPower(0);
    }


    private void doDrive() {


        double y = gamepad1.left_stick_y; // Forward/Backward
        double x = -gamepad1.left_stick_x;  // Strafe Left/Right
        double rx = -gamepad1.right_stick_x; // Rotation


        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        // Normalize powers if any value is greater than 1
        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower),
                Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));


        // Calculate motor powers
        if (max > 1.0) {
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);


        telemetry.addData("Front Left", frontLeftPower);
        telemetry.addData("Back Left", backLeftPower);
        telemetry.addData("Front Right", frontRightPower);
        telemetry.addData("Back Right", backRightPower);

    }


    private void sortercode() {

        if (gamepad2.left_bumper || gamepad2.left_trigger > 0.5) {
            sorter.setPower(0.2);
        } else if (gamepad2.right_bumper || gamepad2.right_trigger > 0.5) {
            sorter.setPower(-0.2);
        } else {
            sorter.setPower(0);
        }

    }


    private void shootercode() {


        double shoot = gamepad2.right_stick_y;

        leftShoot.setPower(-shoot);
        rightShoot.setPower(-shoot);

//    int leftShootCurrentPosition = leftShoot.getCurrentPosition();
//    int rightShootCurrentPosition = rightShoot.getCurrentPosition();
//   // int leftDeltaTicks = leftShootCurrentPosition - leftShootLastPosition;
//   // int rightDeltaTicks = rightShootCurrentPosition - rightShootLastPosition;
//
//   // leftShootLastPosition = leftShootCurrentPosition;
//    rightShootLastPosition = rightShootCurrentPosition;
    }


    private void bootkicker() {

        if (gamepad2.x) {
            bootKicker.setPosition(0.75);
            sleep(500);
            bootKicker.setPosition(.98);
        }

    }

    private void doservo() {

        // How fast it moves per loop
        double SERVO_SPEED = 0.0003;
        servoPosition += -gamepad2.left_stick_y * SERVO_SPEED;
        servoPosition = Range.clip(servoPosition, 0.0, 1.0);
        tiltAdjust.setPosition(servoPosition);
        telemetry.addData("tiltAdjust", tiltAdjust.getDirection());
        telemetry.addData("Servo Position", servoPosition);
    }


    @Override
    public void runOpMode() {
        // Initialize the hardware variables. The names must match your configuration file
        // Declare motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        intake = hardwareMap.get(DcMotor.class, "intake");

        sorter = hardwareMap.get(DcMotor.class, "sorter");

        leftShoot = hardwareMap.get(DcMotor.class, "leftShoot");
        rightShoot = hardwareMap.get(DcMotor.class, "rightShoot");

        bootKicker = hardwareMap.get(Servo.class, "bootkicker");


        tiltAdjust = hardwareMap.get(Servo.class, "tiltAdjust");


        // Reverse the right side motors. Adjust if needed based on your robot’s setup
        //frontRight.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        leftShoot.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftShoot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

//    int leftShootLastPosition = leftShoot.getCurrentPosition();
//    int rightShootLastPosition = rightShoot.getCurrentPosition();

        bootKicker.setPosition(.98);
        tiltAdjust.setPosition(1);


//        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            // Get the gamepad inputs4
//            double stick = gamepad1.left_stick_y;


            doIntake();


            // TO DO
//        doSorter();


            doDrive();


            sortercode();


            shootercode();


            bootkicker();


            doservo();

            //TO DO merge shoot and bootkick code


//telemetry update


            //telemetry.addData("Joystick", gamepad2.left_stick_y);
//        telemetry.addData("leftShoot", leftDeltaTicks);
//        telemetry.addData("rightShoot", rightDeltaTicks);
            telemetry.update();
        }
    }
}