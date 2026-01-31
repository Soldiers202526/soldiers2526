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

        waitForStart();

        PathChain Path1 = follower.pathBuilder().addPath(

                        new BezierLine(
                                new Pose(60.000, 8.000),

                                new Pose(60.000, 82.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-45))

                .build();

        follower.followPath(Path1);




        while (opModeIsActive()) {
            telemetry.addData("following path chain",  follower.getFollowingPathChain());
            telemetry.addData("Motif ID", MotifID);

            huskylens();
            telemetry.update();
            follower.update();

            if (MotifID == 1 && !follower.getFollowingPathChain()) {
                PPG();
            }
            if (MotifID == 2&& !follower.getFollowingPathChain()) {
                PGP();
            }
            if (MotifID == 3&& !follower.getFollowingPathChain()) {
                GPP();
            }

        }
    }
}
