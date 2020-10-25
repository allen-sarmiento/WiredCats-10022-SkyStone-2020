package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "Park")

public class Park extends R3Gen3Superclass {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        backward(0.3, 15);
        sleep(200);

        sleep(30000);

    }
}
