package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "SoldiersAutonomous", group = "Drive")

public class SoldiersAutonomous extends PedroOpMode {

    Robot robot;

    @Override
    public void init() {
        super.init();
        robot = new Robot(hardwareMap, telemetry);
        robot.init();
    }

    @Override
    public void loop() {
        super.loop();
        robot.update();
    }
}
