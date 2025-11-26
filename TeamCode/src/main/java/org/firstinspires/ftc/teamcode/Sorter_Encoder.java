package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Sorter A135_B246 Fixed", group = "Testing")
public class Sorter_Encoder extends LinearOpMode {

    private static final double TICKS_PER_REV = 775.0;   // REV 25:1 HD Hex
    private static final int NUM_POSITIONS = 6;
    private static final double TICKS_PER_POSITION = TICKS_PER_REV / NUM_POSITIONS; // ~116.7 ticks per slot

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor sorter = hardwareMap.get(DcMotor.class, "sorter");

        sorter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorter.setTargetPosition(0);
        sorter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // A and B sequences
        int[] aSequence = {1, 3, 5}; // positions for A
        int[] bSequence = {2, 4, 6}; // positions for B

        int aIndex = 0; // tracks current position in A sequence
        int bIndex = 0; // tracks current position in B sequence

        boolean aPressed = false;
        boolean bPressed = false;

        waitForStart();

        while (opModeIsActive()) {

            // --- Handle A button ---
            if (gamepad1.a && !aPressed) {
                aPressed = true;

                // pick next position in A sequence
                aIndex = (aIndex + 1) % aSequence.length;
                int aPosition = aSequence[aIndex];




                // calculate encoder ticks for this position
                int targetTicks = (int)(TICKS_PER_POSITION * (aPosition - 1)); // reverse direction
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
                int bPosition = bSequence[bIndex];

                // calculate encoder ticks for this position
                int targetTicks = (int)(TICKS_PER_POSITION * (bPosition - 1)); // reverse direction
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
        }

        sorter.setPower(0);
    }
}
