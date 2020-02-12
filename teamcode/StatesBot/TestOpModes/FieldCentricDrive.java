package org.firstinspires.ftc.teamcode.StatesBot.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@TeleOp (name = "FieldCentricDrive")

public class FieldCentricDrive extends Skystone10022Superclass {

    String frdir = "", fldir = "", brdir = "", bldir = "";

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            // Print Robot Orientation
            theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            double forward = -gamepad1.left_stick_y;
            double right = gamepad1.left_stick_x;
            double clockwise = gamepad1.right_stick_x;

            if (getHeading() > 0) {
                temp = forward * Math.cos(Math.toRadians(getHeading())) + right * Math.sin(Math.toRadians(getHeading()));
                right = -forward * Math.sin(Math.toRadians(getHeading())) + right * Math.cos(Math.toRadians(getHeading()));
                forward = temp;
            }

            if (getHeading() <= 0) {
                temp = forward * Math.cos(Math.toRadians(getHeading())) - right * Math.sin(Math.toRadians(getHeading()));
                right = forward * Math.sin(Math.toRadians(getHeading())) + right * Math.cos(Math.toRadians(getHeading()));
                forward = temp;
            }

            flpower = forward + clockwise + right;
            frpower = forward - clockwise - right;
            blpower = forward + clockwise - right;
            brpower = forward - clockwise + right;

            double max = Math.abs(flpower);

            if (Math.abs(frpower) > max)
                max = Math.abs(frpower);

            if (Math.abs(blpower) > max)
                max = Math.abs(blpower);

            if (Math.abs(brpower) > max)
                max = Math.abs(brpower);

            if (max > 1) {
                flpower /= max;
                frpower /= max;
                blpower /= max;
                brpower /= max;
            }

            if (flpower > 0)
                fldir = "POS";
            else if (flpower < 0)
                fldir = "NEG";
            else
                fldir = "-";

            if (frpower > 0)
                frdir = "POS";
            else if (frpower < 0)
                frdir = "NEG";
            else
                frdir = "-";

            if (blpower > 0)
                bldir = "POS";
            else if (blpower < 0)
                bldir = "NEG";
            else
                bldir = "-";

            if (brpower > 0)
                brdir = "POS";
            else if (brpower < 0)
                brdir = "NEG";
            else
                brdir = "-";

            telemetry.addData("Rotation (Radians): ", Math.toRadians(theta.firstAngle));
            telemetry.addData("Rotation (Degrees): ", theta.firstAngle);

            telemetry.addLine("FrontLeft: " + fldir + "\t\t" + "FrontRight: " + frdir);
            telemetry.addLine("BackLeft: " + bldir + "\t\t" + "BackRight: " + brdir);

            frontLeft.setPower(flpower);
            frontRight.setPower(frpower);
            backLeft.setPower(blpower);
            backRight.setPower(brpower);

            telemetry.update();
        }
    }
}


