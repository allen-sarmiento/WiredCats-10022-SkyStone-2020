package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name = "Red Load")

public class RedLoad extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        // disclaimer: none of these numbers are close to accurate

        initialize();

        int stonePosition;
        // use vision
        // find stonePosition

        waitForStart();

        //forward
        forward(0.75, 10);

        //get skystone
        if (stonePosition == 4) {

            strafeLeft(0.75, 10);

            //gather skystone
            {
                intake();
                forward(0.75, 2);
                activateClamp();
                backward(0.75, 2);
                intakeOff();
            }

            //travel field
            strafeRight(0.75, 25);
            ySlidesUp();
            strafeRight(0.75, 25);

            //place
            forward(0.75, 2);
            ySlidesStop();
            outtake();
            deactivateClamp();

            //return
            backward(0.75, 2);
            strafeLeft(0.75, 60);   //50 tow travel field + 10 to reach the next skystone

            //get other skystone
            {
                intake();
                forward(0.75, 2);
                activateClamp();
                backward(0.75, 2);
                intakeOff();
            }

            //deliver
            strafeRight(0.75, 25);
            ySlidesUp();
            strafeRight(0.75, 35);

        } else if (stonePosition == 5) {

            // repeat for stone position

        } else {
            // for stonePosition == 6, don't have to move
            // in case of error in which stonePosition never receives a valid value,
            // the robot will, by default, choose the quickest route.

            // repeat for stone position

        }

        //place
        ySlidesStop();
        outtake();
        deactivateClamp();

        //move foundation
        rotateRight(0.75, 90);
        setHookDown();
        rotateRight(0.75, 180);
        backward(0.75, 5.25);
        strafeLeft(0.75, 5.25);
        setHookUp();

        //park
        forward(0.75, 5);
        // may need left or right movements as well

    }
}
