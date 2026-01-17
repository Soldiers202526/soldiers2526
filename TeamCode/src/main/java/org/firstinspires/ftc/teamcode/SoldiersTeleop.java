package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "SoldiersTeleop", group = "Drive")
public class SoldiersTeleop extends Soldiers_Shared {

    @Override
    public void runOpMode() {
        // Initialize the hardware variables. The names must match your configuration file
        // Declare motors

        // IMPORTANT: need to call this method before doing anything
        init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {


            //Drive Code

            double speed_fraction = 1.;
            if (gamepad1.left_trigger > 0.5) {
                speed_fraction *= 0.25;
            }


            if (gamepad1.right_trigger > 0.5) {
                speed_fraction *= 0.5;
            }

            doDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, speed_fraction);

            //Intake Code

            //doIntake(gamepad2.right_trigger > 0.3, gamepad1.left_trigger > 0.8 && gamepad2.right_trigger > 0.8 );

            //PPG
            if (gamepad2.yWasPressed()) {
                PPG();
            }
            //PGP
            else if (gamepad2.bWasPressed()) {
                PGP();
            }
            //GPP
            else if (gamepad2.xWasPressed()) {
                GPP();
            }
            //just shoot all 3 without using color sensor
            else if (gamepad2.aWasPressed()) {
                ALL();
            }


            autoIntake( gamepad2.dpadDownWasPressed(), gamepad2.rightBumperWasPressed(), gamepad2.leftBumperWasPressed());

            if (gamepad1.dpadDownWasPressed()) {
                intakePos();
            }




            telemetry.update();
        }


    }
}