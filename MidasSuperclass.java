package org.firstinspires.ftc.teamcode.StatesBot.Midas;

import org.firstinspires.ftc.teamcode.StatesBot.PIDController;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    public DcMotor frontLeft, frontRight, backLeft, backRight;

    // REV Sensors
    public BNO055IMU imu;
    public Orientation theta;
    public double temp;
    public DistanceSensor range;
    public double robotHeading;

    // PID
    public PIDController pidForward = new PIDController(0.001, 0, 0);
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
    public ElapsedTime pidTimer;

    // CONTROL CONSTANTS ---------------------------------------------------------------------------

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;
    public boolean dualControl = false;

    // DRIVE CONTROL
    public double slow;
    public double flpower, frpower, blpower, brpower;

    public final double WHEEL_DIAMETER_INCHES = 4;  //temp
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;   //temp
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20   //temp
    public final double DRIVE_GEAR_REDUCTION = 1;   //temp
    public final double DRIVE_TICKS_PER_INCH = (((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES) * (5.0/6));   //temp

    public final double ROBOT_RADIUS_INCHES  = 9.933;   //temp
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;    //temp
    public final double DRIVE_INCHES_PER_DEGREE  = ((ROBOT_CIRCUMFERENCE_INCHES / 360)*(1.175/1)); // temp
    // public final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // METHODS -------------------------------------------------------------------------------------

    // Initialize robot
    public void initialize() {

        //DEVICE INITIALIZATION

        telemetry.addLine("Initializing Robot...");
        telemetry.update();

        // DRIVETRAIN
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // PID -------------------------------------------------------------------------------------
        pidTimer = new ElapsedTime();

        telemetry.addLine("Robot Initialized");
        telemetry.update();
    }

    // DRIVETRAIN

    public void forward(double pow, double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH * (5.15/6);

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, 1,
                    1, 1);
            setDriveMode();

            while (opModeIsActive() && driveIsBusy()) {

                setDrivePower(pow);
            }

            stopDrive();
            resetDriveMode();
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

    public void forwardPID(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, 1,
                    1, 1);
            setDriveMode();

            pidForward.resetPID();

            while (opModeIsActive() && (!pidForward.isFinished || driveIsBusy())) {

                pidForward.performPID(getDrivePosition(), target, 5, pidTimer);
                setDrivePower(pidForward.correction);
            }

            stopDrive();
            resetDriveMode();
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

    public void runEncoder(DcMotor m_motor, double power, double ticks) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)ticks);

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy()) {
            m_motor.setPower(power);
        }

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetDriveEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
        double average = total/4;
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

    public double getHeading() {

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return theta.firstAngle;
    }

    public boolean inRange() {

        if (range.getDistance(DistanceUnit.INCH) < 0.825)
            return true;
        else
            return false;
    }

    public void displayTelemetry() {

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Global Angle: " + theta.firstAngle);
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition());
        telemetry.addLine("FR: " + frontRight.getCurrentPosition());
        telemetry.addLine("BL: " + backLeft.getCurrentPosition());
        telemetry.addLine("BR: " + backRight.getCurrentPosition());
        telemetry.update();
    }
}
