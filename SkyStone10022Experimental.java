package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

/**
 *      CONTROL SCHEME:
 *
 *      Gamepad 1:

 *      Right Bumper: Intake toggle
 *      Left Bumper: Outtake toggle
 *
 *      Left Joystick Y-Axis: Forward/Backward
 *      Left Joystick X-Axis: Strafing
 *      Right Joystick X-Axis: Rotation
 *
 *      Right Trigger: Half Robot Speed
 *
 *      Gamepad 2:
 *
 *      B: Set Foundation Hook
 *      X: Open/Close Claw
 *      Y: Rotate Claw
 *
 *      Up: Raise Vertical Slides
 *      Down: Lower Vertical Slides
 *      Left: Retract Horizontal Slides
 *      Right: Extend Horizontal Slides
 *
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

            /* ROBOT-CENTRIC DRIVETRAIN */ {

                float lefty = -gamepad1.left_stick_y;
                float leftx = gamepad1.left_stick_x;
                float rightx = gamepad1.right_stick_x;

                // Joystick deadzones prevents unintentional drivetrain movements

                if (Math.abs(lefty) <= 0.2) {

                    lefty = 0;
                }
                if (Math.abs(leftx) <= 0.2) {

                    leftx = 0;
                }
                if (Math.abs(rightx) <= 0.2) {

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

                if (gamepad1.right_trigger > 0.9) {

                    flpower /= 2;
                    frpower /= 2;
                    blpower /= 2;
                    brpower /= 2;
                }
                else if (gamepad1.right_trigger > 0.1) {

                    rTrigger = (double)((1 - gamepad1.right_trigger ) /2 + 0.5);

                    flpower *= rTrigger;
                    frpower *= rTrigger;
                    blpower *= rTrigger;
                    brpower *= rTrigger;
                }

                frontLeft.setPower(flpower);
                backLeft.setPower(blpower);
                frontRight.setPower(frpower);
                backRight.setPower(brpower);
            }

            /* foundation hook */ {
                if (gamepad2.b && bToggle == 0) {

                    bToggle = 1;

                } else if (!gamepad2.b && bToggle == 1) {

                    setHookDown();
                    bToggle = 2;

                } else if (gamepad2.b && bToggle == 2) {

                    bToggle = 3;

                } else if (!gamepad2.b && bToggle == 3) {

                    setHookUp();
                    bToggle = 0;
                }
            }

            /* CLAW ACTIVATE */ {
                if (gamepad2.x && xToggle == 0) {

                    xToggle = 1;

                } else if (!gamepad2.x && xToggle == 1) {

                    activateClaw();
                    intakeOff();
                    xToggle = 2;

                } else if (gamepad2.x && xToggle == 2) {

                    xToggle = 3;

                } else if (!gamepad2.x && xToggle == 3) {

                    deactivateClaw();
                    xToggle = 0;
                }
            }

            /* CLAW ROTATE */ {
                if (gamepad2.y && yToggle == 0) {

                    yToggle = 1;

                } else if (!gamepad2.y && yToggle == 1) {

                    rotateClawOut();
                    yToggle = 2;

                } else if (gamepad2.y && yToggle == 2) {

                    yToggle = 3;

                } else if (!gamepad2.y && yToggle == 3) {

                    rotateClawSide();
                    yToggle = 4;

                } else if (gamepad2.y && yToggle == 4) {

                    yToggle = 5;

                } else if (!gamepad2.y && yToggle == 5) {

                    rotateClawIn();
                    yToggle = 0;
                }

            }

            /* vertical slides */ {
                if (gamepad2.dpad_up) {

                    ySlidesUp();

                } else if (gamepad2.dpad_down) {

                    ySlidesDown();

                } else {

                    ySlidesStop();

                }
            }

            /* horizontal slides */ {
                if (gamepad2.dpad_right) {

                    xSlideForward();

                } else if (gamepad2.dpad_left) {

                    xSlideBackward();

                } else {

                    xSlideOff();
                }
            }

            /* intake HOLD-- inactive */
            /** intakes by default
             *
             *  while left bumper is pressed = outtake
             *  while right bumper is pressed = intake off
             *  else intakes


             if (gamepad1.left_bumper) {
             outtake();
             }
             else if (gamepad1.right_bumper) {
             intakeOff();
             }
             else {
             intake();
             }
             */

            /* intake */ {
                if (gamepad1.right_bumper && rBumperToggle == 0) {

                    rBumperToggle = 1;

                } else if (!gamepad1.right_bumper && rBumperToggle == 1) {

                    intake();
                    rBumperToggle = 2;

                } else if (gamepad1.right_bumper && rBumperToggle == 2) {

                    rBumperToggle = 3;

                } else if (!gamepad1.right_bumper && rBumperToggle == 3) {

                    //outtake
                    {
                        if (gamepad1.left_bumper && lBumperToggle == 0) {

                            lBumperToggle = 1;

                        } else if (!gamepad1.left_bumper && lBumperToggle == 1) {

                            outtake();
                            lBumperToggle = 2;

                        } else if (gamepad1.left_bumper && lBumperToggle == 2) {

                            rBumperToggle = 3;

                        } else if (!gamepad1.right_bumper && lBumperToggle == 3) {

                            intakeOff();
                            lBumperToggle = 0;
                        }
                    }

                    rBumperToggle = 0;
                }
            }

        }
    }
}
