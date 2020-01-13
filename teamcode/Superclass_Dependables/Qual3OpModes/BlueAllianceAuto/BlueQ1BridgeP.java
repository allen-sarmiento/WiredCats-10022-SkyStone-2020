package org.firstinspires.ftc.teamcode.Superclass_Dependables.Qual3OpModes.BlueAllianceAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@Autonomous (name = "Q3: BlueQ1BridgeP")

// UPDATED

public class BlueQ1BridgeP extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        // deliver stone 1
        intake();
        forward(0.2, 15.5);
        activateClamp();
        intakeOff();
        sleep(1000);

        backward(0.2, 9);
        sleep(250);

        strafeLeft(0.2, 16);
        sleep(250);

        deactivateClamp();
        forward(0.2, 2.5);
        outtake();
        sleep(250);

        backward(0.2, 2.5);
        sleep(250);

        // deliver stone 2
        strafeRight(0.2,21.5);
        sleep(250);

        intake();
        forward(0.2, 10);
        activateClamp();
        sleep(250);

        rotateLeft(0.2, 91);
        sleep(250);

        strafeLeft(0.2, 11.2);
        intakeOff();
        deactivateClamp();
        sleep(250);

        forward(0.2, 18.5);
        sleep(250);

        // park robot
        backward(0.2,6);
        sleep(250);

        sleep(30000);
    }
}