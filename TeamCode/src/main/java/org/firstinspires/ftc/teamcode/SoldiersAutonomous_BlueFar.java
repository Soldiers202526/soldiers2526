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
        boolean stateinit = false;

        waitForStart();

        PathChain Path1 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(60.000, 8.000),

                                new Pose(64.000, 74.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-45))

                .build();

        PathChain Path2 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(64, 74),

                                new Pose(64, 80)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(180))

                .build();

        PathChain Path3 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(64, 80),

                                new Pose(40, 80)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .setBrakingStart(5)
                .build();

        PathChain Path4 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(40, 80),

                                new Pose(36, 80)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .setBrakingStart(5)

                .build();

        PathChain Path5 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(36, 80),

                                new Pose(28, 80)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .setBrakingStart(5)

                .build();

        PathChain Path6 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(28, 80),

                                new Pose(64, 74)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-45))

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
                    autostate++;
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
                if(!stateinit) {
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
                }
                if (MotifID == 2) {
                    PGP();
                }
                if (MotifID == 3) {
                    GPP();

                    autostate++;
                }

            }

            telemetry.addData("Auto State", autostate );


        }
    }}
