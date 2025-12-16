package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Sorter A135_B246 Forward Only", group = "Testing")
public class Sorter_Encoder extends LinearOpMode {

    private static final double TICKS_PER_REV = 765.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor sorter = hardwareMap.get(DcMotor.class, "sorter");

        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setTargetPosition(0);
        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int[] aSequence = {1, 3, 5};
        int[] bSequence = {2, 4, 6};

        int aIndex = 0;
        int bIndex = 0;

        boolean aPressed = false;
        boolean bPressed = false;

        waitForStart();

        while (opModeIsActive()) {

            // ---------- A BUTTON ----------
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
            telemetry.update();
        }

        sorter.setPower(0);
    }
}
