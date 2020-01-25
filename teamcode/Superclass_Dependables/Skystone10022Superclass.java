package org.firstinspires.ftc.teamcode.Superclass_Dependables;

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
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Bitmap.*;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public abstract class Skystone10022Superclass extends LinearOpMode {

    // UNIVERSAL VARIABLES -------------------------------------------------------------------------
    public final double GRND = 0;
    public final double PLTFM = 3; // temp

    public final double STONE_HEIGHT = 5;
    public final double STONE_LENGTH = 8;
    public final double STONE_WIDTH = 4;

    public int position = -1;

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

    // VUFORIA
    public final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    public final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";
    public VuforiaLocalizer vuforia = null;

    public Image rgbImage = null;
    public VuforiaLocalizer.CloseableFrame closeableFrame = null;

    // PID
    public PIDController pidDrive = new PIDController(2, 0, 0);
    public PIDController pidStrafe = new PIDController(2, 0, 0);
    public PIDController pidRotate = new PIDController(2, 0, 0);
    public PIDController pidDiagonal = new PIDController(2, 0, 0);
    public ElapsedTime pidTimer;

    // CONTROL CONSTANTS ---------------------------------------------------------------------------

    // TOGGLES
    public int a = 0, b = 0, x = 0, y = 0, up = 0, down = 0, left = 0, right = 0, rBumper = 0;

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;

    // DRIVE CONTROL
    public double slow;
    public double flpower, frpower, blpower, brpower;

    public final double WHEEL_DIAMETER_INCHES = 4;
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20
    public final double DRIVE_GEAR_REDUCTION = 1;
    public final double DRIVE_TICKS_PER_INCH = ((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES);

    public final double ROBOT_RADIUS_INCHES  = 9.933;
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public final double DRIVE_INCHES_PER_DEGREE  = ROBOT_CIRCUMFERENCE_INCHES / 360;
    // public final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // Y SLIDES
    public int yTargetInches = 0;

    public final double MAX_LEVEL = 8;
    public final double Y_MIN_EXTENSION = GRND;
    public final double Y_MAX_EXTENSION = PLTFM + MAX_LEVEL * STONE_HEIGHT;

    public final double Y_SPOOL_DIAMETER_INCHES = 50;
    public final double Y_SPOOL_CIRCUMFERENCE_INCHES = Y_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double Y_TICKS_PER_MOTOR_REV = 1120; // NeveRest Classic 40
    public final double Y_GEAR_REDUCTION = 1;
    public final double Y_TICKS_PER_INCH = ((Y_TICKS_PER_MOTOR_REV * Y_GEAR_REDUCTION) / Y_SPOOL_CIRCUMFERENCE_INCHES);

    // X SLIDES
    public final double X_MIN_EXTENSION = 0;
    public final double X_MAX_EXTENSION = STONE_LENGTH + 6; // temp

    public final double X_SPOOL_DIAMETER_INCHES = 50;
    public final double X_SPOOL_CIRCUMFERENCE_INCHES = X_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double x_TICKS_PER_MOTOR_REV = 560; // NeveRest Classic 20
    public final double X_GEAR_REDUCTION = 1;
    public final double X_TICKS_PER_INCH = ((x_TICKS_PER_MOTOR_REV * X_GEAR_REDUCTION) / X_SPOOL_CIRCUMFERENCE_INCHES);

    // CLAW
    public final double CLAW_DOWN = 0;
    public final double CLAW_UP = 1;

    // HOOK
    public final double HOOK_UP = 0.15;
    public final double HOOK_DOWN = 0.775;

    // TELEMTERY
    public boolean intakeIsLoaded;
    public boolean hookIsActivated;
    public boolean clawIsActivated;
    public double intakePower;

    // METHODS -------------------------------------------------------------------------------------

    // Initialize robot
    public void initialize() {

        //DEVICE INITIALIZATION

        telemetry.addLine("Initializing...");
        telemetry.update();

        // DRIVETRAIN
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // CLAW
        clawL = hardwareMap.servo.get("clawL");
        clawR = hardwareMap.servo.get("clawR");
        clawR.setDirection(Servo.Direction.REVERSE);

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

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        range = hardwareMap.get(DistanceSensor.class, "range");

        // PID -------------------------------------------------------------------------------------
        pidTimer = new ElapsedTime();
    }

    public void initializeVuforia() {

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
    }

    // Extend X-Slides and place stone
    public void stack() {

        // Checks if X-Slides are moving before stacking
        if (!xSlide.isBusy()) {

            // Place stone
            yRetract(1,PLTFM);
            openClaw();
        }
    }

    // Intake-ready position
    public void resetRobot() {

        openClaw();
        xRetract(1, getXPosInches() - X_MIN_EXTENSION);
        yRetract(1, getYPosInches() - Y_MIN_EXTENSION);
        intake();
    }

    // DRIVETRAIN
    public void forward(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, 1,
                    1, 1);
            setDriveMode();

            pidDrive.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidDrive.isFinished) {

                pidDrive.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidDrive.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void backward(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, -1,
                    -1, -1);
            setDriveMode();

            pidDrive.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidDrive.isFinished) {

                pidDrive.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidDrive.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeLeft(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    1, -1);
            setDriveMode();

            pidStrafe.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidStrafe.isFinished) {

                pidStrafe.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidStrafe.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void strafeRight(double inches) {

        double target = inches * DRIVE_TICKS_PER_INCH;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    -1, 1);
            setDriveMode();

            pidStrafe.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidStrafe.isFinished) {

                pidStrafe.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidStrafe.correction);
            }

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

                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    pidDiagonal.resetPID();

                    while (opModeIsActive() && frontLeft.isBusy() && backRight.isBusy() && !pidDiagonal.isFinished) {

                        pidDiagonal.performPID(getDrivePosition(), target, 1, pidTimer);
                        frontLeft.setPower(pidDiagonal.correction);
                        backRight.setPower(pidDiagonal.correction);
                    }
                }

                else if (quadrant == 2) {
                    backLeft.setTargetPosition((int)target);
                    frontRight.setTargetPosition((int)target);

                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    pidDiagonal.resetPID();

                    while (opModeIsActive() && (frontRight.isBusy() && backLeft.isBusy())) {

                        pidDiagonal.performPID(getDrivePosition(), target, 1, pidTimer);
                        frontLeft.setPower(pidDiagonal.correction);
                        backRight.setPower(pidDiagonal.correction);
                    }
                }

                else if (quadrant == 3) {
                    frontLeft.setTargetPosition((int)(-target));
                    backRight.setTargetPosition((int) (-target));

                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    pidDiagonal.resetPID();

                    while (opModeIsActive() && frontLeft.isBusy() && backRight.isBusy() && !pidDiagonal.isFinished) {

                        pidDiagonal.performPID(getDrivePosition(), target, 1, pidTimer);
                        frontLeft.setPower(pidDiagonal.correction);
                        backRight.setPower(pidDiagonal.correction);
                    }
                }

                else if (quadrant == 4) {
                    backLeft.setTargetPosition((int) (-target));
                    frontRight.setTargetPosition((int) (-target));

                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    pidDiagonal.resetPID();

                    while (opModeIsActive() && (frontRight.isBusy() && backLeft.isBusy())) {

                        pidDiagonal.performPID(getDrivePosition(), target, 1, pidTimer);
                        frontLeft.setPower(pidDiagonal.correction);
                        backRight.setPower(pidDiagonal.correction);
                    }
                }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateLeft(double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    1, -1,
                    1, -1);
            setDriveMode();

            pidRotate.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidRotate.isFinished) {

                pidRotate.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidRotate.correction);
            }

            stopDrive();
            resetDriveMode();
        }
    }

    public void rotateRight(double angle) {

        double target = angle * DRIVE_TICKS_PER_INCH * DRIVE_INCHES_PER_DEGREE;

        if (opModeIsActive()) {

            resetDriveEncoders();
            setDriveTarget(target,
                    -1, 1,
                    -1, 1);
            setDriveMode();

            pidRotate.resetPID();

            while (opModeIsActive() && driveIsBusy() && !pidRotate.isFinished) {

                pidRotate.performPID(getDrivePosition(), target, 1, pidTimer);
                setDrivePower(pidRotate.correction);
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

    public void yExtend(double power, double inches) {

        checkYTarget(inches);
        double ticks = inches * Y_TICKS_PER_INCH; // Convert to ticks
        runEncoder(ySlide, power, ticks);
    }

    public void yRetract(double power, double inches) {

        checkYTarget(-inches); // Negative distance (retract)
        double ticks = inches * Y_TICKS_PER_INCH; // Convert to ticks
        runEncoder(ySlide, power, ticks);
    }

    // X SLIDES

    public void xExtend() {

        xSlide.setPower(ON);
    }

    public void xRetract() {

        xSlide.setPower(REVERSE);
    }

    public void xOff() {

        xSlide.setPower(OFF);
    }

    public void xExtend(double power, double inches) {

        checkXTarget(inches);
        double ticks = inches * X_TICKS_PER_INCH; // Convert to ticks
        runEncoder(xSlide, power, ticks);
    }

    public void xRetract(double power, double inches) {

        checkYTarget(-inches); // Negative distance (Retract)
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

    // INCOMPLETE
    public void vuforiaScan(boolean saveBitmap) {

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

            cropStartX = 0;
            cropStartY = 0;
            cropWidth = (int)(0.5 * quarry.getWidth());
            cropHeight = (int)(0.5 * quarry.getHeight());

            telemetry.addLine("VuforiaScan\n"
                    + "Crop StartX: " + cropStartX + "\n"
                    + "Crop StartY: " + cropStartY + "\n"
                    + "Crop Width: " + cropStartY + "\n"
                    + "Crop Height: " + cropStartY + "\n"
                    + "Original Width: " + quarry.getWidth() + "\n"
                    + "Original Height: " + quarry.getHeight());
            telemetry.update();
            sleep(3000);

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

            int height, width, pixel;
            int bitmapWidth = quarry.getWidth(), bitmapHeight = quarry.getHeight();
            int r = -1, g = -1, b = -1;

            // From experimentation,
            // RGB of skystone-black is approximately(120, 120, 120)
            // RGB of stone-yellow is approximately (230, 200, 120)
            // Sole RGB of individual pixels not very reliable in determining skystone positiion

            // 1. Find the RGB sum of 9 pixels from each stone; lowest sum > skystone
            // 2. Experiment with HSV Values


            // Traverse through each row
            for (height = 0; height < bitmapHeight; height++) {

                for (width = 0; width < bitmapWidth; width++) {

                    pixel = quarry.getPixel(width, height);

                    if (Color.red(pixel) > r)
                        r = Color.red(pixel);

                    if (Color.green(pixel) > g)
                        g = Color.green(pixel);

                    if (Color.blue(pixel) > b)
                        b = Color.blue(pixel);
                }
            }

            telemetry.addLine("Max Red: " + r);
            telemetry.addLine("Max Green: " + g);
            telemetry.addLine("Max Blue: " + b);
            telemetry.update();
        }
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    public void runEncoder(DcMotor m_motor, double power, double ticks) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)ticks);

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy())
            m_motor.setPower(power);

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void checkXTarget(double inches) {

        // Find resulting poisiton after adding target position to current position (inches)
        double result = xSlide.getCurrentPosition() / X_TICKS_PER_INCH + inches;

        // Normalize if result is result is greater than max extension
        if (result > X_MAX_EXTENSION)
            // Set correct distance to max extension
            inches = (X_MAX_EXTENSION - xSlide.getCurrentPosition() / X_TICKS_PER_INCH);

        // Normalize if result is result is less than min extension
        else if (result < X_MIN_EXTENSION)
            // Set correct distance to min extension
            inches = (X_MIN_EXTENSION - xSlide.getCurrentPosition() / X_TICKS_PER_INCH);
    }

    public void checkYTarget(double inches) {

        // Find resulting poisiton after adding target position to current position (inches)
        double result = ySlide.getCurrentPosition() / Y_TICKS_PER_INCH + inches;

        // Normalize if result is result is greater than max extension
        if (result > Y_MAX_EXTENSION)
            // Set correct distance to max extension
            inches = (Y_MAX_EXTENSION - ySlide.getCurrentPosition() / Y_TICKS_PER_INCH);

            // Normalize if result is result is less than min extension
        else if (result < Y_MIN_EXTENSION)
            // Set correct distance to min extension
            inches = (Y_MIN_EXTENSION - ySlide.getCurrentPosition() / Y_TICKS_PER_INCH);
    }

    public double getXPosInches() {

        return xSlide.getCurrentPosition() / X_TICKS_PER_INCH;
    }

    public double getYPosInches() {

        return ySlide.getCurrentPosition() / Y_TICKS_PER_INCH;
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

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        return Math.toDegrees(theta.firstAngle);
    }
}
