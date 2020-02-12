package org.firstinspires.ftc.teamcode.StatesBot.Midas;

import org.firstinspires.ftc.teamcode.StatesBot.PIDController;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class MidasSuperclass extends LinearOpMode {

    // DRIVETRAIN
    public DcMotorEx frontLeft, frontRight, backLeft, backRight;

    public Servo mServo;

    // REV Sensors
    public BNO055IMU imu;
    public Orientation theta;
    public double temp;
    public DistanceSensor range;
    public double robotHeading;

    // PID
    public PIDController pidForward = new PIDController(0.0001, 0, 0);
    public PIDController pidBackward = new PIDController(0.001, 10, 1);
    public PIDController pidStrafeL = new PIDController(0, 0, 0);
    public PIDController pidStrafeR = new PIDController(0, 0, 0);
    public PIDController pidRotateGyro = new PIDController(0.00775, 0, 0);
    public PIDController pidRotateR = new PIDController(0.000725, 0, 0);
    public PIDController pidRotateL = new PIDController(0, 0, 0);
    public PIDController pidDiagQ1 = new PIDController(0, 0, 0);
    public PIDController pidDiagQ2 = new PIDController(0, 0, 0);
    public PIDController pidDiagQ3 = new PIDController(0, 0, 0);
    public PIDController pidDiagQ4 = new PIDController(0, 0, 0);
    public PIDController pidDriveVelLeft = new PIDController(0.004794,0,0);
    public PIDController pidDriveVelRight = new PIDController(0.001,0,0);
    public ElapsedTime pidTimer;

    // CONTROL CONSTANTS ---------------------------------------------------------------------------

    // TOGGLES
    public int a = 0, b = 0, x = 0, y = 0, up = 0, down = 0, left = 0, right = 0, rBumper = 0, back = 0;

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;
    public boolean dualControl = false;

    public ElapsedTime velocityTime;

    // DRIVE CONTROL
    public double slow;
    public double flpower, frpower, blpower, brpower;
    public String fldir, frdir, bldir, brdir;

    public final double WHEEL_DIAMETER_INCHES = 4;  //temp
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;   //temp
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20   //temp
    public final double DRIVE_GEAR_REDUCTION = 1;   //temp
    public final double DRIVE_TICKS_PER_INCH = ((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES);   //temp

    public final double ROBOT_RADIUS_INCHES  = 9.933;   //temp
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;    //temp
    public final double DRIVE_INCHES_PER_DEGREE  = ((ROBOT_CIRCUMFERENCE_INCHES / 360)*(1.175/1)); // temp
    // public final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // VELOCITY
    double totalDist;
    double refreshRate = 50;
    double ticksPerInOverSec = DRIVE_TICKS_PER_INCH * refreshRate / 20.0;
    double vel = 0;
    double lVel, rVel;

    boolean thread_run = true;
    double rpm_gate_time = 250;
    double LRPM, RRPM, RPM;

    // METHODS -------------------------------------------------------------------------------------

    public void initialize() {

        //DEVICE INITIALIZATION

        telemetry.addLine("Initializing Robot...");
        telemetry.update();

        // DRIVETRAIN
        frontLeft = (DcMotorEx)hardwareMap.dcMotor.get("frontLeft");
        frontRight = (DcMotorEx)hardwareMap.dcMotor.get("frontRight");
        backLeft = (DcMotorEx)hardwareMap.dcMotor.get("backLeft");
        backRight = (DcMotorEx)hardwareMap.dcMotor.get("backRight");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        resetDriveEncoders();
        resetDriveMode();

        mServo = hardwareMap.servo.get("AM");
        mServo.setPosition(0.8);

        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);

        velocityTime = new ElapsedTime();

        // PID -------------------------------------------------------------------------------------
        pidTimer = new ElapsedTime();

        telemetry.addLine("Robot Initialized");
        telemetry.update();
    }

    // DRIVETRAIN (RAW)
    public void leftVelPIDConstants(double p, double i, double d, double f){

        frontLeft.setVelocityPIDFCoefficients(p, i, d, f);
        backLeft.setVelocityPIDFCoefficients(p, i, d, f);

    }
    public void rightVelPIDConstants(double p, double i, double d, double f){

        frontRight.setVelocityPIDFCoefficients(p, i, d, f);
        backRight.setVelocityPIDFCoefficients(p, i, d, f);

    }

    public void setLeftVel(double vel){

        frontLeft.setVelocity(vel * DRIVE_TICKS_PER_INCH / 2.54);
        backLeft.setVelocity(vel * DRIVE_TICKS_PER_INCH / 2.54);

    }

    public void setRightVel(double vel){

        frontRight.setVelocity(vel * DRIVE_TICKS_PER_INCH / 2.54);
        backRight.setVelocity(vel * DRIVE_TICKS_PER_INCH / 2.54);

    }

    public void forward(double pow, double inches) {

        // Convert target to ticks
        double target =  inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            setDriveTarget((getDrivePosition() + target),
                    1, 1,
                    1, 1);

            while (opModeIsActive() && driveIsBusy()) {
                setDrivePower(pow);
            }
        }
    }

    public void backward(double pow, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH * (5.3/6);

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, -1,
                    -1, -1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {

                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeLeft(double pow, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH * 5.7/6;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    1, -1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {

                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeRight(double pow, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH * 5.2/6;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    -1, 1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {

                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void diagonal(double pow, double inches, int quadrant) {

        if (opModeIsActive()) {

            double target = inches * DRIVE_TICKS_PER_INCH * (1.0/1);

            resetDriveEncoders();

            if (quadrant == 1) {

                target = inches * DRIVE_TICKS_PER_INCH * (5.0/4.8);

                frontLeft.setTargetPosition((int)target);
                backRight.setTargetPosition((int)target);

                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (opModeIsActive() && frontLeft.isBusy() && backRight.isBusy()) {
                    frontLeft.setPower(pow);
                    backRight.setPower(pow);
                }
            }

            else if (quadrant == 2) {

                target = inches * DRIVE_TICKS_PER_INCH * (10.0/4.75);

                backLeft.setTargetPosition((int)target);
                frontRight.setTargetPosition((int)target);

                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (opModeIsActive() && backLeft.isBusy() && frontRight.isBusy()) {
                    backLeft.setPower(pow);
                    frontRight.setPower(pow);
                }
            }

            else if (quadrant == 3) {

                target = inches * DRIVE_TICKS_PER_INCH * (5.0/4.8);

                frontLeft.setTargetPosition((int)(-target));
                backRight.setTargetPosition((int) (-target));

                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (opModeIsActive() && frontLeft.isBusy() && backRight.isBusy()) {
                    frontLeft.setPower(pow);
                    backRight.setPower(pow);
                }
            }

            else if (quadrant == 4) {
                backLeft.setTargetPosition((int) (-target));
                frontRight.setTargetPosition((int) (-target));

                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                pidDiagQ4.resetPID();

                while (opModeIsActive() && backLeft.isBusy() && frontRight.isBusy()) {
                    backLeft.setPower(pow);
                    frontRight.setPower(pow);
                }
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateLeft(double pow, double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE * (87.0/90);

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    -1, 1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {
                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateRight(double pow, double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    1, -1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {
                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    // DRIVETRAIN (PID)

    public void leftDrivePID(double targetVel) {

        if (opModeIsActive()) {

            resetDriveEncoders();
            resetDriveMode();

            pidDriveVelLeft.resetPID();

            while (opModeIsActive() && !pidDriveVelLeft.isFinished) {
                pidForward.performPID(lVel, targetVel, 1, pidTimer);
                setDrivePower(pidDriveVelLeft.correction);
            }
        }
    }

    public void forwardPID(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, 1,
                    1, 1);
            setDriveMode();

            pidForward.resetPID();

            while (opModeIsActive() && !pidForward.isFinished && driveIsBusy()) {
                displayTelemetry();
                pidForward.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidForward.correction);
            }
        }
    }

    public void backwardPID(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, -1,
                    -1, -1);
            setDriveMode();

            pidBackward.resetPID();

            while (opModeIsActive() && !pidBackward.isFinished) {

                pidBackward.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidBackward.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeLeftPID(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    1, -1);
            setDriveMode();

            pidStrafeL.resetPID();

            while (opModeIsActive() && !pidStrafeL.isFinished) {

                pidStrafeL.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidStrafeL.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeRightPID(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    1, -1);
            setDriveMode();

            pidStrafeR.resetPID();

            while (opModeIsActive() && !pidStrafeR.isFinished) {

                pidStrafeR.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidStrafeR.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void diagonalPID(double inches, int quadrant) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();

            if (quadrant == 1) {
                frontLeft.setTargetPosition((int)target);
                backRight.setTargetPosition((int)target);

                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                pidDiagQ1.resetPID();

                while (opModeIsActive() && !pidDiagQ1.isFinished) {

                    pidDiagQ1.performPID(getDrivePosition(), target, 1, pidTimer);
                    frontLeft.setPower(pidDiagQ1.correction);
                    backRight.setPower(pidDiagQ1.correction);
                }
            }

            else if (quadrant == 2) {
                backLeft.setTargetPosition((int)target);
                frontRight.setTargetPosition((int)target);

                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                pidDiagQ2.resetPID();

                while (opModeIsActive() && !pidDiagQ2.isFinished) {

                    pidDiagQ2.performPID(getDrivePosition(), target, 1, pidTimer);
                    frontLeft.setPower(pidDiagQ2.correction);
                    backRight.setPower(pidDiagQ2.correction);
                }
            }

            else if (quadrant == 3) {
                frontLeft.setTargetPosition((int)(-target));
                backRight.setTargetPosition((int) (-target));

                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                pidDiagQ3.resetPID();

                while (opModeIsActive() && !pidDiagQ3.isFinished) {

                    pidDiagQ3.performPID(getDrivePosition(), target, 1, pidTimer);
                    frontLeft.setPower(pidDiagQ3.correction);
                    backRight.setPower(pidDiagQ3.correction);
                }
            }

            else if (quadrant == 4) {
                backLeft.setTargetPosition((int) (-target));
                frontRight.setTargetPosition((int) (-target));

                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                pidDiagQ4.resetPID();

                while (opModeIsActive() && !pidDiagQ4.isFinished) {

                    pidDiagQ4.performPID(getDrivePosition(), target, 1, pidTimer);
                    frontLeft.setPower(pidDiagQ4.correction);
                    backRight.setPower(pidDiagQ4.correction);
                }
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateRightPID(double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    1, -1);
            setDriveMode();

            pidRotateR.resetPID();

            while (opModeIsActive() && !pidRotateR.isFinished) {

                pidRotateR.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidRotateR.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateLeftPID(double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    1, -1);
            setDriveMode();

            pidRotateL.resetPID();

            while (opModeIsActive() && !pidRotateL.isFinished) {

                pidRotateR.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidRotateL.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    public void resetDriveEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void resetLeftDriveEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void resetRightDriveEncoders() {

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public boolean driveIsBusy() {

        if (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

            return true;
        }

        else
            return false;
    }

    public double getDrivePosition() {

        double total = frontLeft.getCurrentPosition() + frontRight.getCurrentPosition()
                + backLeft.getCurrentPosition() + backRight.getCurrentPosition();
        double average = total/4.0;
        return average;
    }

    public double getLeftDrivePosition() {

        double total = frontLeft.getCurrentPosition() + backLeft.getCurrentPosition();
        double average = total/2.0;
        return average;
    }

    public double getRightDrivePosition() {

        double total = frontRight.getCurrentPosition() + backRight.getCurrentPosition();
        double average = total/2.0;
        return average;
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

    public void setRightDrivePower(double pow) {

        frontRight.setPower(pow);
        backRight.setPower(pow);
    }

    public void setLeftDrivePower(double pow) {

        frontLeft.setPower(pow);
        backLeft.setPower(pow);
    }


    public void leftTurnPower(double pow) {

        frontLeft.setPower(-pow);
        backLeft.setPower(-pow);
        frontRight.setPower(pow);
        backRight.setPower(pow);
    }

    public void rightTurnPower(double pow) {

        frontLeft.setPower(pow);
        backLeft.setPower(pow);
        frontRight.setPower(-pow);
        backRight.setPower(-pow);
    }

    public void stopDrive() {

        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    public void setDriveTarget(double dist, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * dist));
        backLeft.setTargetPosition((int) (bl * dist));
        frontRight.setTargetPosition((int) (fr * dist));
        backRight.setTargetPosition((int) (br * dist));
    }

    public void setLeftDriveTarget(double dist, double fl, double bl) {

        frontLeft.setTargetPosition((int) (fl * dist));
        backLeft.setTargetPosition((int) (bl * dist));
    }

    public void setRightDriveTarget(double dist, double fr, double br) {

        frontRight.setTargetPosition((int) (fr * dist));
        backRight.setTargetPosition((int) (br * dist));
    }

    public double getHeading() {

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return theta.firstAngle;
    }

    public double getVelocityLeft() {

        return lVel = (frontLeft.getVelocity() + backLeft.getVelocity())/2 / DRIVE_TICKS_PER_INCH * 2.54;
    }

    public double getVelocityRight() {

        return rVel = (frontRight.getVelocity() + backRight.getVelocity())/2 / DRIVE_TICKS_PER_INCH * 2.54;
    }

    public void displayTelemetry() {

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Global Angle: " + getHeading());
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition());
        telemetry.addLine("FR: " + frontRight.getCurrentPosition());
        telemetry.addLine("BL: " + backLeft.getCurrentPosition());
        telemetry.addLine("BR: " + backRight.getCurrentPosition());
        telemetry.addLine("Left Velocity: " + getVelocityLeft());
        telemetry.addLine("Right Velocity: " + getVelocityRight());
        telemetry.update();
    }

    public void drive() {

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
    }

    public void driveFC() {

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