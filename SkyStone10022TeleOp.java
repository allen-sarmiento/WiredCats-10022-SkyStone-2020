package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

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

            frontLeft.setPower(lefty + leftx + rightx);
            backLeft.setPower(lefty - leftx + rightx);
            frontRight.setPower(lefty - leftx - rightx);
            backRight.setPower(lefty + leftx - rightx);

            //HOOK

            if (gamepad1.b == true && toggle1 == 0) {

                toggle1 = 1;
            }

            else if (gamepad1.b == false && toggle1 == 1) {

                setHookL.setPosition(0.1);
                setHookR.setPosition(0.1);
                toggle1 = 2;
            }

            else if (gamepad1.b == true && toggle1 == 2) {

                toggle1 = 3;
            }

            else if (gamepad1.b == false && toggle1 == 3) {

                setHookL.setPosition(.7);
                setHookR.setPosition(.7);
                toggle1 = 0;
            }

            if (gamepad1.x == true && toggle3 == 0) {

                toggle3 = 1;
            }

            else if (gamepad1.x == false && toggle3 == 1) {

                clampActivate.setPosition(0.77);
                toggle3 = 2;
            }

            else if (gamepad1.x == true && toggle3 == 2) {

                toggle3 = 3;
            }

            else if (gamepad1.x == false && toggle3 == 3) {

                clampActivate.setPosition(1);
                toggle3 = 0;
            }

            if (gamepad1.y == true && toggle2 == 0) {

                toggle2 = 1;
            }

            else if (gamepad1.y == false && toggle2 == 1) {

                clampRotate.setPosition(0.4);
                toggle2 = 2;
            }

            else if (gamepad1.y == true && toggle2 == 2) {

                toggle2 = 3;
            }

            else if (gamepad1.y == false && toggle2 == 3) {

                clampRotate.setPosition(0.775);
                toggle2 = 0;
            }
        }
    }
}
