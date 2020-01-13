package org.firstinspires.ftc.teamcode.Superclass_Dependables.Qual3OpModes.RedAllianceAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@Autonomous (name = "Q3: RedFBridgeP")

// UPDATED

public class RedFBridgeP extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        // Align robot to foundation
        forward(0.2, 3.7);
        sleep(250);

        telemetry.addLine("Aligned");
        telemetry.update();

        // Drag foundation
        strafeLeft(0.2,11.5);
        sleep(250);

        setHookDown();
        sleep(250);

        strafeRight(0.2,13);
        setHookUp();
        sleep(250);

        telemetry.addLine("Dragged");
        telemetry.update();

        // park robot
        strafeRight(0.2, 2);
        sleep(250);

        backward(0.2,17);
        sleep(250);

        telemetry.addLine("Parked");
        telemetry.update();

        sleep(10000);

        // move to bridge
        strafeLeft(0.2,10);
        sleep(250);

        sleep(30000);
    }
}