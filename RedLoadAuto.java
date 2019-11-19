package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous

public class RedLoadAuto extends SkyStone10022LinearOpMode {

    @Override public void runOpMode() {

        initialize();

        waitForStart();

        strafeLeft(0.2, 3);

        sleep(1000);

        forward(0.2, 12);

        sleep(30000);

        // intake();

        /*

        forward(0.2, 0);

        sleep(250);

        rotateRight(0.2, 10);

        sleep(250);

        forward(0.2, 0);

        sleep(250);

        rotateLeft(0.2, 90);

        sleep(250);

        strafeLeft(0.2, 0);

        sleep(250);

        forward(0.2, 0);

        intakeOff();

        sleep(250);

        outtake();

        sleep(1000);

        backward(0.2, 0);

        sleep(30000);

         */
    }
}