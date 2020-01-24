package org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@Autonomous (name = "Vuforia Test: Skystone Detector")

public class VuforiaSkystoneDetector extends Skystone10022Superclass {

    @Override
    public void runOpMode() throws  InterruptedException {

        initialize();

        initializeVuforia();

        waitForStart();

        vuforiaScan(true);

        telemetry.addLine("SCAN FINISHED");
        telemetry.update();
        sleep(10000);
    }
}