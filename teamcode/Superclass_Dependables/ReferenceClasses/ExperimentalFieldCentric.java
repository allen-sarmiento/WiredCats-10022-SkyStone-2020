package org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@TeleOp (name = "Experimental: Field-Centric Drive")

public class ExperimentalFieldCentric extends Skystone10022Superclass {


    @Override
    public void runOpMode() {

        initialize();

        while(!(isStarted()  || isStopRequested())) {

            idle();
        }

        waitForStart();

        while (opModeIsActive()) {

            // Print Robot Orientation
            theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

            telemetry.addData("Rotation (Radians): ", theta.firstAngle);
            telemetry.addData("Rotation (Degrees): ", Math.toDegrees(theta.firstAngle));

            /*

            double forward = -gamepad1.left_stick_y;
            double right = gamepad1.left_stick_x;
            double clockwise = gamepad1.right_stick_x;

             */

            double forward = -1;
            double right = gamepad1.left_stick_x;
            double clockwise = gamepad1.right_stick_x;

            // If theta is measured clockwise from zero reference
            if (theta.firstAngle <= 0) {

                temp = forward * Math.cos(-theta.firstAngle) + right * Math.sin(-theta.firstAngle);
                right = -forward * Math.sin(-theta.firstAngle) + right * Math.cos(-theta.firstAngle);
                forward = temp;
            }

            // If theta is measured counterclockwise from zero reference

            if (theta.firstAngle > 0) {

                // Theta is reversed to account for IMU measurement
                temp = forward * Math.cos(theta.firstAngle) - right * Math.sin(theta.firstAngle);
                right = forward * Math.sin(theta.firstAngle) + right * Math.cos(theta.firstAngle);
                forward = temp;
            }

            flpower = forward + clockwise + right;
            frpower = forward - clockwise - right;
            blpower = forward + clockwise - right;
            brpower = forward - clockwise + right;

            double max = Math.abs(flpower);

            if (Math.abs(frpower) > max) {

                max = Math.abs(frpower);
            }

            if (Math.abs(blpower) > max) {

                max = Math.abs(blpower);
            }

            if (Math.abs(brpower) > max) {

                max = Math.abs(brpower);
            }

            if (max > 1) {

                flpower /= max;
                frpower /= max;
                blpower /= max;
                brpower /= max;
            }

            /*

            frontLeft.setPower(-flpower);
            frontRight.setPower(-frpower);
            backLeft.setPower(-blpower);
            backRight.setPower(brpower);

             */

            telemetry.update();
        }
    }
}


