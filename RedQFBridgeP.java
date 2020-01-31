package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Skystone10022Superclass;

@Autonomous (name = "Red: Quarry&Foundation: WallP")

public class RedQFBridgeP extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        //initialize & initialize Vuforia
        initialize();
        initializeVuforia();

        waitForStart();

        // upon start
        // vuforia
        vuforiaScanRed(true);
        // skyStonePos is now updated

        // drive up to the skystones
        forward(0.75, 25);

        /* gather & deliver skystone */

        // align with skystone

        /*
         * if for some reason the stone is not 4-6,
         * (ex. if vuforia failed & it defaults to -1,)
         * the robot will assume position 6,
         * since that's quickest.
         */

        if (skyStonePos == 4)
            strafeLeft(3);
        else if (skyStonePos == 5)
            strafeLeft(0);
        else
            strafeRight(3);

        // get skystone
        intake();
        forward(0.75, 2);
        closeClaw();
        backward(4);
        intakeOff();

        // traverse field
        strafeRight(84);

        // inch up to foundation
        forward(0.75, 2);

        // place
        yExtend(0.75, 0);
        xExtend(0.75, X_MAX_EXTENSION);
        openClaw();

        // bring slides back in
        //bring y slides back in
        xExtend(0.75, -X_MAX_EXTENSION);

        // return to quarry
        backward(2);
        intake();
        strafeLeft(108);

        // second skystone
        forward(0.75, 2);
        closeClaw();
        backward(2);
        intakeOff();

        // deliver
        strafeRight(108);

        // inch up to foundation
        forward(0.75, 2);

        // place
        yExtend(0.75, 0);
        xExtend(0.75, X_MAX_EXTENSION);
        openClaw();

        // bring slides back in
        //bring y slides back in
        xExtend(0.75, -X_MAX_EXTENSION);

        // move foundation
        hookDown();
        rotateRight(0.75, 90);
        strafeRight(25.0);
        forward(0.75, 5.25);
        hookUp();

        //park
        backward(36);
        strafeRight(24);
    }
}