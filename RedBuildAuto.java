package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous

public class RedBuildAuto extends SkyStone10022LinearOpMode {

    @Override public void runOpMode() {

        initialize();

        waitForStart();

        forward(0.2, 3.7);

        sleep(250);

        strafeLeft(0.2,10.5);

        sleep(250);

        setHookDown();

        sleep(250);

        strafeRight(0.2,12);

        sleep(250);

        backward(0.2, 3.5);

        sleep(250);

        rotateRight(0.2, 115);

        sleep(250);

        setHookUp();

        sleep(250);

        forward(0.2, 11);

        sleep(250);

        strafeRight(0.2, 5);

        sleep(250);

        forward(0.2, 5);

        sleep(250);

        strafeRight(0.2, 9.5);

        /*
        sleep(250);

        rotateLeft(0.2, 45);

        intake();

        sleep(250);

        forward(0.2, 13);
        */

        sleep(30000);
    }
}
