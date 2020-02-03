package org.firstinspires.ftc.teamcode.StatesBot.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@Autonomous (name = "Blue: Foundation")

public class BlueFoundation extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        forward(0.3, 10);
        sleep(100);

        hookDown();
        sleep(250);

        backward(0.3, 10);
        sleep(100);

        hookUp();
        sleep(250);

        strafeLeft(0.3, 10);
        sleep(30000);
    }
}