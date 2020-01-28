package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
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

import static android.graphics.Bitmap.*;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public abstract class Skystone10022Superclass extends LinearOpMode {

    // UNIVERSAL VARIABLES -------------------------------------------------------------------------
    public final double GRND = 0;
    public final double PLTFM = 3; // temp

    public final double STONE_HEIGHT = 5;
    public final double STONE_LENGTH = 8;
    public final double STONE_WIDTH = 4;
    public final double STACK_CLEARANCE = 1; // temp

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
    public int a = 0, b = 0, x = 0, y = 0, up = 0, down = 0, left = 0, right = 0, rBumper = 0, back = 0;

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;
    public int skyStonePos = -1;

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
    public final double DRIVE_INCHES_PER_DEGREE  = ROBOT_CIRCUMFERENCE_INCHES / 360; // temp
    // public final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // Y SLIDES
    public double yTargetInches = 0;
    public int yLevel = 0;

    public final double MAX_LEVEL = 8;
    public final double Y_MIN_EXTENSION = GRND;
    public final double Y_MAX_EXTENSION = PLTFM + STACK_CLEARANCE + MAX_LEVEL * STONE_HEIGHT;

    public final double Y_SPOOL_DIAMETER_INCHES = 2;
    public final double Y_SPOOL_CIRCUMFERENCE_INCHES = Y_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double Y_TICKS_PER_MOTOR_REV = 1680; // NeveRest Classic 60
    public final double Y_GEAR_REDUCTION = 1;
    public final double Y_TICKS_PER_INCH = ((Y_TICKS_PER_MOTOR_REV * Y_GEAR_REDUCTION) / Y_SPOOL_CIRCUMFERENCE_INCHES);

    // X SLIDES
    public final double X_MIN_EXTENSION = 0;
    public final double X_MAX_EXTENSION = 6.5; // temp

    public final double X_SPOOL_DIAMETER_INCHES = 2;
    public final double X_SPOOL_CIRCUMFERENCE_INCHES = X_SPOOL_DIAMETER_INCHES * Math.PI;
    public final double X_TICKS_PER_MOTOR_REV = 560; // NeveRest Classic 20
    public final double X_GEAR_REDUCTION = 1;
    public final double X_TICKS_PER_INCH = (((X_TICKS_PER_MOTOR_REV * X_GEAR_REDUCTION) / X_SPOOL_CIRCUMFERENCE_INCHES)*(2.5/1.5));

    public String stackState = "";

    // CLAW
    public final double CLAW_DOWN = 0.6;
    public final double CLAW_UP = 1;

    // HOOK
    public final double HOOK_UP = 0;
    public final double HOOK_DOWN = 0.95;

    // TELEMTERY
    public boolean intakeIsLoaded;
    public boolean hookIsActivated;
    public boolean clawIsActivated;
    public double intakePower;

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
        hookDown();

        // LINEAR SLIDES
        xSlide = hardwareMap.dcMotor.get("xSlide");
        ySlide = hardwareMap.dcMotor.get("ySlide");
        xSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        ySlide.setDirection(DcMotorSimple.Direction.REVERSE);
        resetSlideEncoders();
        resetSlideMode();

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        range = hardwareMap.get(DistanceSensor.class, "range");

        // PID -------------------------------------------------------------------------------------
        pidTimer = new ElapsedTime();

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

        telemetry.addLine("Vuforia Initialized");
        telemetry.update();
    }

    // Extend X-Slides and place stone
    public void stack() {

        // Checks if X-Slides are moving before stacking
        if (!xSlide.isBusy()) {

            // Place stone
            yExtend(1,-STACK_CLEARANCE);
            openClaw();
        }
    }

    // Intake-ready position
    public void resetRobot() {

        openClaw();
        xExtend(1, -(getXPosInches() - X_MIN_EXTENSION));
        yExtend(1, -(getYPosInches() - Y_MIN_EXTENSION));
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

        double ticks = checkYTarget(inches) * Y_TICKS_PER_INCH; // Convert to ticks
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

        double ticks = checkXTarget(inches) * X_TICKS_PER_INCH; // Convert to ticks
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

            int height, width, pixel, tempColorSum;
            int bitmapWidth = quarry.getWidth(), bitmapHeight = quarry.getHeight();
            int colorSum = 1000;
            int row;
            width = (int)(0.3425 * quarry.getWidth());      // temp ish -- does this need to be adjusted?
            double factor = 1.0 / 12;

            // check each of the 3 stones
            for (int currentStone = 4; currentStone <= 6; currentStone++) {

                height = (int) (quarry.getHeight() * factor);    // temp

                // 'pixel' uses a constant width and a variable height
                pixel = quarry.getPixel(width, height);

                tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);

                telemetry.addLine("factor#: " + factor);
                telemetry.addLine("quarry.getHeight(): " + quarry.getHeight());
                telemetry.addLine("height: " + height);
                telemetry.addLine("width: " + width);
                /*telemetry.addLine("red: " + Color.green(pixel));
                telemetry.addLine("green: " + Color.red(pixel));
                telemetry.addLine("blue: " + Color.blue(pixel));*/

                telemetry.addLine();
                telemetry.addLine("stone #: " + currentStone);
                /*
                telemetry.addLine("temp color sum: " + tempColorSum);
                telemetry.addLine("min color sum: " + colorSum);
                telemetry.addLine("skystone: " + skyStonePos); */
                telemetry.update();
                sleep(2000);

                if (tempColorSum < colorSum) {

                    colorSum = tempColorSum;
                    skyStonePos = currentStone;
                }

                factor += 1.0 / 3;
            }

            // right now,
            // this method ONLY WORKS IF correct starting posiiton on blue side
            // easy to correct I just haven't done it yet

            // From experimentation,
            // RGB of skystone-black is approximately(120, 120, 120)
            // RGB of stone-yellow is approximately (230, 200, 120)
            // Sole RGB of individual pixels not very reliable in determining skystone positiion

            telemetry.addLine("color sum: " + colorSum);
            telemetry.addLine("skystone: " + skyStonePos);

            telemetry.update();
        }
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    public void runEncoder(DcMotor m_motor, double power, double ticks) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)ticks);

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy()) {
            teleOp();
            m_motor.setPower(power);
        }

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double checkXTarget(double inches) {

        // Find resulting poisiton after adding target position to current position (inches)
        double result = getXPosInches() + inches;

        // Normalize if result is result is greater than max extension
        if (result > X_MAX_EXTENSION || result < X_MIN_EXTENSION)
            // Set correct distance to max extension
            inches = (X_MAX_EXTENSION - getXPosInches());

        return inches;
    }

    public double checkYTarget(double inches) {

        // Find resulting position after adding target position to current position (inches)
        double result = getYPosInches() + inches;

        // Normalize if result is result is greater than max extension or less than min extension
        if (result > Y_MAX_EXTENSION || result < Y_MIN_EXTENSION)
            // Set correct distance to max/min extension
            inches = (Y_MAX_EXTENSION - getYPosInches());

        return inches;
    }

    public double getXPosInches() {

        return xSlide.getCurrentPosition() / X_TICKS_PER_INCH;
    }

    public double getYPosInches() {

        return ySlide.getCurrentPosition() / Y_TICKS_PER_INCH;
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

    public void teleOp() {

        // TELEMETRY ---------------------------------------------------------------------------
        // Display control info for the driver

        telemetry.addLine("STACK AND LEVELLING: ");
        telemetry.addLine("Y-Target, Level: " + yTargetInches + ", " + yLevel);
        telemetry.addLine("Stack State: " + stackState);
        telemetry.addLine();

        telemetry.addLine("INTAKE: ");
        telemetry.addLine("Intake Loaded: " + intakeIsLoaded);
        telemetry.addLine("Intake Power: " + intakePower);
        telemetry.addLine();

        telemetry.addLine("CLAW AND HOOK:");
        telemetry.addLine("Claw Activated: " + clawIsActivated);
        telemetry.addLine("Hook Activated " + hookIsActivated);
        telemetry.addLine();

        telemetry.addLine("LINEAR SLIDES:");
        telemetry.addLine("X-Slide Position: " + getXPosInches());
        telemetry.addLine("Y-Slide Position: " + getYPosInches());
        telemetry.addLine();

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Global Angle: " + getHeading());
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition());
        telemetry.addLine("FR: " + frontRight.getCurrentPosition());
        telemetry.addLine("BL: " + backLeft.getCurrentPosition());
        telemetry.addLine("BR: " + backRight.getCurrentPosition());
        telemetry.addLine("X-Slide: " + xSlide.getCurrentPosition());
        telemetry.addLine("Y-Slide: " + ySlide.getCurrentPosition());
        telemetry.update();

        // SIGNALLING ------------------------------------------------------------------------------

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
        if (gamepad1.right_trigger > 0.9) {

            flpower /= 5;
            frpower /= 5;
            blpower /= 5;
            brpower /= 5;
        }

        else if (gamepad1.right_trigger > 0.1) {

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

        // ROTATE ------------------------------------------------------------------------------
        if (gamepad1.y && y == 0)
            y = 1;

        else if (!gamepad1.y && y == 1) {
            rotateRight(180);
            y = 0;
        }

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

        // Y SLIDES ----------------------------------------------------------------------------

        // Every Dpad_Up press increases level until max level
        // Dpad_Right press runs slides
        if (yTargetInches < Y_MAX_EXTENSION) {

            if (gamepad1.dpad_up && up == 0)
                up = 1;

            else if (!gamepad1.dpad_up && up == 1) {

                // Add stone height to target
                yTargetInches += STONE_HEIGHT;
                yLevel++;

                // If stone is level one, add platform and clearance height
                if (yTargetInches == STONE_HEIGHT)
                    yTargetInches += PLTFM + STACK_CLEARANCE;

                up = 0;
            }
        }

        // Every Dpad_Down press decreases level until min level
        if (yTargetInches > -Y_MAX_EXTENSION) {

            if (gamepad1.dpad_down && down == 0)
                down = 1;

            else if (!gamepad1.dpad_down && down == 1) {

                yTargetInches -= STONE_HEIGHT;
                yLevel--;

                // If stone was level one, subtract platfrom and clearance height
                if (yTargetInches == PLTFM + STACK_CLEARANCE)
                    yTargetInches = 0;

                // If stone was negative max level, subtract platform and clearance height
                if (yTargetInches == -(STONE_HEIGHT * MAX_LEVEL))
                    yTargetInches = -Y_MAX_EXTENSION;

                down = 0;
            }
        }

        // CLAW-INTAKE -----------------------------------------------------------------------------

        if(range.getDistance(DistanceUnit.INCH) < 0.8) {

            intakeIsLoaded = true;
            intakeOff();
            closeClaw();
        }

        else {

            intakeIsLoaded = false;
            intake();
            openClaw();
        }
    }
}
