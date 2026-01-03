
//Import Libraries

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;


public abstract class Soldiers_Shared extends LinearOpMode {


    TestBenchColor bench = new TestBenchColor();

    //Declare Motors
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private DcMotor intake = null;
    private DcMotor sorter = null;
    private DcMotorEx leftShoot = null;
    private DcMotorEx rightShoot = null;

    //Declare Servos
    private Servo bootKicker = null;
    private Servo tiltAdjust = null;

    //Sorter

    private static final double TICKS_PER_REV = 765.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS; // ~116.7 ticks per slot


    //Sorter Stuff
    private final int[] aSequence = {1, 3, 5}; // positions for A
    private final int[] bSequence = {2, 4, 6}; // positions for B
    private int aIndex = 0; // tracks current position in A sequence
    private int bIndex = 0; // tracks current position in B sequence
    boolean aPressed = false;
    boolean bPressed = false;

    //Initial Intake State
    private int intake_state = 0;

    //Initial Tilt State
    private int tilt_state = 0;

    //Constructor

    public void init(HardwareMap hw_map) {
        frontLeft = hw_map.get(DcMotor.class, "frontLeft");
        backLeft = hw_map.get(DcMotor.class, "backLeft");
        frontRight = hw_map.get(DcMotor.class, "frontRight");
        backRight = hw_map.get(DcMotor.class, "backRight");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);

        bench.init(hw_map);

        intake = hw_map.get(DcMotor.class, "intake");

        sorter = hw_map.get(DcMotor.class, "sorter");
        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setTargetPosition(0);
        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftShoot = hw_map.get(DcMotorEx.class, "leftShoot");
        rightShoot = hw_map.get(DcMotorEx.class, "rightShoot");
        leftShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        bootKicker = hw_map.get(Servo.class, "bootkicker");
        bootKicker.setPosition(.98);

        tiltAdjust = hw_map.get(Servo.class, "tiltAdjust");
        tiltAdjust.setPosition(1.0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }


    //Drive Code
    public void doDrive(double forw_back, double left_right, double spin, double speed_fraction) {


        double y = forw_back;   //Left Stick Y
        double x = -left_right;  // Left Stick X
        double rx = -spin; // Right Stick X


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


        frontLeftPower *= speed_fraction;
        backLeftPower *= speed_fraction;
        frontRightPower *= speed_fraction;
        backRightPower *= speed_fraction;


        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);


        telemetry.addData("Front Left", frontLeftPower);
        telemetry.addData("Back Left", backLeftPower);
        telemetry.addData("Front Right", frontRightPower);
        telemetry.addData("Back Right", backRightPower);

    }

    //Intake State Machine

     /* private void doIntake() {
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
*/


    //Intake Code
    public void doIntake(boolean intake_collect, boolean intake_stop, boolean intake_eject) {

        if (intake_stop) {
            intake.setPower(0);
        } else if (intake_eject) {
            intake.setPower(-1);
        } else if (intake_collect) {
            intake.setPower(1);
        }

    }

    //Launcher Code
    public void preparelaunch() {
        leftShoot.setPower(0.35);
        rightShoot.setPower(0.35);
    }

    //BootKicker Code
    public void launch() {
        bootKicker.setPosition(0.75);
        sleep(500);
        bootKicker.setPosition(.98);
        sleep(200);
    }

    //Sorter Code

    public void autoSort() {
        //aPressed = true;

        int position = aSequence[aIndex];
        aIndex = (aIndex + 1) % aSequence.length;

        int targetTicks = (int) (TICKS_PER_POSITION * (position - 1));

        // FORCE target forward only
        while (targetTicks <= sorter.getCurrentPosition()) {
            targetTicks += (int) TICKS_PER_REV;
        }

        sorter.setTargetPosition(targetTicks);
        sorter.setPower(0.6);
        sleep(1500);
    }

    //Green Shoot

    public void green_shoot() {

        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);
        if (color == TestBenchColor.DetectedColor.GREEN) {
            launch();

        } else {
            autoSort();
            green_shoot();

//            color = bench.getDetectedColor(telemetry);
//            if (color == TestBenchColor.DetectedColor.GREEN) {
//                launch();
//            } else {
//                autoSort();
//                color = bench.getDetectedColor(telemetry);
//                if (color == TestBenchColor.DetectedColor.GREEN) {
//                    launch();
//                }
//
//            }
        }

        // calls
    }


    //Purple Shoot

    public void purple_shoot() {


        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);

        if (color == TestBenchColor.DetectedColor.PURPLE) {
            launch();

        } else {
            autoSort();
            purple_shoot();


//            color = bench.getDetectedColor(telemetry);
//            if (color == TestBenchColor.DetectedColor.PURPLE) {
//                launch();
//            } else {
//                autoSort();
//                color = bench.getDetectedColor(telemetry);
//                if (color == TestBenchColor.DetectedColor.PURPLE) {
//                    launch();
//                }
//
//            }
        }
    }

    //Purple-Purple-Green Shoot
    public void PPG() {

        preparelaunch();
        sleep(1000);
        purple_shoot();
        sleep(200);
        purple_shoot();
        sleep(200);
        green_shoot();
        sleep(200);
        leftShoot.setPower(0);
        rightShoot.setPower(0);
    }

    public void PGP() {
        sleep(2500);
        preparelaunch();
        purple_shoot();
        sleep(200);
        green_shoot();
        sleep(200);
        purple_shoot();
        sleep(200);
        leftShoot.setPower(0);
        rightShoot.setPower(0);
    }

    public void GPP() {
        sleep(2500);
        preparelaunch();
        green_shoot();
        sleep(200);
        purple_shoot();
        sleep(200);
        purple_shoot();
        sleep(200);
        leftShoot.setPower(0);
        rightShoot.setPower(0);
    }

}
