package org.firstinspires.ftc.teamcode.Superclass_Dependables.Qual3OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.*;
import static org.firstinspires.ftc.teamcode.Utilities.ControlConstants.*;

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
            if (gamepad1.dpad_up)
                yExtend();

            else if (gamepad1.dpad_down)
                yRetract();

            else
                yOff();

            // X SLIDES ----------------------------------------------------------------------------
            if (gamepad1.dpad_right)
                xExtend();

            else if (gamepad1.dpad_left)
                xRetract();

            else
                xOff();

            // CLAMP -------------------------------------------------------------------------------
            if (gamepad1.x && x == 0)
                x = 1;

            else if (!gamepad1.x && x == 1) {
                openClamp();
                x = 2;

            } else if (gamepad1.x && x == 2)
                x = 3;

            else if (!gamepad1.x && x == 3) {
                closeClamp();
                x = 0;
            }

            // INTAKE ------------------------------------------------------------------------------
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
        }
    }
}