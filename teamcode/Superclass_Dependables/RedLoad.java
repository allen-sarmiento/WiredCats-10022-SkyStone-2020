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
            activateClamp();

            //travel field
            strafeRight(0.75, 50);

            //place
            ySlidesUp();
            sleep(200);
            deactivateClamp();
            ySlidesStop();

            //return
            strafeLeft(0.75, 60);

            //get other skystone
            activateClamp();

            //deliver
            strafeRight(0.75, 60);

        } else if (stonePosition == 5) {

            strafeLeft(0.75, 5);

            //gather skystone
            activateClamp();

            //travel field
            strafeRight(0.75, 45);

            //place
            ySlidesUp();
            sleep(200);
            deactivateClamp();
            ySlidesStop();

            //return
            strafeLeft(0.75, 55);

            //get other skystone
            activateClamp();

            //deliver
            strafeRight(0.75, 55);

        } else {
            // for stonePosition == 6, don't have to move
            // in case of error in which stonePosition never receives a valid value,
            // the robot will, by default, choose the quickest route.

            //gather skystone
            activateClamp();

            //travel field
            strafeRight(0.75, 40);

            //place
            ySlidesUp();
            sleep(200);
            deactivateClamp();
            ySlidesStop();

            //return
            strafeLeft(0.75, 50);

            //get other skystone
            activateClamp();

            //deliver
            strafeRight(0.75, 50);

        }

        //place
        ySlidesUp();
        sleep(500);
        deactivateClamp();
        ySlidesStop();

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
