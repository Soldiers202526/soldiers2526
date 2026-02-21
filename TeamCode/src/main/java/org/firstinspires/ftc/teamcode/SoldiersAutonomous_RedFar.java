package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "SoldiersAutonomous_RedFar ", group = "Drive")
public class SoldiersAutonomous_RedFar extends Soldiers_Shared {


    @Override
    public void runOpMode() {

        init(hardwareMap);

        follower.setStartingPose(new Pose(84, 8, Math.toRadians(-190)));

        int autostate = 0;
        boolean stateinit = false;
        long timer = System.currentTimeMillis() / 1000;


        waitForStart();

        PathChain Path1 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(84, 8.000),

                                new Pose(84, 90.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-90))


                .build();

        PathChain Path10 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(84, 90.000),

                                new Pose(80,78)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-135))

                .build();

        PathChain Path2 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(80, 78),

                                new Pose(80, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-135), Math.toRadians(0))

                .build();

        PathChain Path3 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(80, 84),

                                new Pose(104, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setBrakingStart(5)
                .build();

        PathChain Path4 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(104, 84),

                                new Pose(108, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setBrakingStart(5)

                .build();

        PathChain Path5 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(108, 84),

                                new Pose(116, 84)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setBrakingStart(5)

                .build();

        PathChain Path6 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(116, 84),

                                new Pose(80, 74)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-150))

                .build();


        autostate = 1;


        follower.followPath(Path1);
//        telemetry.addData("following path chain",  follower.getFollowingPathChain());
//            telemetry.addData("Motif ID", MotifID);

        while (opModeIsActive()) {

            huskylens();
            telemetry.update();
            follower.update();

            if (System.currentTimeMillis() / 1000 - timer > 26) {
                stateinit = false;
                autostate=0;
            }

            if (autostate== 0) {
                if (!stateinit) {

                    PathChain Park = follower.pathBuilder().addPath(

                                    new BezierLine(
                                            follower.getPose(),

                                            new Pose(39, 33)
                                    )
                            ).setLinearHeadingInterpolation(Math.toRadians(follower.getHeading()), Math.toRadians(-90))

                            .build();

                    follower.followPath(Park);
                    stateinit = true;
                }
            }

            if (autostate == 1) {
//                follower.followPath(Path1);

                telemetry.addData("following path chain", follower.getFollowingPathChain());
                telemetry.addData("Motif ID", MotifID);

                if (!follower.getFollowingPathChain()) {
                    sleep(500);
                    autostate = 10;
                }
            }

            if (autostate == 10) {
                if (!stateinit) {
                    follower.followPath(Path10);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {

                    stateinit = false;
                    autostate = 2;
                    sleep(500);
                }
            }

            if (autostate == 2) {
                if (MotifID == 1) {
                    PPG();
                } else if (MotifID == 2) {
                    PGP();
                } else if (MotifID == 3) {
                    GPP();
                } else {
                    ALL();
                    telemetry.addLine("Did Not Read Obelisk");

                }


                intakePos();
                sleep(500);

                autostate++;
                autoIntake(false, true, false);
            }

            if (autostate == 3) {

                // change states
                if (!stateinit) {
                    // intakePos();
                    follower.followPath(Path2);
                    autoIntake(false, true, false);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {
                    sleep(500);
                    intakePos();
                    sleep(500);
                    stateinit = false;
                    autostate++;
                }
            }

            if (autostate == 4) {
                // intakePos();
                // doDrive(0,0,0,0.25);
                autoIntake(false, true, false);
                if (!stateinit) {
                    //intakePos();
                    follower.followPath(Path3);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {
                    // sleep(1000);
                    sleep(500);
                    intakePos();
                    sleep(500);
                    stateinit = false;
                    autostate++;
                }
            }

            if (autostate == 5) {
                autoIntake(false, true, false);
                // doDrive(0,0,0,0.25);
                if (!stateinit) {
                    //  intakePos();
                    follower.followPath(Path4);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {
                    sleep(500);
                    intakePos();
                    sleep(500);
                    stateinit = false;
                    autostate++;
                }
            }

            if (autostate == 6) {
                //  doDrive(0, 0, 0, .25);
                autoIntake(false, true, false);
                if (!stateinit) {
                    follower.followPath(Path5);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {
                    //intakePos();
                    sleep(1000);
                    stateinit = false;
                    autostate++;
                }
            }

            if (autostate == 7) {
                if (!stateinit) {
                    sleep(1000);
                    follower.followPath(Path6);
                    stateinit = true;
                }
                if (!follower.getFollowingPathChain()) {
                    stateinit = false;
                    autostate++;
                }
            }

            if (autostate == 8) {
                if (MotifID == 1) {
                    PPG();
                } else if (MotifID == 2) {
                    PGP();
                } else if (MotifID == 3) {
                    GPP();


                } else {
                    ALL();
                    telemetry.addLine("Did Not Read Obelisk");

                }
                autostate++;
            }

            telemetry.addData("Auto State", autostate);


        }
    }
}