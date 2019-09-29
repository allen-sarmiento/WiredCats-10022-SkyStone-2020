package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * TeleOp Robot-Centric Drive
 */

@TeleOp

public class SkyStone10022TeleOpRC extends OpMode{

    //DECLARATION

    //Drivetrain
    DcMotor frontLeft, frontRight, backLeft, backRight;

    //Arm
    DcMotor spoolMotor, armMotor;

    //Intake
    Servo clawIntake;
    CRServo clawRotate;

    //Hook
    Servo setHookL;
    Servo setHookR;

    //Variables
    int toggle1 = 0, toggle2 = 0, toggle3 = 0, toggle4 = 0;

    //IMU
    BNO055IMU imu;
    Orientation angles;

    @Override
    public void init(){

        //DEVICE MAPPING

        //Drivetrain
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //Arm
        spoolMotor = hardwareMap.dcMotor.get("spoolMotor");
        armMotor = hardwareMap.dcMotor.get("armMotor");

        //Intake
        clawIntake = hardwareMap.servo.get("clawIntake");
        clawRotate = hardwareMap.crservo.get("clawRotate");

        //Hook
        setHookL = hardwareMap.servo.get("setHookL");
        setHookR = hardwareMap.servo.get("setHookR");
        setHookR.setDirection(Servo.Direction.REVERSE);

        //IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
    }

    @Override
    public void loop(){

        //TELEMETRY
        telemetry.addData("Servo Current Position: ", setHookL.getPosition());
        telemetry.addData("Servo Current Position: ", setHookR.getPosition());

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Pitch: ", angles.firstAngle);
        telemetry.update();

        //DRIVETRAIN

        /*
            Gamepad 1:
            Left Analog Y - Forward, Backward
            Left Analog X - Strafing
            Right Analog X - Rotation
        */

        float lefty = -gamepad1.left_stick_y;
        float leftx = gamepad1.left_stick_x;
        float rightx = gamepad1.right_stick_x;

        frontLeft.setPower(lefty + leftx + rightx);
        backLeft.setPower(lefty - leftx + rightx);
        frontRight.setPower(lefty - leftx - rightx);
        backRight.setPower(lefty + leftx - rightx);

        //HOOK

        /*
            Gamepad 1:
            B (Toggle): Hook Down/Up
            A (Toggle): Reset Hook Position
        */

        if (gamepad1.b == true && toggle1 == 0) {

            toggle1 = 1;
        }

        else if (gamepad1.b == false && toggle1 == 1){

            setHookL.setPosition(0);
            setHookR.setPosition(0);
            toggle1 = 2;
        }

        else if (gamepad1.b == true && toggle1 == 2){

            toggle1 = 3;
        }

        else if (gamepad1.b == false && toggle1 == 3){

            setHookL.setPosition(1);
            setHookR.setPosition(1);
            toggle1 = 0;
        }

        if (gamepad1.a == true && toggle2 == 0){

            toggle2 = 1;
        }

        else if (gamepad1.a == false && toggle2 == 1){

            setHookL.setPosition(1);
            setHookR.setPosition(1);
            toggle2 = 0;
        }

        //ARM

        /*
            Gamepad 2:
            Dpad Up: Arm Pivot Up
            Dpad Down: Arm Pivot Down
            Y: Extend Slides
            A: Retract Slides
        */

        //Arm Pivot Up/Down
        if (gamepad2.dpad_up == true){

            armMotor.setPower(1.0);
        }

        else if (gamepad2.dpad_down == true){

            armMotor.setPower(-1.0);
        }

        else {

            armMotor.setPower(0);
        }

        //Extend/Retract Slides
        if (gamepad2.y == true){

            spoolMotor.setPower(-1.0);
        }

        else if (gamepad2.a == true){

            spoolMotor.setPower(1.0);
        }

        else {

            spoolMotor.setPower(0);
        }

        //INTAKE

        /*
            Gamepad 2:
            X (Toggle): Claw Open/Close
            B (Toggle): Reset Claw
        */

        //Claw Open/Close
        if (gamepad2.x == true && toggle3 == 0) {

            toggle3 = 1;
        }

        else if (gamepad2.x == false && toggle3 == 1){

            clawIntake.setPosition(1);
            toggle3 = 2;
        }

        else if (gamepad2.x == true && toggle3 == 2){

            toggle3 = 3;
        }

        else if (gamepad2.x == false && toggle3 == 3){

            clawIntake.setPosition(0);
            toggle3 = 0;
        }

        //Reset Claw
        if (gamepad2.b == true && toggle4 == 0){

            toggle4 = 1;
        }

        else if (gamepad2.b == false && toggle4 == 1){

            clawIntake.setPosition(1);
            toggle4 = 0;
        }

        //Rotate Claw
        if (gamepad2.left_bumper == true){

            clawRotate.setPower(-1);
        }

        else if (gamepad2.right_bumper == true){

            clawRotate.setPower(1);
        }

        else {

            clawRotate.setPower(0);
        }
    }
}
