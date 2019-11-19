package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous

public class RedBuildAuto extends SkyStone10022LinearOpMode {

    @Override public void runOpMode() {

        initialize();

        waitForStart();

        strafeLeft(0.2,10.5);

        sleep(750);

        setHookDown();

        sleep(750);

        strafeRight(0.2, 11.5);

        sleep(750);

        backward(0.2, 7);

        sleep(750);

        rotateRight(0.2, 35);

        sleep(750);

        setHookUp();

        sleep(750);

        forward(0.2, 5);

        sleep(750);

        strafeRight(0.2, 10);

        sleep(30000);
    }
}