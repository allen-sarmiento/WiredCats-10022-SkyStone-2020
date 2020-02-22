package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "Blue: Foundation")

public class R3Gen3Auto extends R3Gen3Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();
    }
}