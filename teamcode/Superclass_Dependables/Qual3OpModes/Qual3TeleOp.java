package org.firstinspires.ftc.teamcode.Superclass_Dependables.Qual3OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@TeleOp (name = "Q3: TeleOp")

// UPDATED

public class Qual3TeleOp extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            // DRIVETRAIN
            double lefty = -gamepad1.left_stick_y;
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

            frontLeft.setPower(flpower);

            // Motor Power is halved while either joystick button is held down to allow for
            // more precise robot control
            if (gamepad1.right_trigger > 0.9) {

                flpower /= 5;
                frpower /= 5;
                blpower /= 5;
                brpower /= 5;

            }

            else if (gamepad1.right_trigger > 0.1) {

                rTrigger = -0.8 * gamepad1.right_trigger + 1;

                flpower *= rTrigger;
                frpower *= rTrigger;
                blpower *= rTrigger;
                brpower *= rTrigger;
            }

            // Set Motor Powers
            frontLeft.setPower(flpower);
            backLeft.setPower(blpower);
            frontRight.setPower(frpower);
            backRight.setPower(brpower);

            // HOOK
            if (gamepad1.b && bToggle == 0) {

                bToggle = 1;
            }

            else if (!gamepad1.b && bToggle == 1) {

                setHookDown();
                bToggle = 2;
            }

            else if (gamepad1.b && bToggle == 2) {

                bToggle = 3;
            }

            else if (!gamepad1.b && bToggle == 3) {

                setHookUp();
                bToggle = 0;
            }

            // Y SLIDES
            if (gamepad1.dpad_up) {

                ySlidesUp();
            }

            else if (gamepad1.dpad_down) {

                ySlidesDown();
            }

            else {

                ySlidesStop();
            }

            // X SLIDES
            if (gamepad1.dpad_right) {

                xSlideForward();
            }

            else if (gamepad1.dpad_left) {

                xSlideBackward();
            }

            else {

                xSlideOff();
            }

            // INTAKE
            if (gamepad1.right_bumper && (rBumperToggle == -1 || rBumperToggle == 0)) {

                rBumperToggle = 10;
            }

            else if (!gamepad1.right_bumper && rBumperToggle == 10) {

                intake();
                rBumperToggle = 1;
            }

            // REVERSE INTAKE
            else if (gamepad1.left_bumper && (rBumperToggle == 0 || rBumperToggle == 1)) {

                rBumperToggle = -10;
            }

            else if (!gamepad1.left_bumper && rBumperToggle == -10) {

                outtake();
                rBumperToggle = -1;
            }

            // OFF
            else if ((gamepad1.right_bumper && rBumperToggle == 1) || (gamepad1.left_bumper && rBumperToggle == -1)) {

                rBumperToggle = 50;
            }

            else if(!gamepad1.right_bumper && !gamepad1.left_bumper && rBumperToggle == 50) {

                intakeOff();
                rBumperToggle = 0;
            }

            // CLAMP
            if (gamepad1.x && xToggle == 0) {

                xToggle = 1;

            } else if (!gamepad1.x && xToggle == 1) {

                activateClamp();
                xToggle = 2;

            } else if (gamepad1.x && xToggle == 2) {

                xToggle = 3;

            } else if (!gamepad1.x && xToggle == 3) {

                deactivateClamp();
                xToggle = 0;
            }
        }
    }
}