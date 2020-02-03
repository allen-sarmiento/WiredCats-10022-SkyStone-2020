package org.firstinspires.ftc.teamcode.StatesBot.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@Autonomous (name = "Red: Quarry")

public class RedQuarry extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        //initialize & initialize Vuforia
        initialize();
        initializeVuforia();

        waitForStart();

        vuforiaScanRed(true);
        skyStonePos = 6;
        /* if (skyStonePos == 4)
            skystonePos = 6; */
        hookUp();

        // drive up to quarry
        forward(0.3, 25);
        sleep(200);

        if (skyStonePos == 4) {
            strafeLeft(0.3, 16.5);
            sleep(200);
        }
        else if (skyStonePos == 5) {
            strafeLeft(0.3,12);
            sleep(200);
        }

        // grab skystone
        intake();
        forward(0.3, 18);
        closeClaw();
        sleep(200);

        backward(0.3, 21);
        intakeOff();
        sleep(200);

        rotateRight(0.3, 88.5);
        sleep(200);

        // deliver
        if (skyStonePos == 4) {
            forward(0.3, 55);
        }
        else if (skyStonePos == 5) {
            forward(0.3, 45);
        }
        else {
            forward(0.3, 42);
        }

        openClaw();
        outtake();
        sleep(200);

        rotateLeft(0.3, 88.5);
        sleep(200);

        // drive to quarry again
        if (skyStonePos == 4) {
            strafeLeft(0.3, 70);
            openClaw();
            sleep(200);;
        }
        else if (skyStonePos == 5) {
            strafeLeft(0.3, 70);
            openClaw();
            sleep(200);
        }
        else {
            strafeLeft(0.3, 63);
            openClaw();
            sleep(200);
        }

        // grab skystone
        intake();
        forward(0.3, 21);
        closeClaw();
        sleep(200);

        backward(0.3, 21);
        intakeOff();
        sleep(200);

        rotateRight(0.3, 88.5);
        sleep(200);

        // drive to quarry again
        if (skyStonePos == 4) {
            strafeRight(0.3, 40);
            openClaw();
            sleep(200);;
        }
        else if (skyStonePos == 5) {
            strafeRight(0.3, 35);
            openClaw();
            sleep(200);
        }
        else {
            forward(0.3, 95);
            openClaw();
            sleep(200);
        }

        // park
        backward(0.3, 12);
        sleep(200);

        // wait till end of period
        sleep(30000);
    }
}