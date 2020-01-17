package org.firstinspires.ftc.teamcode.Superclass_Dependables.Qual3OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.*;
import static org.firstinspires.ftc.teamcode.Utilities.ControlConstants.*;
import static org.firstinspires.ftc.teamcode.Utilities.UniversalVariables.*;

/*
 *      CURRENT CONTROLS:
 *
 *      A: Stacking
 *      B: Hook
 *      Y: 180deg Rotation
 *
 *      Dpad Up: Set Extension Target
 *      Dpad Down: Set Retraction Target
 *      Dpad Left: Reset Target and Slides
 *      Dpad Right: Run to Target
 *
 *      Right Trigger: Slow-mode
 */

@TeleOp (name = "Q3: TeleOp")

public class Qual3TeleOp extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            // DRIVETRAIN --------------------------------------------------------------------------

            double lefty = -gamepad1.left_stick_y;
            float leftx = gamepad1.left_stick_x;
            float rightx = gamepad1.right_stick_x;

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
            frpower = Math.pow((lefty - leftx - rightx), 3);
            brpower = Math.pow((lefty + leftx - rightx), 3);

            frontLeft.setPower(flpower);

            // Motor Power is halved while either joystick button is held down to allow for more
            // precise robot control
            if (gamepad1.right_trigger > 0.9) {

                flpower /= 5;
                frpower /= 5;
                blpower /= 5;
                brpower /= 5;
            }

            else if (gamepad1.right_trigger > 0.1) {

                slow = -0.8 * gamepad1.right_trigger + 1;

                flpower *= slow;
                frpower *= slow;
                blpower *= slow;
                brpower *= slow;
            }

            // Set Motor Powers
            frontLeft.setPower(flpower);
            backLeft.setPower(blpower);
            frontRight.setPower(frpower);
            backRight.setPower(brpower);

            // DRIVE -------------------------------------------------------------------------------
            if (gamepad1.y && y == 0)
                y = 1;

            else if (!gamepad1.dpad_up && y == 1) {

                rotateRight(1, 180);
                y = 0;
            }

            // HOOK --------------------------------------------------------------------------------
            if (gamepad1.b && b == 0)
                b = 1;

            else if (!gamepad1.b && b == 1) {
                hookDown();
                b = 2;
            }

            else if (gamepad1.b && b == 2)
                b = 3;

            else if (!gamepad1.b && b == 3) {
                hookUp();
                b = 0;
            }

            // Y SLIDES ----------------------------------------------------------------------------

            // Every Dpad_Up press increases level until max level
            // Dpad_Right press runs slides
            if (getYPosInches() < Y_MAX_EXTENSION) {

                if (gamepad1.dpad_up && up == 0)
                    up = 1;

                else if (!gamepad1.dpad_up && up == 1) {

                    yTargetInches += STONE_HEIGHT;
                    up = 2;
                }

                else if (gamepad1.dpad_right && up == 2)
                    up = 3;

                else if (!gamepad1.dpad_right && up == 3) {

                    yExtend(1,yTargetInches);
                    yTargetInches = 0;
                    up = 0;
                }
            }

            // Every Dpad_Down press decreases level until min level
            // Dpad_Right press runs slides
            if (getYPosInches() > Y_MIN_EXTENSION) {

                if (gamepad1.dpad_down && down == 0)
                    down = 1;

                else if (!gamepad1.dpad_down && down == 1) {

                    yTargetInches += STONE_HEIGHT;
                    down = 2;
                }

                else if (gamepad1.dpad_right && down == 2)
                    down = 3;

                else if (!gamepad1.dpad_right && down == 3) {

                    yRetract(1,yTargetInches);
                    yTargetInches = 0;
                    down = 0;
                }
            }

            // Dpad_Left press resets target and slides
            if (gamepad1.dpad_left && left == 0)
                left = 1;

            else if (!gamepad1.dpad_left && left == 1) {

                yTargetInches = 0;
                yRetract(1, getYPosInches() - Y_MIN_EXTENSION);
                left = 0;
            }

            // STACK -------------------------------------------------------------------------------

            // First 'A' press extends X-Slides to stackable position
            // 'B' press after first 'A' press retracts X-Slides
            // Second 'A' press places stone
            // Third 'A' press resets robot to intake-ready position
            if (gamepad1.a && a == 0)
                a = 1;

            else if (!gamepad1.a && a == 1) {

                xExtend(1, X_MAX_EXTENSION - getXPosInches());
                a = 2;
            }

            else if (gamepad1.b && a == 2) {

                xRetract(1, X_MAX_EXTENSION - getXPosInches());
                a = 0;
            }

            else if (gamepad1.a && a == 2)
                a = 3;

            else if (!gamepad1.a && a == 3) {

                stack();
                a = 4;
            }

            else if (gamepad1.a && a == 4)
                a = 5;

            else if (!gamepad1.a && a == 5) {

                resetRobot();
                a = 0;
            }

            // CLAMP-INTAKE ------------------------------------------------------------------------

            if(range.getDistance(DistanceUnit.INCH) < 0.5) {

                closeClamp();
                intakeOff();
            }

            else {

                openClamp();
                intake();
            }

            // CAN BE AUTOMATED WITH SENSORS
            /*
            if (gamepad1.x && x == 0)
                x = 1;

            else if (!gamepad1.x && x == 1) {
                closeClamp();
                x = 2;
            }
             */

            // INTAKE ------------------------------------------------------------------------------

            // CAN BE AUTOMATED WITH SENSORS
            /*
            // Pressing either bumper twice in succession stops intake
            // Pressing right bumper intakes
            // Pressing left bumper outtakes
            if (gamepad1.right_bumper && (rBumper == 4 || rBumper == 0))
                rBumper = 1;

            else if (!gamepad1.right_bumper && rBumper == 1) {
                intake();
                rBumper = 2;
            }

            // REVERSE INTAKE
            else if (gamepad1.left_bumper && (rBumper == 0 || rBumper == 2))
                rBumper = 3;

            else if (!gamepad1.left_bumper && rBumper == 3) {
                outtake();
                rBumper = 4;
            }

            // OFF
            else if ((gamepad1.right_bumper && rBumper == 2) || (gamepad1.left_bumper && rBumper == 4))
                rBumper = 5;

            else if(!gamepad1.right_bumper && !gamepad1.left_bumper && rBumper == 5) {
                intakeOff();
                rBumper = 0;
            }
             */
        }
    }
}