package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Sorter A135_B246 Fixed", group = "Testing")
public class Sorter_Encoder extends LinearOpMode {

    private static final double TICKS_PER_REV = 775.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor sorter = hardwareMap.get(DcMotor.class, "sorter");

        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // sequences
        int[] aSequence = {1, 3, 5};
        int[] bSequence = {2, 4, 6};

        int aIndex = -1; // start BEFORE first so first press goes to index 0
        int bIndex = -1;

        boolean leftBumperPressed = false;
        boolean rightBumperPressed = false;

        waitForStart();

        while (opModeIsActive()) {

            // ----- A Button: 1 → 3 → 5 -----
            if (gamepad1.left_bumper && !leftBumperPressed) {
                leftBumperPressed = true;

                aIndex = (aIndex + 1) % aSequence.length;
                int slot = aSequence[aIndex];

                int targetTicks = (int)(TICKS_PER_POSITION * (slot - 1));

                sorter.setTargetPosition(targetTicks);
                sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sorter.setPower(0.25);
            }
            if (!gamepad1.left_bumper) leftBumperPressed = false;

            // ----- B Button: 2 → 4 → 6 -----
            if (gamepad1.right_bumper && !rightBumperPressed) {
                rightBumperPressed = true;

                bIndex = (bIndex + 1) % bSequence.length;
                int slot = bSequence[bIndex];

                int targetTicks = (int)(TICKS_PER_POSITION * (slot - 1));

                sorter.setTargetPosition(targetTicks);
                sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sorter.setPower(0.25);
            }
            if (!gamepad1.b) rightBumperPressed = false;

            // When done moving, switch back safely
            if (!sorter.isBusy()) {
                sorter.setPower(0);
                sorter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            // telemetry
            telemetry.addData("Current Pos", sorter.getCurrentPosition());
            telemetry.addData("Target Pos", sorter.getTargetPosition());
            telemetry.addData("A index", aIndex);
            telemetry.addData("B index", bIndex);
            telemetry.update();
        }

        sorter.setPower(0);
    }
}
