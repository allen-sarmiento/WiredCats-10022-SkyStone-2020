package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

public class SkyStone10022TeleOp extends SkyStone10022LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        while(!(isStarted()  || isStopRequested())) {

            idle();

        }

        waitForStart();

        while (opModeIsActive()) {

            //DRIVETRAIN

            // Robot Centric

            float lefty = -gamepad1.left_stick_y;
            float leftx = gamepad1.left_stick_x;
            float rightx = gamepad1.right_stick_x;

            // deadzones
            if (lefty <= 0.2) {
                lefty = 0;
            }
            if (leftx <= 0.2) {
                leftx = 0;
            }
            if (rightx <= 0.2) {
                rightx = 0;
            }
            // end deadzones

            // increment

            frontLeft.setPower(Math.pow((lefty + leftx + rightx), 3));      // power of 3
            backLeft.setPower(Math.pow((lefty - leftx + rightx), 3));       // makes it so the robot
            frontRight.setPower(Math.pow((lefty - leftx - rightx), 3));     //accelerates exponentially,
            backRight.setPower(Math.pow((lefty + leftx - rightx), 3));      //not linear-ly
            // end drivetrain

            // HOOK
            // for foundation platform
            if (gamepad1.b && toggle1 == 0) {

                toggle1 = 1;
            } else if (!gamepad1.b && toggle1 == 1) {

                setHookL.setPosition(0.1);
                setHookR.setPosition(0.1);
                toggle1 = 2;
            } else if (gamepad1.b && toggle1 == 2) {

                toggle1 = 3;
            } else if (!gamepad1.b && toggle1 == 3) {

                setHookL.setPosition(.7);
                setHookR.setPosition(.7);
                toggle1 = 0;
            }
            // END HOOK

            // claw
            if (gamepad1.x && toggle3 == 0) {

                toggle3 = 1;
            } else if (!gamepad1.x && toggle3 == 1) {

                clampActivate.setPosition(0.77);
                toggle3 = 2;
            } else if (gamepad1.x && toggle3 == 2) {

                toggle3 = 3;
            } else if (!gamepad1.x && toggle3 == 3) {

                clampActivate.setPosition(1);
                toggle3 = 0;
            }

            // claw rotate
            if (gamepad1.y && toggle2 == 0) {

                toggle2 = 1;
            } else if (!gamepad1.y && toggle2 == 1) {

                clampRotate.setPosition(clampInitPosition - 0.375);     // rotates 0 --> 90 degrees
                toggle2 = 2;
            } else if (gamepad1.y && toggle2 == 2) {

                toggle2 = 3;
            } else if (!gamepad1.y && toggle2 == 3) {

                clampRotate.setPosition(clampInitPosition);         // rotates back to original position
                toggle2 = 0;                                        // 0 degrees
            }
            // claw rotate end


            // HORIZONTAL SLIDES

            if (gamepad1.dpad_right) {
                xSlide.setPower(1.0);
            } else if (gamepad1.dpad_left) {
                xSlide.setPower(-1.0);
            } else {
                xSlide.setPower(0.0);
            }
            // end horizontal slides


            /* VERTICAL SLIDES -- manual mode -- backup
            if (gamepad1.dpad_up) {
                ySlide.setPower(1.0);
            } else if (gamepad1.dpad_down) {
                ySlide.setPower(-1.0);
            } else {
                ySlide.setPower(0.0);
            }
            end vertical slides */

            // VERTICAL SLIDES -- automatic mode -- WIP

            // dpad up (y slide up)
            if (gamepad1.dpad_up && yToggle == 0) {
                ySlide.setTargetPosition(ySlidePosition + change1);    //change1 = change from 0 to 1
                yToggle = 1;
            }
            else if (gamepad1.dpad_up && yToggle == 1) {
                ySlide.setTargetPosition(ySlidePosition + change);    //change = change from 1/2/3 to 2/3/4
                yToggle = 2;
            }
            else if (gamepad1.dpad_up && yToggle == 2) {
                ySlide.setTargetPosition(ySlidePosition + 2*change);
                yToggle = 3;
            }
            else if (gamepad1.dpad_up && yToggle == 3) {
                ySlide.setTargetPosition(ySlidePosition + 3*change);
                yToggle = 4;
            }

            //dpad down (y slide down)
            else if (gamepad1.dpad_down && yToggle == 4) {
                ySlide.setTargetPosition(ySlidePosition + 2*change);
                yToggle = 3;
            }
            else if (gamepad1.dpad_down && yToggle == 3) {
                ySlide.setTargetPosition(ySlidePosition + change);
                yToggle = 2;
            }
            else if (gamepad1.dpad_down && yToggle == 2) {
                ySlide.setTargetPosition(ySlidePosition + change1);
                yToggle = 1;
            }
            else if (gamepad1.dpad_down && yToggle == 1) {
                ySlide.setTargetPosition(ySlidePosition);
                yToggle = 0;
            }
            //end vertical slide code

        }
    }
}
