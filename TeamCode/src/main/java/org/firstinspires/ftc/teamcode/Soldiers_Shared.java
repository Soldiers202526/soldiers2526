package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Soldiers_Shared {

    /* ===================== FTC HANDLES ===================== */

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;

    /* ===================== HARDWARE ===================== */

    private DcMotor frontLeft, backLeft, frontRight, backRight;
    private DcMotor intake, sorter;
    private DcMotorEx leftShoot, rightShoot;
    private Servo bootKicker, tiltAdjust;

    private final TestBenchColor bench = new TestBenchColor();

    /* ===================== SORTER CONSTANTS ===================== */

    private static final double TICKS_PER_REV = 765.0;
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS;

    private final int[] aSequence = {1, 3, 5};
    private final int[] bSequence = {2, 4, 6};
    private int aIndex = 0;
    private int bIndex = 0;

    /* ===================== STATE ===================== */

    private int intakeState = 0;

    private int launchState = 0;
    private final ElapsedTime launchTimer = new ElapsedTime();

    private boolean sorting = false;

    private boolean greenShootActive = false;
    private boolean purpleShootActive = false;

    /* ===================== CONSTRUCTOR ===================== */

    public Soldiers_Shared(HardwareMap hw, Telemetry tel) {
        this.hardwareMap = hw;
        this.telemetry = tel;
    }

    /* ===================== INIT ===================== */

    public void init() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotor.class, "intake");

        sorter = hardwareMap.get(DcMotor.class, "sorter");
        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setTargetPosition(0);
        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftShoot  = hardwareMap.get(DcMotorEx.class, "leftShoot");
        rightShoot = hardwareMap.get(DcMotorEx.class, "rightShoot");

        leftShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bootKicker = hardwareMap.get(Servo.class, "bootkicker");
        bootKicker.setPosition(0.98);

        tiltAdjust = hardwareMap.get(Servo.class, "tiltAdjust");
        tiltAdjust.setPosition(1.0);

        bench.init(hardwareMap);

        telemetry.addData("SoldiersShared", "Initialized");
    }

    /* ===================== UPDATE ===================== */

    public void update() {
        updateLaunch();
        updateGreenShoot();
        updatePurpleShoot();
    }

    /* ===================== DRIVE ===================== */

    public void drive(double y, double x, double rx, double speed) {

        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        double max = Math.max(Math.abs(fl),
                Math.max(Math.abs(bl), Math.max(Math.abs(fr), Math.abs(br))));

        if (max > 1.0) {
            fl /= max; bl /= max; fr /= max; br /= max;
        }

        frontLeft.setPower(fl * speed);
        backLeft.setPower(bl * speed);
        frontRight.setPower(fr * speed);
        backRight.setPower(br * speed);
    }

    /* ===================== SHOOTER ===================== */

    public void prepareLaunch() {
        leftShoot.setPower(0.35);
        rightShoot.setPower(0.35);
    }

    public void startLaunch() {
        launchState = 1;
        launchTimer.reset();
    }

    private void updateLaunch() {
        if (launchState == 1) {
            bootKicker.setPosition(0.75);
            launchTimer.reset();
            launchState = 2;
        }
        else if (launchState == 2 && launchTimer.milliseconds() > 500) {
            bootKicker.setPosition(0.98);
            launchTimer.reset();
            launchState = 3;
        }
        else if (launchState == 3 && launchTimer.milliseconds() > 200) {
            launchState = 0;
        }
    }

    /* ===================== SORTER ===================== */

    private void startAutoSort() {
        int position = aSequence[aIndex];
        aIndex = (aIndex + 1) % aSequence.length;

        int targetTicks = (int)(TICKS_PER_POSITION * (position - 1));
        while (targetTicks <= sorter.getCurrentPosition()) {
            targetTicks += TICKS_PER_REV;
        }

        sorter.setTargetPosition(targetTicks);
        sorter.setPower(0.6);
        sorting = true;
    }

    private boolean isSortingDone() {
        return !sorter.isBusy();
    }

    /* ===================== COLOR SHOOT ===================== */

    public void startGreenShoot() {
        greenShootActive = true;
    }

    private void updateGreenShoot() {
        if (!greenShootActive) return;

        if (launchState != 0 || sorting) return;

        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);

        if (color == TestBenchColor.DetectedColor.GREEN) {
            startLaunch();
            greenShootActive = false;
        } else {
            startAutoSort();
            sorting = false;
        }
    }

    public void startPurpleShoot() {
        purpleShootActive = true;
    }

    private void updatePurpleShoot() {
        if (!purpleShootActive) return;

        if (launchState != 0 || sorting) return;

        TestBenchColor.DetectedColor color = bench.getDetectedColor(telemetry);

        if (color == TestBenchColor.DetectedColor.PURPLE) {
            startLaunch();
            purpleShootActive = false;
        } else {
            startAutoSort();
            sorting = false;
        }
    }
}
