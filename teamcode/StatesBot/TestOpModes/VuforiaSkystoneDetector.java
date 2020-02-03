package org.firstinspires.ftc.teamcode.StatesBot.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@Autonomous (name = "Vuforia Test: Skystone Detector")

public class VuforiaSkystoneDetector extends Skystone10022Superclass {

    @Override
    public void runOpMode() throws  InterruptedException {

        initialize();

        initializeVuforia();

        waitForStart();

        vuforiaScanRed(true);

        telemetry.addLine("SCAN FINISHED");
        telemetry.update();
        sleep(10000);
    }
}