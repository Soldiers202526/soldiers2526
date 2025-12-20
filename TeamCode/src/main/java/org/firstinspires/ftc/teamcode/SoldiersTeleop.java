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

// internal imports


@TeleOp(name = "SoldiersTeleop", group = "Drive")
public class SoldiersTeleop extends LinearOpMode {


    TestBenchColor bench = new TestBenchColor();

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

    // Declare motors
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;

    private DcMotor intake = null;

    private DcMotor sorter = null;

//    private DcMotor sorter = null;

    private DcMotorEx leftShoot = null;
    private DcMotorEx rightShoot = null;

    private Servo bootKicker = null;

    private Servo tiltAdjust = null;
    //private double servoPosition = 1; // Start halfway

//    private static final double TICKS_PER_REV = 537.7;   // Example for GoBilda 5203
//    private static final int NUM_POSITIONS = 6;
//    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS;


    private static final double TICKS_PER_REV = 765.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS; // ~116.7 ticks per slot



    // A and B sequences
    private final int[] aSequence = {1, 3, 5}; // positions for A
    private final int[] bSequence = {2, 4, 6}; // positions for B

    private int aIndex = 0; // tracks current position in A sequence
    private int bIndex = 0; // tracks current position in B sequence

    boolean aPressed = false;
    boolean bPressed = false;

    // initial intake state
    private int intake_state = 0;

    private int tilt_state = 0;

    private void doTilt() {
        if (tilt_state == 0) {
            tiltAdjust.setPosition(0);

            if (gamepad2.dpad_up) {
                tilt_state = 1;
            }
        } else if (tilt_state == 1) {
            tiltAdjust.setPosition(1.0);

            if (gamepad2.dpad_down) {
                tilt_state = 0;
            }
        } else {
            telemetry.addLine("ERROR: unknown tilt adjust state");
        }

        telemetry.addData("tiltAdjust = ", tiltAdjust.getPosition());
        telemetry.addData("tilt_state = ", tilt_state);

    }

    private void doIntake() {
        if (intake_state == 0) {
            // action
            intake.setPower(0);

            // transition
            if (gamepad2.b && gamepad1.b) {
                intake_state = 1;
            } else if (gamepad2.a) {
                intake_state = 4;
            }
        } else if (intake_state == 1) {
            //transition
            if (!gamepad2.b && !gamepad1.b) {
                intake_state = 2;
            }
        } else if (intake_state == 2) {
            // action
            intake.setPower(-1);

            // transition
            if (gamepad2.b && gamepad1.b) {
                intake_state = 3;
            } else if (gamepad2.a) {
                intake_state = 4;
            }
        } else if (intake_state == 3) {
            // transition
            if (!gamepad2.b && !gamepad1.b) {
                intake_state = 0;
            }
        } else if (intake_state == 4) {
            if (!gamepad2.a) {
                intake_state = 5;
            }
        } else if (intake_state == 5) {
            intake.setPower(1);

            //transition
            if (gamepad2.a) {
                intake_state = 6;
            } else if (gamepad2.b && gamepad1.b) {
                intake_state = 1;
            }
        } else if (intake_state == 6) {
            if (!gamepad2.a) {
                intake_state = 0;
            }
        } else {
            telemetry.addData("Invalid Intake State", intake_state);
        }
    }



     private void autoSort() {
         //aPressed = true;

         int position = aSequence[aIndex];
         aIndex = (aIndex + 1) % aSequence.length;

         int targetTicks = (int)(TICKS_PER_POSITION * (position - 1));

         // FORCE target forward only
         while (targetTicks <= sorter.getCurrentPosition()) {
             targetTicks += (int) TICKS_PER_REV;
         }

         sorter.setTargetPosition(targetTicks);
         sorter.setPower(1);
         sleep(1000);
     }
    private void doSorter() {
        // TO DO
        if (gamepad1.a && !aPressed) {
            aPressed = true;

            int position = aSequence[aIndex];
            aIndex = (aIndex + 1) % aSequence.length;

            int targetTicks = (int)(TICKS_PER_POSITION * (position - 1));

            // FORCE target forward only
            while (targetTicks <= sorter.getCurrentPosition()) {
                targetTicks += TICKS_PER_REV;
            }

            sorter.setTargetPosition(targetTicks);
            sorter.setPower(1);
        }
        if (!gamepad1.a) aPressed = false;



        // ---------- B BUTTON ----------
        if (gamepad1.b && !bPressed) {
            bPressed = true;

            int position = bSequence[bIndex];
            bIndex = (bIndex + 1) % bSequence.length;

            int targetTicks = (int)(TICKS_PER_POSITION * (position - 1));

            // FORCE target forward only
            while (targetTicks <= sorter.getCurrentPosition()) {
                targetTicks += TICKS_PER_REV;
            }

            sorter.setTargetPosition(targetTicks);
            sorter.setPower(1);
        }
        if (!gamepad1.b) bPressed = false;

        // Stop motor when done
        if (!sorter.isBusy()) {
            sorter.setPower(0);
        }

        telemetry.addData("Current", sorter.getCurrentPosition());
        telemetry.addData("Target", sorter.getTargetPosition());
   }



   private void ppg (){

       autoshoot();
        purple_shoot();
        sleep(200);
        purple_shoot();
        sleep(200);
        green_shoot();
        sleep(200);
        leftShoot.setPower(0);
        rightShoot.setPower(0);



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

        if (gamepad1.left_trigger > 0.5) {
            frontLeftPower *= 0.25;
            backLeftPower *= 0.25;
            frontRightPower *= 0.25;
            backRightPower *= 0.25;
        }

        if (gamepad1.right_trigger > 0.5) {
            frontLeftPower *= 0.5;
            backLeftPower *= 0.5;
            frontRightPower *= 0.5;
            backRightPower *= 0.5;
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



private void autoshoot() {
//


        leftShoot.setPower(0.38);
        rightShoot.setPower(0.38);

        sleep(3000);


    }

    private void launch() {
        bootKicker.setPosition(0.75);
        sleep(500);
        bootKicker.setPosition(.98);

        sleep(200);
    }

    private void shootercode() {
//
//
//        double shoot = gamepad2.right_stick_y;
        if (gamepad2.yWasPressed()) {
               autoshoot();

        }















//        leftShoot.setPower(-shoot);
//        rightShoot.setPower(-shoot);

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

    public void green_shoot()
    {



        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);
        if (color == TestBenchColor.DetectedColor.GREEN)
        {
            launch();

        }
        else {
            autoSort();

            color = bench.getDetectedColor(telemetry);
            if (color == TestBenchColor.DetectedColor.GREEN)
            {
                launch();
            }
            else {
                autoSort();
                color = bench.getDetectedColor(telemetry);
                if (color == TestBenchColor.DetectedColor.GREEN)
                {
                    launch();
                }

                else {
//                    leftShoot.setPower(0);
//                    rightShoot.setPower(0);
                }

            }
        }

        // calls
    }

    public void purple_shoot() {


        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);

        if (color == TestBenchColor.DetectedColor.PURPLE) {
            launch();

        } else {
            autoSort();

            color = bench.getDetectedColor(telemetry);
            if (color == TestBenchColor.DetectedColor.PURPLE) {
                launch();
            } else {
                autoSort();
                color = bench.getDetectedColor(telemetry);
                if (color == TestBenchColor.DetectedColor.PURPLE) {
                    launch();
                } else {
//                    leftShoot.setPower(0);
//                    rightShoot.setPower(0);
                }

            }
        }
    }



    @Override



    public void runOpMode() {
        // Initialize the hardware variables. The names must match your configuration file
        // Declare motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            bench.init(hardwareMap);

        intake = hardwareMap.get(DcMotor.class, "intake");

        sorter = hardwareMap.get(DcMotor.class, "sorter");
        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setTargetPosition(0);
        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftShoot = hardwareMap.get(DcMotorEx.class, "leftShoot");
        rightShoot = hardwareMap.get(DcMotorEx.class, "rightShoot");
        leftShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        bootKicker = hardwareMap.get(Servo.class, "bootkicker");

        tiltAdjust = hardwareMap.get(Servo.class, "tiltAdjust");

        // TODO: how to do this?
//        tiltAdjust.setPwmRange(new ServoPwmRange(600, 2400));




        // Reverse the right side motors. Adjust if needed based on your robot’s setup
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        leftShoot.setDirection(DcMotor.Direction.REVERSE);
//        rightShoot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftShoot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

//    int leftShootLastPosition = leftShoot.getCurrentPosition();
//    int rightShootLastPosition = rightShoot.getCurrentPosition();

        bootKicker.setPosition(.98);

        tiltAdjust.setPosition(1.0);


//        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            // Get the gamepad inputs4
//            double stick = gamepad1.left_stick_y;


            doIntake();


            // TODO
       // doSorter();

            doDrive();

            bench.getDetectedColor(telemetry);

            //sortercode();


            shootercode();


            if (gamepad1.aWasPressed()) {
                ppg();
            }

//            if (gamepad1.bWasPressed()) {
//                purple_shoot();
//            }

            bootkicker();

            doTilt();



            //doservo();

            //TO DO merge shoot and bootkick code


//telemetry update


            //telemetry.addData("Joystick", gamepad2.left_stick_y);
//        telemetry.addData("leftShoot", leftDeltaTicks);
//        telemetry.addData("rightShoot", rightDeltaTicks);
            telemetry.update();
        }
        sorter.setPower(0);








    }
}