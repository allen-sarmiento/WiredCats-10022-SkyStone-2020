package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "Vuforia Test Blue")

public class VuforiaDetection extends R3Gen3Superclass {

    @Override
    public void runOpMode() throws InterruptedException {

        //initialize();

        initializeVuforia();

        waitForStart();

        vuforiaScanRed(false);

        sleep(10000);
    }
}
