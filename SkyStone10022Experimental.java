package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

/**
 *      CONTROL SCHEME:
 *
 *      A: Automatic Win
 *      B: Set Foundation Hook
 *      X: Open/Close Claw
 *      Y: Rotate Claw
 *
 *      Back: Switch Drive Mode from Field-Centric to Robot-Centric
 *
 *      Up: Raise Vertical Slides
 *      Down: Lower Vertical SLides
 *      Left: Retract Horizontal Slides
 *      Right: Extend Horizontal Slides
 *
 *      Right Bumper: Intake Block
 *      Left Bumper: Outtake Block
 *
 *      Left Joystick Y-Axis: Forward/Backward
 *      Left Joystick X-Axis: Strafing
 *      Right Joystick X-Axis: Rotation
 *
 *      Left Joystick Button: Half Robot Speed
 *      Right Joystick Button: Half Robot Speed
 */

public class SkyStone10022Experimental extends SkyStone10022LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        while(!(isStarted()  || isStopRequested())) {

            idle();

        }

        waitForStart();

        while (opModeIsActive()) {

            // START ROBOT-CENTRIC DRIVETRAIN

            float lefty = -gamepad1.left_stick_y;
            float leftx = gamepad1.left_stick_x;
            float rightx = gamepad1.right_stick_x;

            // Joystick deadzones prevents unintentional drivetrain movements

            if (lefty <= 0.2) {

                lefty = 0;
            }
            if (leftx <= 0.2) {

                leftx = 0;
            }
            if (rightx <= 0.2) {

                rightx = 0;
            }

            // Motor powers are set to the power of 3 so that the drivetrain motors accelerates
            // exponentially instead of linearly

            flpower = Math.pow((lefty + leftx + rightx), 3);
            blpower = Math.pow((lefty - leftx + rightx), 3);
            frpower = Math.pow((lefty - leftx - rightx), 3);
            brpower = Math.pow((lefty + leftx - rightx), 3);

            // Motor Power is halved while either joystick button is held down to allow for
            // more precise robot control

            if (gamepad1.left_stick_button || gamepad1.right_stick_button) {

                flpower /= 2;
                frpower/= 2;
                blpower /= 2;
                brpower /= 2;
            }

            frontLeft.setPower(flpower);
            backLeft.setPower(blpower);
            frontRight.setPower(frpower);
            backRight.setPower(brpower);

            // END ROBOT-CENTRIC DRIVETRAIN

            // START FOUNDATION HOOK

            if (gamepad1.b && bToggle == 0) {

                bToggle = 1;

            } else if (!gamepad1.b && bToggle == 1) {

                setHookDown();
                bToggle = 2;

            } else if (gamepad1.b && bToggle == 2) {

                bToggle = 3;

            } else if (!gamepad1.b && bToggle == 3) {

                setHookUp();
                bToggle = 0;
            }

            // END FOUNDATION HOOK

            // START CLAW ACTIVATE

            if (gamepad1.x && xToggle == 0) {

                xToggle = 1;

            } else if (!gamepad1.x && xToggle == 1) {

                clawActivate.setPosition(0.77);
                xToggle = 2;

            } else if (gamepad1.x && xToggle == 2) {

                xToggle = 3;

            } else if (!gamepad1.x && xToggle == 3) {

                clawActivate.setPosition(1);
                xToggle = 0;
            }

            // END CLAW ACTIVATE

            // START CLAW ROTATE

            if (gamepad1.y && yToggle == 0) {

                yToggle = 1;

            } else if (!gamepad1.y && yToggle == 1) {

                clawRotate.setPosition(clampInitPosition - 0.375);
                yToggle = 2;

            } else if (gamepad1.y && yToggle == 2) {

                yToggle = 3;

            } else if (!gamepad1.y && yToggle == 3) {

                clawRotate.setPosition(clampInitPosition);
                yToggle = 0;
            }

            // END CLAW ROTATE

            // START VERTICAL SLIDES

            if (gamepad1.dpad_up) {

                ySlideOne.setPower(1.0);
                ySlideTwo.setPower(1.0);

            } else if (gamepad1.dpad_down) {

                ySlideOne.setPower(-1.0);
                ySlideTwo.setPower(-1.0);

            } else {

                ySlideOne.setPower(0.0);
                ySlideTwo.setPower(0.0);
            }

            // END VERTICAL SLIDES

            // START HORIZONTAL SLIDES

            if (gamepad1.dpad_right) {

                xSlide.setPower(1.0);

            } else if (gamepad1.dpad_left) {

                xSlide.setPower(-1.0);

            } else {

                xSlide.setPower(0.0);
            }

            // END HORIZONTAL SLIDES

            // START INTAKE

            if (gamepad1.right_bumper && rBumperToggle == 0) {

                rBumperToggle = 1;

            } else if (!gamepad1.right_bumper && rBumperToggle == 1) {

                leftIntake.setPower(1);
                rightIntake.setPower(1);
                rBumperToggle = 2;

            } else if (gamepad1.right_bumper && rBumperToggle == 2) {

                rBumperToggle = 3;

            } else if (!gamepad1.right_bumper && rBumperToggle == 3) {

                leftIntake.setPower(0);
                rightIntake.setPower(0);
                rBumperToggle = 0;
            }

            if (gamepad1.left_bumper && lBumperToggle == 0) {

                lBumperToggle = 1;

            } else if (!gamepad1.left_bumper && lBumperToggle == 1) {

                leftIntake.setPower(-1);
                rightIntake.setPower(-1);
                lBumperToggle = 2;

            } else if (gamepad1.left_bumper && lBumperToggle == 2) {

                rBumperToggle = 3;

            } else if (!gamepad1.right_bumper && lBumperToggle == 3) {

                leftIntake.setPower(0);
                rightIntake.setPower(0);
                lBumperToggle = 0;
            }

            // END INTAKE
        }
    }
}
