package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "SoldiersAutonomous", group = "Drive")
public class SoldiersAutonomous_BlueFar extends Soldiers_Shared {


    @Override
    public void runOpMode() {

        init(hardwareMap);

        follower.setStartingPose(new Pose(60, 8, Math.toRadians(-90)));

        int autostate = 0;

        waitForStart();

        PathChain Path1 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(60.000, 8.000),

                                new Pose(64.000, 78.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-45))

                .build();

        PathChain Path2 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(64, 78),

                                new Pose(38, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(180))

                .build();

        PathChain Path3 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(38, 84),

                                new Pose(32, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path4 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(32, 84),

                                new Pose(26, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path5 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(26, 84),

                                new Pose(60, 82)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-42))

                .build();


            autostate = 1;


        follower.followPath(Path1);
//        telemetry.addData("following path chain",  follower.getFollowingPathChain());
//            telemetry.addData("Motif ID", MotifID);

        while (opModeIsActive()) {

            huskylens();
            telemetry.update();
            follower.update();

            if (autostate == 1) {
//                follower.followPath(Path1);

                telemetry.addData("following path chain", follower.getFollowingPathChain());
                telemetry.addData("Motif ID", MotifID);

                if (!follower.getFollowingPathChain()) {
                    autostate = 2;
                }
            }

            if (autostate == 2) {
                if (MotifID == 1) {
                    PPG();
                }
                if (MotifID == 2) {
                    PGP();
                }
                if (MotifID == 3) {
                    GPP();
                }

                autostate = 3;

            }

            if (autostate == 3) {
                autoIntake(false, true, false);
                intakePos();

                // change states
                follower.followPath(Path2);
                autostate = 4;
            }



            if (autostate == 4) {
                intakePos();
                follower.followPath(Path3);
                autostate = 5;
            }

            if (autostate == 5) {
                intakePos();

                follower.followPath(Path4);
                autostate = 6;
            }

            if (autostate == 6) {
                if (!follower.getFollowingPathChain()) {
                    sleep(500);
                }
                follower.followPath(Path5);
                autostate = 7;
            }

            if (autostate == 7) {
                if (MotifID == 1 && !follower.getFollowingPathChain()) {
                    PPG();
                }
                if (MotifID == 2 && !follower.getFollowingPathChain()) {
                    PGP();
                }
                if (MotifID == 3 && !follower.getFollowingPathChain()) {
                    GPP();

                    autostate =12;
                }

            }



        }
    }}
