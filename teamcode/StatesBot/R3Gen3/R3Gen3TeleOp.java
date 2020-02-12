package org.firstinspires.ftc.teamcode.StatesBot.R3Gen3;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "R3Gen3: TeleOp")

public class R3Gen3TeleOp extends R3Gen3Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        TeleOpTime.reset();

        while (opModeIsActive()) {

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
            frpower = Math.pow((lefty - leftx - rightx), 3);
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

            // LINEAR SLIDES -----------------------------------------------------------------------

            if (gamepad1.dpad_up)
                extendY();
            else if (gamepad1.dpad_down)
                retractY();
            else
                yOff();

            if (gamepad1.dpad_right)
                extendX();
            else if (gamepad1.dpad_left)
                retractX();

            // INTAKE ------------------------------------------------------------------------------

            if (gamepad1.right_bumper && (intakeToggle == 4 || intakeToggle == 0))
                intakeToggle = 1;
            else if (!gamepad1.right_bumper && intakeToggle == 1) {
                intakeOn();
                intakeToggle = 2;
            }
            // REVERSE INTAKE
            else if (gamepad1.left_bumper && (intakeToggle == 0 || intakeToggle == 2))
                intakeToggle = 3;
            else if (!gamepad1.left_bumper && intakeToggle == 3) {
                intakeReverse();
                intakeToggle = 4;
            }
            // OFF
            else if ((gamepad1.right_bumper && intakeToggle == 2) || (gamepad1.left_bumper && intakeToggle == 4))
                intakeToggle = 5;
            else if(!gamepad1.right_bumper && !gamepad1.left_bumper && intakeToggle == 5) {
                intakeOff();
                intakeToggle = 0;
            }
        }
    }
}