package org.firstinspires.ftc.teamcode.States.R3Gen3;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
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
import org.firstinspires.ftc.teamcode.States.PIDController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.graphics.Bitmap.*;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

// X-Slides are commented

public abstract class R3Gen3Superclass extends LinearOpMode {

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

    // Y-SLIDES
    public DcMotor ySlideL, ySlideR;

    // X-SLIDES
    public Servo xSlide, clawGrabber, clawRotate, hookL, hookR, capper;

    // INTAKE
    public DcMotor intakeL, intakeR;

    // REV SENSORS
    public BNO055IMU imu;
    public Orientation theta;

    // PID
    public ElapsedTime pidTimer;
    PIDController pidGyroTurn = new PIDController(0,0,0);

    // VUFORIA
    public final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    public final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";
    public VuforiaLocalizer vuforia = null;

    public Image rgbImage = null;
    public VuforiaLocalizer.CloseableFrame closeableFrame = null;

    // CONTROL CONSTANTS ---------------------------------------------------------------------------

    // GENERAL CONTROL
    public final double ON = 1;
    public final double OFF = 0;
    public final double REVERSE = -1;
    public int skyStonePos = -1;

    // TOGGLES
    public int intakeToggle = 0, a = 0, b = 0;

    // DRIVETRAIN

    public double flpower, frpower, blpower, brpower;
    public double slow;

    public final double WHEEL_DIAMETER_INCHES = 4;
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20
    public final double DRIVE_GEAR_REDUCTION = 1;
    public final double DRIVE_TICKS_PER_INCH = (((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES));

    public final double ROBOT_RADIUS_INCHES  = 11; // temp
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public final double DRIVE_INCHES_PER_DEGREE  = (ROBOT_CIRCUMFERENCE_INCHES / 360.0); // temp

    // LINEAR SLIDES
    public final double X_MAX_EXTENSION = 0.5;
    public final double X_MIN_EXTENSION = 0;

    // CLAW
    public final double open = 1;
    public final double close = 0;
    public final double out = 1;
    public final double in = 0;

    // HOOK
    public final double up = 0;
    public final double down = 1;

    // CAPPER
    public final double set = 1;
    public final double reset = 0;

    // METHODS -------------------------------------------------------------------------------------

    public void initialize() {

        //DEVICE INITIALIZATION

        telemetry.addLine("Initializing Robot...");
        telemetry.update();

        // UNIVERSAL VARIABLES
        TeleOpTime = new ElapsedTime();

        // DRIVETRAIN
        frontLeft = (DcMotorEx)hardwareMap.dcMotor.get("frontLeft");
        frontRight = (DcMotorEx)hardwareMap.dcMotor.get("frontRight");
        backLeft = (DcMotorEx)hardwareMap.dcMotor.get("backLeft");
        backRight = (DcMotorEx)hardwareMap.dcMotor.get("backRight");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        resetDriveEncoders();
        resetDriveMode();

        // LINEAER SLIDES
        ySlideL = (DcMotorEx)hardwareMap.dcMotor.get("ySlideL");
        ySlideR = (DcMotorEx)hardwareMap.dcMotor.get("ySlideR");
        ySlideL.setDirection(DcMotor.Direction.REVERSE);
        xSlide = hardwareMap.servo.get("xSlide");

        // CLAW
        clawGrabber = hardwareMap.servo.get("clawGrabber");
        clawRotate = hardwareMap.servo.get("clawRotate");

        // HOOK
        hookL = hardwareMap.servo.get("hookL");
        hookR = hardwareMap.servo.get("hookR");
        hookR.setDirection(Servo.Direction.REVERSE);

        // CAPPER
        capper = hardwareMap.servo.get("capper");

        // INTAKE
        intakeL = (DcMotorEx)hardwareMap.dcMotor.get("intakeL");
        intakeR = (DcMotorEx)hardwareMap.dcMotor.get("intakeR");
        intakeL.setDirection(DcMotor.Direction.REVERSE);

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);

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

    public void gyroTurn(double pow, double angle) {

        while(Math.abs(getHeading() - angle) > 3 && opModeIsActive()) {

            if(getHeading() > angle)
                leftTurnPower(pow);

            else if (getHeading() < angle)
                rightTurnPower(pow);
        }

        stopDrive();
        resetDriveMode();
    }

    public void gyroTurnPID(double angle) {

        if (opModeIsActive()) {

            pidGyroTurn.resetPID();

            while (opModeIsActive() && !pidGyroTurn.isFinished) {

                pidGyroTurn.performPID(getHeading(), angle, 3, pidTimer);

                if (getHeading() > angle)
                    leftTurnPower(pidGyroTurn.correction);
                else if ( getHeading() < angle)
                    rightTurnPower(pidGyroTurn.correction);
            }

            stopDrive();
            resetDriveMode();
        }
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

            /*
            cropStartX = (int)(0.3125 * quarry.getWidth());
            cropStartY = 0;
            cropWidth = (int)(0.06 * quarry.getWidth());
            cropHeight = (int)(1.0 * quarry.getHeight());

            // Create cropped bitmap to show only stones
            quarry = createBitmap(quarry, cropStartX, cropStartY, cropWidth, cropHeight);
            */

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

            /*
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
            */
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

    // LINEAR SLIDES

    public void extendY() {
        ySlideL.setPower(ON);
        ySlideR.setPower(ON);
    }

    public void retractY() {
        ySlideL.setPower(REVERSE);
        ySlideR.setPower(REVERSE);
    }

    public void yOff() {
        ySlideL.setPower(OFF);
        ySlideR.setPower(OFF);
    }

    public void extendX() {
        xSlide.setPosition(X_MAX_EXTENSION);
    }

    public void retractX() {
        xSlide.setPosition(X_MIN_EXTENSION);
    }

    // INTAKE

    public void intakeOn() {
        intakeL.setPower(ON);
        intakeR.setPower(ON);
    }

    public void intakeReverse() {
        intakeL.setPower(REVERSE);
        intakeR.setPower(REVERSE);
    }

    public void intakeOff() {
        intakeL.setPower(OFF);
        intakeR.setPower(OFF);
    }

    // CLAW
    public void openClaw() {
        clawGrabber.setPosition(open);
    }

    public void closeClaw() {
        clawGrabber.setPosition(close);
    }

    public void flipClawOut() {
        clawRotate.setPosition(out);
    }

    public void flipClawIn() {
        clawRotate.setPosition(in);
    }

    // HOOK

    public void hookUp() {
        hookL.setPosition(up);
        hookR.setPosition(up);
    }

    public void hookDown() {
        hookL.setPosition(down);
        hookR.setPosition(down);
    }

    // CAPPER

    public void setCapstone() {
        capper.setPosition(set);
    }

    public void resetCapstone() {
        capper.setPosition(reset);
    }

    // UTILITY METHODS -----------------------------------------------------------------------------

    // DRIVETRAIN

    public void resetDriveEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public boolean driveIsBusy() {

        if (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())
            return true;
        else
            return false;
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

    public double getDrivePosition() {

        double total = frontLeft.getCurrentPosition() + frontRight.getCurrentPosition()
                + backLeft.getCurrentPosition() + backRight.getCurrentPosition();
        return total/4.0;
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

    // TELEMETRY AND DATA

    public double getHeading() {

        // Returns robot heading in 0-360 degrees

        double robotHeading;

        theta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        if (theta.firstAngle < 0)
            robotHeading = theta.firstAngle * -1;

        else if (theta.firstAngle > 0) {
            robotHeading = 360 - theta.firstAngle;
        }

        else
            robotHeading = 0;

        return robotHeading;
    }

    public boolean isEndgame() {
        if (TeleOpTime.time(TimeUnit.MINUTES) > 2)
            return true;
        else
            return false;
    }

    public void displayTelemetry() {

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Global Angle: " + theta.firstAngle);
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition() + "\t\tFR: " + frontRight.getCurrentPosition());
        telemetry.addLine("FL: " + backLeft.getCurrentPosition() + "\t\tFR: " + backRight.getCurrentPosition());
        telemetry.addLine("X-Slide Position: " + xSlide.getPosition());
        telemetry.addLine("Claw Grabber: " + clawGrabber.getPosition());
        telemetry.addLine("Claw Rotate: " + clawRotate.getPosition());
        telemetry.addLine("Hook" + hookR.getPosition());

        telemetry.update();
    }
}