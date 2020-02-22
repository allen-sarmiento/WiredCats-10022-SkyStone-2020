package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "Vuforia Test: Skystone Detector")

public class VuforiaDetection extends R3Gen3Superclass {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        initializeVuforia();

        waitForStart();

        vuforiaScanRed(true);

        telemetry.addLine("SCAN FINISHED");
        telemetry.update();
        sleep(10000);
    }
}
