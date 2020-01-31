package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Skystone10022Superclass;

@Autonomous (name = "Red: Foundation: BridgeP")

public class RedFBridgeP extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        // align with foundation
        forward(0.75, 25.0);

        // move foundation
        hookDown();
        rotateRight(0.75, 90);
        strafeRight(26.0);
        forward(0.75, 5.25);
        hookUp();

        // park
        backward(36);
        strafeLeft(24.5);
    }
}