package org.firstinspires.ftc.teamcode.States.R3Gen3.DriverOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

import java.util.concurrent.TimeUnit;

@TeleOp (name = "ClosedLoop: TeleOp")

public class ClosedLoopTeleOp extends R3Gen3Superclass {

    @Override
    public void runOpMode() {

        initialize();
        hookDown();
        waitForStart();
        int cl = 0;
        int r = 0;

        TeleOpTime.reset();

        while (opModeIsActive()) {

            int ticksPerInch = 200;

            // DRIVETRAIN --------------------------------------------------------------------------

            double lefty = -gamepad1.left_stick_y;
            double leftx = gamepad1.left_stick_x;
            double rightx = gamepad1.right_stick_x;

            // Joystick deadzones prevents unintentional drivetrain movements
            if (Math.abs(lefty) <= 0.2)
                lefty = 0;

            if (Math.abs(leftx) <= 0.2)
                leftx = 0;

            if (Math.abs(rightx) <= 0.2)
                rightx = 0;

            // Motor powers are set to the power of 3 so that the drivetrain motors accelerates
            // exponentially instead of linearly
            flpower = Math.pow((lefty + leftx + rightx), 3);
            blpower = Math.pow((lefty - leftx + rightx), 3);
            frpower = Math.pow((lefty -leftx - rightx), 3);
            brpower = Math.pow((lefty + leftx - rightx), 3);

            // Motor Power is halved while the right trigger is held down to allow for more
            // precise robot control
            if (gamepad1.right_trigger > 0.8) {

                flpower /= 3;
                frpower /= 3;
                blpower /= 3;
                brpower /= 3;
            }

            else if (gamepad1.right_trigger > 0.1) {

                driveSlow = -0.8 * gamepad1.right_trigger + 1;

                flpower *= driveSlow;
                frpower *= driveSlow;
                blpower *= driveSlow;
                brpower *= driveSlow;
            }

            // Set Motor Powers
            frontLeft.setPower(flpower);
            backLeft.setPower(blpower);
            frontRight.setPower(frpower);
            backRight.setPower(brpower);

            //Gwongs nutty closed loop

            if(gamepad1.right_bumper && cl == 0){


                cl = 1;


            }
            else if(!gamepad1.right_bumper && cl == 1){

                hookDown();
                retractPusher();
                openClaw();
                retractX();
                flipClawIn();
                intakeOn();
                cl = 2;

            }
            else if(gamepad1.right_bumper && cl == 2){

                cl = 3;

            }
            else if(!gamepad1.right_bumper && cl == 3){

                while(range.getDistance(DistanceUnit.CM ) > 3.25){

                    intakeOn();
                    doTeleOp();
                    /*
                    if(range.getDistance(DistanceUnit.CM) > 1 && range.getDistance(DistanceUnit.CM) < 3.5){
                        extendPusher();
                        sleep(100);
                    }
                    */

                }
                extendPusher();
                doTeleOp();
                sleep(350);
                retractPusher();
                doTeleOp();
                sleep(150);
                intakeOff();
                closeClaw();
                extendX();
                liftEncoder(0.7, ticksPerInch * 8);
                flipClawOut();
                liftEncoder(0.7, -ticksPerInch * 7.5);
                cl = 0;

            }


            if(gamepad2.b && r == 0){

                r = 1;

            }
            else if(!gamepad2.b && r == 1){

                liftEncoder(0.7, ticksPerInch * 8);
                flipClawIn();
                liftEncoder(0.7, -ticksPerInch * 7.5);
                retractX();
                openClaw();
                retractPusher();
                r = 0;


            }

            // LINEAR SLIDES -----------------------------------------------------------------------
    
                /*if (gamepad1.left_trigger > 0.1)
                    verticalSlow = -0.8 * gamepad1.left_trigger + 1;
                else
                    verticalSlow = 0;
    */
            if (gamepad2.dpad_up)
                extendY();
            else if (gamepad2.dpad_down)
                retractY();
            else
                yOff();


            if (gamepad2.dpad_right)
                extendX();
            else if (gamepad2.dpad_left)
                retractX();

            // CAPSTONE
            if (gamepad2.a && a == 0)
                a = 1;
            else if (!gamepad2.a && a == 1) {
                setCapstone();
                a = 2;
            }
            else if (gamepad2.a && a == 2)
                a = 3;
            else if (!gamepad2.a && a == 3) {
                resetCapstone();
                a = 0;
            }


            // CLAW ACTIVATE

            if (gamepad2.x && x == 0)
                x = 1;
            else if (!gamepad2.x && x == 1) {
                openClaw();
                x = 2;
            }
            else if (gamepad2.x && x == 2)
                x = 3;
            else if (!gamepad2.x && x == 3) {
                closeClaw();
                x = 0;
            }

            // CLAW ROTATE

            if (gamepad2.y && y == 0)
                y = 1;
            else if (!gamepad2.y && y == 1) {
                flipClawOut();
                y = 2;
            }
            else if (gamepad2.y && y == 2)
                y = 3;
            else if (!gamepad2.y && y == 3) {
                flipClawIn();
                y = 0;
            }

            if (gamepad2.back && back == 0)
                back = 1;
            else if (!gamepad2.back && back == 1) {
                y = 0;
                flipClawSideways();
                back = 0;
            }

            // HOOK

            if (gamepad1.b && b == 0)
                b = 1;
            else if (!gamepad1.b && b == 1) {
                hookDown();
                b = 2;
            }
            else if (gamepad1.b && b == 2)
                b = 3;
            else if (!gamepad1.start && b == 3) {
                hookUp();
                b = 0;
            }

            // INTAKE ------------------------------------------------------------------------------
            // REVERSE INTAKE
            else if (gamepad1.left_bumper && (intakeToggle == 0))
                intakeToggle = 1;
            else if (!gamepad1.left_bumper && intakeToggle == 1) {
                intakeReverse();
                intakeToggle = 2;
            }
            // OFF
            else if ((gamepad1.left_bumper && intakeToggle == 2))
                intakeToggle = 3;
            else if(!gamepad1.left_bumper && intakeToggle == 3) {
                intakeOff();
                intakeToggle = 0;
            }

            getHeading();
            displayTelemetry();
        }
    }
}
