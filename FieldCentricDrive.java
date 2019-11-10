package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp

public class FieldCentricDrive extends OpMode {

    //DECLARATION

    // Drivetrain
    DcMotor frontLeft, frontRight, backLeft, backRight;
    double flpower, frpower, blpower, brpower;

    // REV IMU
    BNO055IMU imu;
    Orientation theta;
    double temp;

    // Telemetry
    boolean fl, fr, bl, br;

    @Override
    public void init(){

        // DEVICE INITIALIZATION

        // Drivetrain
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
    }

    @Override
    public void loop() {

        // Print Robot Orientation
        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        telemetry.addData("Rotation (Radian): ", theta.firstAngle);
        telemetry.addData("Rotation (Degrees): ", Math.toDegrees(theta.firstAngle));

        double forward = -gamepad1.left_stick_y;
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

        frontLeft.setPower(-flpower);
        frontRight.setPower(-frpower);
        backLeft.setPower(-blpower);
        backRight.setPower(brpower);

        // Print Motor Power Direction
        telemetry.addLine();
        telemetry.addLine("FrontLeft: " + direction(flpower) + "\t\t\t" + "FrontRight: " + direction(frpower));
        telemetry.addLine();
        telemetry.addLine("BackLeft: " + direction(blpower) + "\t\t\t\t" + "BackRight: " + direction(brpower));

        // Update telemetry
        telemetry.update();
    }

    public String direction(double power) {

        if (power > 0) {
            return "Positive";
        }

        else if (power < 0) {
            return "Negative";
        }

        else {
            return "Zero";
        }
    }
}
