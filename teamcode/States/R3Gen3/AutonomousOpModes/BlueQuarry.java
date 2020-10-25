package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "Blue: Quarry")

public class BlueQuarry extends R3Gen3Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        vuforiaScanBlue(false);

        // Drive to Quarry First

        if(skyStonePos == 4) {
            strafeRight(0.3, 5);
            sleep(200);
        }
        else if (skyStonePos == 6) {
            strafeLeft(0.3, 5);
            sleep(200);
        }

        intakeOn();
        forward(0.3,24);
        intakeOff();
        sleep(200);

        backward(0.3,10);
        sleep(200);

        // Deliver First

        if(skyStonePos == 4) {
            strafeLeft(0.3, 60);
            intakeReverse();
            sleep(300);
        }
        else if (skyStonePos == 6) {
            strafeLeft(0.3, 60);
            intakeReverse();
            sleep(300);
        }
        else {
            strafeLeft(0.3,60);
            intakeReverse();
            sleep(300);
        }

        intakeOff();

        // Drive to Quarry Second

        if(skyStonePos == 4) {
            strafeRight(0.3, 60);
            intakeOn();
            sleep(300);
        }
        else if (skyStonePos == 6) {
            strafeRight(0.3, 60);
            intakeOn();
            sleep(300);
        }
        else {
            strafeRight(0.3,60);
            intakeOn();
            sleep(300);
        }

        forward(0.3, 10);
        sleep(200);

        backward(0.3, 10);
        intakeOff();
        sleep(200);

        // Deliver Second

        if(skyStonePos == 4) {
            strafeLeft(0.3, 60);
            intakeReverse();
            sleep(300);
        }
        else if (skyStonePos == 6) {
            strafeLeft(0.3, 60);
            intakeReverse();
            sleep(300);
        }
        else {
            strafeLeft(0.3,60);
            intakeReverse();
            sleep(300);
        }

        // Park

        intakeOff();
        strafeRight(0.3,10);
        sleep(200);

        sleep(30000);
    }
}
