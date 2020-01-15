package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

import static org.firstinspires.ftc.teamcode.Utilities.ControlConstants.*;
import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.*;

public abstract class Skystone10022Superclass extends LinearOpMode {

    public void initialize() {

        //DEVICE INITIALIZATION

        // RUNTIME
        runtime.startTime();

        // DRIVETRAIN
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // CLAMP
        clamp = hardwareMap.servo.get("clamp");

        // INTAKE
        leftIntake = hardwareMap.dcMotor.get("leftIntake");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake.setDirection(DcMotor.Direction.REVERSE);

        // HOOK
        hookL = hardwareMap.servo.get("setHookL");
        hookR = hardwareMap.servo.get("setHookR");
        hookR.setDirection(Servo.Direction.REVERSE);

        // LINEAR SLIDES
        xSlide = hardwareMap.dcMotor.get("xSlide");
        ySlide = hardwareMap.dcMotor.get("ySlide");
        xSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        ySlide.setDirection(DcMotorSimple.Direction.REVERSE);

        // REV IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // PID ControlConstants
        pidRotate = new PIDController(0,0,0);
    }

    // DRIVETRAIN ----------------------------------------------------------------------------------

    public void forward(double power, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, 1,
                    1, 1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    public void backward(double power, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, -1,
                    -1, -1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeLeft(double power, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    1, -1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeRight(double power, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    -1, 1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    public void diagonal(double power, double inches, int quadrant) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();

                if (quadrant == 1) {
                    frontLeft.setTargetPosition((int)target);
                    backRight.setTargetPosition((int)target);
                }

                else if (quadrant == 2) {
                    backLeft.setTargetPosition((int)target);
                    frontRight.setTargetPosition((int)target);
                }

                else if (quadrant == 3) {
                    frontLeft.setTargetPosition((int)(-target));
                    backRight.setTargetPosition((int) (-target));
                }

                else if (quadrant == 4) {
                    backLeft.setTargetPosition((int) (-target));
                    frontRight.setTargetPosition((int) (-target));
                }

            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();

        }
    }

    public void rotateLeft(double power, double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setRotateTarget(target,
                    -1, 1,
                    -1, 1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateRight(double power, double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setRotateTarget(target,
                    1, -1,
                    1, -1);
            setDriveMode();

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
                setDrivePower(power);

            stopDrive();
            resetDriveMode();
        }
    }

    // HOOK ----------------------------------------------------------------------------------------
    public void hookDown() {

        hookL.setPosition(HOOK_DOWN);
        hookR.setPosition(HOOK_DOWN);
    }

    public void hookUp() {

        hookL.setPosition(HOOK_UP);
        hookR.setPosition(HOOK_UP);
    }

    // Y SLIDES ------------------------------------------------------------------------------------
    public void yExtend() {

        ySlide.setPower(ON);
    }

    public void yRetract() {

        ySlide.setPower(REVERSE);
    }

    public void yOff() {

        ySlide.setPower(OFF);
    }

    // X SLIDES ------------------------------------------------------------------------------------
    public void xExtend(double power, int inches) {

        //inches =

        runEncoder(xSlide, power, inches);
    }

    public void xExtend() {

        xSlide.setPower(ON);
    }

    public void xRetract() {

        xSlide.setPower(REVERSE);
    }

    public void xOff() {

        xSlide.setPower(OFF);
    }


    // INTAKE --------------------------------------------------------------------------------------
    public void intake() {

        leftIntake.setPower(ON);
        rightIntake.setPower(ON);
    }

    public void outtake() {

        leftIntake.setPower(REVERSE);
        rightIntake.setPower(REVERSE);
    }

    public void intakeOff() {

        leftIntake.setPower(OFF);
        rightIntake.setPower(OFF);
    }


    // CLAMP ---------------------------------------------------------------------------------------
    public void closeClamp() {

        clamp.setPosition(CLAMP_DOWN);
    }

    public void openClamp() {

        clamp.setPosition(CLAMP_UP);
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    public static void runEncoder(DcMotor m_motor, double power, double inches) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)(inches * DRIVE_TICKS_PER_INCH));

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy())
            m_motor.setPower(power);

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // DRIVETRAIN ----------------------------------------------------------------------------------
    public void resetDriveEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // DRIVE
    public void setDriveTarget(double dist, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * dist));
        backLeft.setTargetPosition((int) (bl * dist));
        frontRight.setTargetPosition((int) (fr * dist));
        backRight.setTargetPosition((int) (br * dist));
    }

    // ROTATIONAL
    public void setRotateTarget(double deg, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * deg));
        backLeft.setTargetPosition((int) (bl * deg));
        frontRight.setTargetPosition((int) (fr * deg));
        backRight.setTargetPosition((int) (br * deg));
    }

    public void setDriveMode() {

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetDriveMode() {

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setDrivePower(double pow) {

        frontLeft.setPower(pow);
        backLeft.setPower(pow);
        frontRight.setPower(pow);
        backRight.setPower(pow);
    }

    public void stopDrive() {

        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}
