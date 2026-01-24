package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public final Soldiers_Shared soldiers;

    public Robot(HardwareMap hw, Telemetry tel) {
        soldiers = new Soldiers_Shared(hw, tel);
    }

    public void init() {
        soldiers.init();
    }

    public void update() {
        soldiers.update();
    }
}
