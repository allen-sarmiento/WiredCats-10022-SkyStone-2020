package org.firstinspires.ftc.teamcode.StatesBot;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.graphics.Bitmap.*;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public abstract class Skystone10022Superclass extends LinearOpMode {

    // UNIVERSAL VARIABLES -------------------------------------------------------------------------
    public final double GRND = 0;
    public final double PLTFM = 0.5; // incl. clearance

    public final double STONE_HEIGHT = 5;
    public final double STONE_LENGTH = 8;
    public final double STONE_WIDTH = 4;

    public ElapsedTime TeleOpTime;

    // ROBOT OBJECTS -------------------------------------------------------------------------------

    // DRIVETRAIN
    public DcMotor frontLeft, frontRight, backLeft, backRight;

    // CLAW
    public Servo clawL, clawR;

    // INTAKE
    public DcMotor leftIntake, rightIntake;

    // HOOK
    public Servo hookL, hookR;

    // LINEAR SLIDES
    public DcMotor ySlide, xSlide;

    // REV Sensors
    public BNO055IMU imu;
    public Orientation theta;
    public double temp;
    public DistanceSensor range;
    public Servo blinkin;
    public double robotHeading;

    // VUFORIA
    public final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    public final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";
    public VuforiaLocalizer vuforia = null;

    public Image rgbImage = null;
    public VuforiaLocalizer.CloseableFrame closeableFrame = null;

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

    // TOGGLES
    public int a = 0, b = 0, x = 0, y = 0, up = 0, down = 0, left = 0, right = 0, rBumper = 0, back = 0;

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;
    public int skyStonePos = -1;
    public boolean dualControl = false;

    // DRIVE CONTROL
    public double slow;
    public double flpower, frpower, blpower, brpower;

    public final double WHEEL_DIAMETER_INCHES = 4;
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20
    public final double DRIVE_GEAR_REDUCTION = 1;
    public final double DRIVE_TICKS_PER_INCH = (((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES) * (5.0/6));

    public final double ROBOT_RADIUS_INCHES  = 9.933;
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public final double DRIVE_INCHES_PER_DEGREE  = ((ROBOT_CIRCUMFERENCE_INCHES / 360)*(1.175/1)); // temp
    // public final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // Y SLIDES
    public double yTargetLevel = 0;
    public double yCurrentLevel = 0;

    public final double MAX_LEVEL = 7;
    public final double Y_MIN_EXTENSION = GRND;
    public final double Y_MAX_EXTENSION = PLTFM + MAX_LEVEL * STONE_HEIGHT;

    public final double Y_SPOOL_DIAMETER_INCHES = 2;
    public final double Y_SPOOL_CIRCUMFERENCE_INCHES = Y_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double Y_TICKS_PER_MOTOR_REV = 1680; // NeveRest Classic 60
    public final double Y_GEAR_REDUCTION = 1;
    public final double Y_TICKS_PER_INCH = ((Y_TICKS_PER_MOTOR_REV * Y_GEAR_REDUCTION) / Y_SPOOL_CIRCUMFERENCE_INCHES);
    public double yTargetInches = ((STONE_HEIGHT * + yTargetLevel) + PLTFM) * Y_TICKS_PER_INCH;

    // X SLIDES
    public final double X_MIN_EXTENSION = -0.5;
    public final double X_MAX_EXTENSION = 5.5; // temp

    public final double X_SPOOL_DIAMETER_INCHES = 2;
    public final double X_SPOOL_CIRCUMFERENCE_INCHES = X_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double X_TICKS_PER_MOTOR_REV = 560; // NeveRest Classic 20
    public final double X_GEAR_REDUCTION = 1;
    public final double X_TICKS_PER_INCH = (((X_TICKS_PER_MOTOR_REV * X_GEAR_REDUCTION) / X_SPOOL_CIRCUMFERENCE_INCHES)*(2.5/1.5));
    public String stackState = "";

    // CLAW
    public final double CLAW_DOWN = 0.635;
    public final double CLAW_UP = 1;
    public ElapsedTime clawWait;

    // HOOK
    public final double HOOK_UP = 0.25;
    public final double HOOK_DOWN = 0.98;

    // TELEMTERY
    public boolean intakeIsLoaded;
    public boolean hookIsActivated;
    public boolean clawIsActivated;
    public double intakePower;

    // LED
    public final double STATIC_GOLD = 0.6845;
    public final double STATIC_BLUE_VIOLET = 0.7495;
    public final double STATIC_RED = 0.6695;
    public final double STATIC_YELLOW = 0.6945;
    public final double STATIC_GREEN = 0.7145;
    public final double STROBE_RED = 0.4695;
    public final double STROBE_GOLD = 0.4795;

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

        // CLAW
        clawWait = new ElapsedTime();
        clawL = hardwareMap.servo.get("clawL");
        clawR = hardwareMap.servo.get("clawR");
        clawR.setDirection(Servo.Direction.REVERSE);
        openClaw();

        // INTAKE
        leftIntake = hardwareMap.dcMotor.get("leftIntake");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake.setDirection(DcMotor.Direction.REVERSE);
        intakeOff();

        // HOOK
        hookL = hardwareMap.servo.get("setHookL");
        hookR = hardwareMap.servo.get("setHookR");
        hookL.setDirection(Servo.Direction.REVERSE);
        hookUp();

        // LINEAR SLIDES
        xSlide = hardwareMap.dcMotor.get("xSlide");
        ySlide = hardwareMap.dcMotor.get("ySlide");
        ySlide.setDirection(DcMotorSimple.Direction.REVERSE);
        resetSlideEncoders();
        resetSlideMode();

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        range = hardwareMap.get(DistanceSensor.class, "range");
        blinkin = hardwareMap.servo.get("blinkin");
        blinkin.setPosition(STROBE_GOLD);

        // PID -------------------------------------------------------------------------------------
        pidTimer = new ElapsedTime();
        TeleOpTime = new ElapsedTime();

        telemetry.addLine("Robot Initialized");
        telemetry.update();
    }

    public void initializeVuforia() {

        telemetry.addLine("Initializing Vuforia...");
        telemetry.update();

        // VUFORIA
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Skystone Target");

        targetsSkyStone.activate();

        com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);

        hookDown();

        telemetry.addLine("Vuforia Initialized");
        telemetry.update();
    }

    // Extend X-Slides and place stone
    public void stack() {

        // Place stone
        runEncoder(ySlide,1,-(3 * Y_TICKS_PER_INCH));
        openClaw();
        runEncoder(ySlide,1,(3 * Y_TICKS_PER_INCH));
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

    // HOOK
    public void hookDown() {

        hookL.setPosition(HOOK_DOWN);
        hookR.setPosition(HOOK_DOWN);
        hookIsActivated = true;
    }

    public void hookUp() {

        hookL.setPosition(HOOK_UP);
        hookR.setPosition(HOOK_UP);
        hookIsActivated = false;
    }

    // Y SLIDES

    public void yExtend() {

        ySlide.setPower(ON);
    }

    public void yRetract() {

        ySlide.setPower(REVERSE);
    }

    public void yOff() {

        ySlide.setPower(OFF);
    }

    public void yExtend(double power, double level) {

        double ticks = ((STONE_HEIGHT * level) + PLTFM) * Y_TICKS_PER_INCH;
        runEncoder(ySlide, power, ticks);
    }

    public void zeroY(double power) {

        runEncoder(ySlide, power, ((STONE_HEIGHT * -yTargetLevel) - PLTFM) * Y_TICKS_PER_INCH);
    }

    // X SLIDES

    public void xExtend() {

        xSlide.setPower(0.5);
    }

    public void xRetract() {

        xSlide.setPower(-0.5);
    }

    public void xOff() {

        xSlide.setPower(OFF);
    }

    public void xExtend(double power, double inches) {

        double ticks = inches * X_TICKS_PER_INCH; // Convert to ticks
        runEncoder(xSlide, power, ticks);
    }

    // INTAKE
    public void intake() {

        leftIntake.setPower(ON);
        rightIntake.setPower(ON);
        intakePower = 1;
    }

    public void outtake() {

        leftIntake.setPower(REVERSE);
        rightIntake.setPower(REVERSE);
        intakePower = -1;
    }

    public void intakeOff() {

        leftIntake.setPower(OFF);
        rightIntake.setPower(OFF);
        intakePower = 0;
    }


    // CLAW
    public void closeClaw() {

        clawL.setPosition(CLAW_DOWN);
        clawR.setPosition(CLAW_DOWN);
        clawIsActivated = true;
    }

    public void openClaw() {

        clawL.setPosition(CLAW_UP);
        clawR.setPosition(CLAW_UP);
        clawIsActivated = false;
    }

    // VUFORIA

    // RED SIDE QUARRY AUTO ONLY
    public void vuforiaScanRed(boolean saveBitmap) {

        // note: height and width are reversed

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); // Enables RGB565 format for image
        vuforia.setFrameQueueCapacity(1); // Store only one frame at a time

        // Capture Vuforia Frame
        while (rgbImage == null) {

            try {

                closeableFrame = vuforia.getFrameQueue().take();
                long numImages = closeableFrame.getNumImages();

                for (int i = 0; i < numImages; i++) {

                    if (closeableFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {

                        rgbImage = closeableFrame.getImage(i);

                        if (rgbImage != null)
                            break;
                    }
                }

            } catch (InterruptedException exc) {

            } finally {

                if (closeableFrame != null)
                    closeableFrame.close();
            }
        }

        if (rgbImage != null) {

            // Copy Bitmap from Vuforia Frame
            Bitmap quarry = Bitmap.createBitmap(rgbImage.getWidth(), rgbImage.getHeight(), Bitmap.Config.RGB_565);
            quarry.copyPixelsFromBuffer(rgbImage.getPixels());

            // Find Directory
            String path = Environment.getExternalStorageDirectory().toString();
            FileOutputStream out = null;

            // Save Bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, "Bitmap.png");
                    out = new FileOutputStream(file);
                    quarry.compress(Bitmap.CompressFormat.PNG, 100, out);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Crop Bitmap
            // (0,0) is the top-left corner of the bitmap
            int cropStartX;
            int cropStartY;
            int cropWidth;
            int cropHeight;

            cropStartX = (int)(0.3125 * quarry.getWidth());
            cropStartY = 0;
            cropWidth = (int)(0.06 * quarry.getWidth());    //temp
            cropHeight = (int)(1.0 * quarry.getHeight());

            // Create cropped bitmap to show only stones
            quarry = createBitmap(quarry, cropStartX, cropStartY, cropWidth, cropHeight);

            // Save cropped bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, "CroppedBitmap.png");
                    out = new FileOutputStream(file);
                    quarry.compress(Bitmap.CompressFormat.PNG, 100, out);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Compress bitmap to reduce scan time
            quarry = createScaledBitmap(quarry, 110, 20, true);

            int height, width, pixel, tempColorSum;
            int bitmapWidth = quarry.getWidth(), bitmapHeight = quarry.getHeight();
            int colorSum = 1000;
            int row;
            width = (int)(0.3425 * quarry.getWidth());
            double factor = 1.0 / 12;

            // check each of the 3 stones
            for (int currentStone = 4; currentStone <= 6; currentStone++) {

                height = (int) (quarry.getHeight() * factor);    // temp

                // 'pixel' uses a constant width and a variable height
                pixel = quarry.getPixel(width, height);

                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);

                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = currentStone;
                }

                factor += 1.0 / 3;
            }

            telemetry.addLine("Position: " + skyStonePos);
            telemetry.update();
        }
    }

    // BLUE SIDE QUARRY AUTO ONLY
    public void vuforiaScanBlue(boolean saveBitmap) {

        // note: height and width are reversed

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); // Enables RGB565 format for image
        vuforia.setFrameQueueCapacity(1); // Store only one frame at a time

        // Capture Vuforia Frame
        while (rgbImage == null) {

            try {

                closeableFrame = vuforia.getFrameQueue().take();
                long numImages = closeableFrame.getNumImages();

                for (int i = 0; i < numImages; i++) {

                    if (closeableFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {

                        rgbImage = closeableFrame.getImage(i);

                        if (rgbImage != null)
                            break;
                    }
                }

            } catch (InterruptedException exc) {

            } finally {

                if (closeableFrame != null)
                    closeableFrame.close();
            }
        }

        if (rgbImage != null) {

            // Copy Bitmap from Vuforia Frame
            Bitmap quarry = Bitmap.createBitmap(rgbImage.getWidth(), rgbImage.getHeight(), Bitmap.Config.RGB_565);
            quarry.copyPixelsFromBuffer(rgbImage.getPixels());

            // Find Directory
            String path = Environment.getExternalStorageDirectory().toString();
            FileOutputStream out = null;

            // Save Bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, "Bitmap.png");
                    out = new FileOutputStream(file);
                    quarry.compress(Bitmap.CompressFormat.PNG, 100, out);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Crop Bitmap
            // (0,0) is the top-left corner of the bitmap
            int cropStartX;
            int cropStartY;
            int cropWidth;
            int cropHeight;

            //cropStartX = (int)(0.3125 * quarry.getWidth());
            cropStartX = (int) (0.35 * quarry.getWidth());   // temp
            cropStartY = 0;
            //cropStartY = (int) (0.1518987342 * quarry.getHeight());
            cropWidth = (int) (0.06 * quarry.getWidth());
            cropHeight = (int) (1.0 * quarry.getHeight());

            // Create cropped bitmap to show only stones
            quarry = createBitmap(quarry, cropStartX, cropStartY, cropWidth, cropHeight);

            // Save cropped bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, "CroppedBitmap.png");
                    out = new FileOutputStream(file);
                    quarry.compress(Bitmap.CompressFormat.PNG, 100, out);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Compress bitmap to reduce scan time
            quarry = createScaledBitmap(quarry, 110, 20, true);

            int height, width, pixel, tempColorSum;
            int bitmapWidth = quarry.getWidth(), bitmapHeight = quarry.getHeight();
            int colorSum = 1000;

            // width = (int)(0.3425 * quarry.getWidth());
            width = (int) (0.35 * quarry.getWidth());    // temp

            //stone 6
            {
                height = (int) (bitmapHeight * 0.2405063291);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 6;
                }

                height = (int) (bitmapHeight * 0.3291139241);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 6;
                }

                height = (int) (bitmapHeight * 0.417721519);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 6;
                }
            }

            //stone 5
            {
                height = (int) (bitmapHeight * 0.58544303795);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 5;
                }

                height = (int) (bitmapHeight * 0.664556962);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 5;
                }

                height = (int) (bitmapHeight * 0.74367088605);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 5;
                }
            }

            //stone 4
            {
                height = (int) (bitmapHeight * 0.867088607575);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 4;
                }

                height = (int) (bitmapHeight * 0.91139240505);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 4;
                }

                height = (int) (bitmapHeight * 0.955696202525);
                pixel = quarry.getPixel(width, height);
                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = 4;
                }
            }

            telemetry.addLine("Position: " + skyStonePos);
            telemetry.update();
        }
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    public void runEncoder(DcMotor m_motor, double power, double ticks) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)ticks);

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy()) {

            if (dualControl)
                dualTeleOp();
            else
                soloTeleOp();

            m_motor.setPower(power);
        }

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetSlideEncoders() {

        xSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ySlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void resetSlideMode() {

        xSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ySlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public boolean isEndgame() {
        if (TeleOpTime.time(TimeUnit.MINUTES) > 1.5)
            return true;
        else
            return false;
    }

    public void displayTelemetry() {

        telemetry.addLine("CONTROL TYPE: ");
        telemetry.addLine("Dual Control: " + dualControl);
        telemetry.addLine();

        telemetry.addLine("CLAW AND HOOK:");
        telemetry.addLine("Claw Activated: " + clawIsActivated);
        telemetry.addLine("Hook Activated " + hookIsActivated);
        telemetry.addLine();

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Global Angle: " + theta.firstAngle);
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition());
        telemetry.addLine("FR: " + frontRight.getCurrentPosition());
        telemetry.addLine("BL: " + backLeft.getCurrentPosition());
        telemetry.addLine("BR: " + backRight.getCurrentPosition());
        telemetry.addLine("X-Slide: " + xSlide.getCurrentPosition()/X_TICKS_PER_INCH);
        telemetry.addLine("Y-Slide: " + ySlide.getCurrentPosition()/Y_TICKS_PER_INCH);
        telemetry.update();
    }

    public void soloTeleOp() {

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // DRIVETRAIN ------------------------------------------------------------------------------

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

        // HOOK --------------------------------------------------------------------------------
        if (gamepad1.b && b == 0)
            b = 1;

        else if (!gamepad1.b && b == 1) {
            hookDown();
            b = 2;
        }

        else if (gamepad1.b && b == 2)
            b = 3;

        else if (!gamepad1.b && b == 3) {
            hookUp();
            b = 0;
        }

        // SLIDES ----------------------------------------------------------------------------------

        if (gamepad1.dpad_up)
            yExtend();
        else if (gamepad1.dpad_down)
            yRetract();
        else
            yOff();

        if (gamepad1.dpad_right)
            xExtend();
        else if (gamepad1.dpad_left)
            xRetract();
        else
            xOff();

        // CLAW ------------------------------------------------------------------------------------

        if (gamepad1.x && x == 0)
            x = 1;
        else if (!gamepad1.x && x == 1) {
            closeClaw();
            intakeOff();
            x = 2;
        }
        else if (gamepad1.x && x == 2)
            x = 3;
        else if (!gamepad1.x && x == 3) {
            openClaw();
            x = 0;
        }

        // INTAKE ----------------------------------------------------------------------------------

        // Pressing either bumper twice in succession stops intake
        // Pressing right bumper intakes
        // Pressing left bumper outtakes
        if (gamepad1.right_bumper && (rBumper == 4 || rBumper == 0))
            rBumper = 1;
        else if (!gamepad1.right_bumper && rBumper == 1) {
            intake();
            rBumper = 2;
        }
        // REVERSE INTAKE
        else if (gamepad1.left_bumper && (rBumper == 0 || rBumper == 2))
            rBumper = 3;
        else if (!gamepad1.left_bumper && rBumper == 3) {
            outtake();
            rBumper = 4;
        }
        // OFF
        else if ((gamepad1.right_bumper && rBumper == 2) || (gamepad1.left_bumper && rBumper == 4))
            rBumper = 5;
        else if(!gamepad1.right_bumper && !gamepad1.left_bumper && rBumper == 5) {
            intakeOff();
            rBumper = 0;
        }

        // LED ------------------------------------------------------------------------------------

        if (intakeIsLoaded) {
            blinkin.setPosition(STATIC_GREEN);
        }

        else if (hookIsActivated)
            blinkin.setPosition(STATIC_BLUE_VIOLET);

        else if (isEndgame())
            blinkin.setPosition(STROBE_RED);

        else
            blinkin.setPosition(STROBE_GOLD);

        displayTelemetry();
    }

    public void dualTeleOp() {

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // DRIVETRAIN ------------------------------------------------------------------------------

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

        // HOOK --------------------------------------------------------------------------------
        if (gamepad1.b && b == 0)
            b = 1;

        else if (!gamepad1.b && b == 1) {
            hookDown();
            b = 2;
        }

        else if (gamepad1.b && b == 2)
            b = 3;

        else if (!gamepad1.b && b == 3) {
            hookUp();
            b = 0;
        }

        // SLIDES ----------------------------------------------------------------------------------

        if (gamepad2.dpad_up)
            yExtend();
        else if (gamepad2.dpad_down)
            yRetract();
        else
            yOff();

        if (gamepad2.dpad_right)
            xExtend();
        else if (gamepad2.dpad_left)
            xRetract();
        else
            xOff();

        // CLAW ------------------------------------------------------------------------------------

        if (gamepad1.x && x == 0)
            x = 1;
        else if (!gamepad1.x && x == 1) {
            closeClaw();
            intakeOff();
            x = 2;
        }
        else if (gamepad1.x && x == 2)
            x = 3;
        else if (!gamepad1.x && x == 3) {
            openClaw();
            x = 0;
        }

        // INTAKE ----------------------------------------------------------------------------------

        // Pressing either bumper twice in succession stops intake
        // Pressing right bumper intakes
        // Pressing left bumper outtakes
        if (gamepad1.right_bumper && (rBumper == 4 || rBumper == 0))
            rBumper = 1;
        else if (!gamepad1.right_bumper && rBumper == 1) {
            intake();
            rBumper = 2;
        }
        // REVERSE INTAKE
        else if (gamepad1.left_bumper && (rBumper == 0 || rBumper == 2))
            rBumper = 3;
        else if (!gamepad1.left_bumper && rBumper == 3) {
            outtake();
            rBumper = 4;
        }
        // OFF
        else if ((gamepad1.right_bumper && rBumper == 2) || (gamepad1.left_bumper && rBumper == 4))
            rBumper = 5;
        else if(!gamepad1.right_bumper && !gamepad1.left_bumper && rBumper == 5) {
            intakeOff();
            rBumper = 0;
        }

        // LED ------------------------------------------------------------------------------------

        if (intakeIsLoaded) {
            blinkin.setPosition(STATIC_GREEN);
        }

        else if (hookIsActivated)
            blinkin.setPosition(STATIC_BLUE_VIOLET);

        else if (isEndgame())
            blinkin.setPosition(STROBE_RED);

        else
            blinkin.setPosition(STROBE_GOLD);

        displayTelemetry();
    }
}