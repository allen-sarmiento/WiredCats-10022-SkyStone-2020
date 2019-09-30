package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * LOG:
 *
 * 9/28/19
 * - When the joystick is pushed all the way up, y = 1.0, x = 0.0. As the joystick rotates
 * clockwise, y remains at 1.0 while x increases, up until a certain point where y begins to
 * decrease and x continues to increase to 1.0 and vice versa in the anticlockwise direction
 *
 * - The IMU measure from 0 degrees to 180 degrees and then measures from -179 back to 0
 *
 * - Max inputs for motor power was at 2.57. Min inputs (when the joystick isn't moved) for motor
 * power was -0.11. This is considered negligible as it is not enough to power the motor
 */

@TeleOp

public class SkyStone10022Experimental extends OpMode{

    //IMU
    BNO055IMU imu;
    Orientation rotation;

    @Override
    public void init(){

        //DEVICE Initialization

        //IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
    }

    @Override
    public void loop(){

        // TELEMETRY
        double lefty = -gamepad1.left_stick_y;
        double leftx = gamepad1.left_stick_x;
        double rightx = gamepad1.right_stick_x;

        double FClefty;
        double FCleftx;
        double FCrightx;
        double antiClockWiseHeading;

        if (rotation.firstAngle >= 0) {

            //Clockwise measurement
            FClefty = lefty * Math.cos(Math.toRadians(rotation.firstAngle)) + leftx * Math.sin(Math.toRadians(rotation.firstAngle));
            FCleftx = -lefty * Math.sin(Math.toRadians(rotation.firstAngle)) + leftx * Math.cos(Math.toRadians(rotation.firstAngle));
        }

        else {

            //Accounts for measuring -180 to 0, anticlockwise measurement
            antiClockWiseHeading = -(Math.toRadians(rotation.firstAngle));
            FClefty = lefty * Math.cos(antiClockWiseHeading) - leftx * Math.sin(antiClockWiseHeading);
            FCleftx = lefty * Math.sin(antiClockWiseHeading) + leftx * Math.cos(antiClockWiseHeading);
        }

        FCrightx = rightx;

        // Net Motor Inputs
        double frontLeftNet = FClefty + FCleftx + FCrightx;
        double backLeftNet = FClefty - FCleftx + FCrightx;
        double frontRightNet = FClefty - FCleftx - FCrightx;
        double backRightNet = FClefty + FCleftx - FCrightx;

        // Joystick
        telemetry.addLine("------------------------------");
        telemetry.addLine("RAW JOYSTICK INPUTS");
        telemetry.addData("Left Stick Y: ", lefty);
        telemetry.addData("Left Stick X: ", leftx);
        telemetry.addData("Right Stick X: ", rightx);

        telemetry.addLine("------------------------------");

        //Motor Powers
        telemetry.addLine("MOTOR INPUTS");
        telemetry.addData("FL Input: ", frontLeftNet);
        telemetry.addData("BL Input: ", backLeftNet);
        telemetry.addData("FR Input: ", frontRightNet);
        telemetry.addData("BR Input: ", backRightNet);

        telemetry.addLine("------------------------------");

        // Robot Orientation
        telemetry.addLine("ROBOT ORIENTATION");
        rotation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Pitch: ", rotation.firstAngle);

        telemetry.addLine("------------------------------");

        telemetry.update();
    }
}
