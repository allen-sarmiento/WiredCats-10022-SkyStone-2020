package org.firstinspires.ftc.teamcode.StatesBot.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.StatesBot.Midas.MidasSuperclass;

@TeleOp (name = "Drive: TeleOp")

public class Drive extends MidasSuperclass {

    @Override
    public void runOpMode() {

        initialize();

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

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
            } else if (gamepad1.right_trigger > 0.1) {

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

            telemetry.addLine("LeftY: " + lefty);
            telemetry.update();
        }
    }
}