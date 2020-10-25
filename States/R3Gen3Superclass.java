package org.firstinspires.ftc.teamcode.States.R3Gen3;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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
    public DcMotorEx frontLeft, frontRight, backLeft, backRight;

    // Y-SLIDES
    public DcMotorEx ySlideL, ySlideR;

    // X-SLIDES
    public Servo xSlide, hookL, hookR, capper; // ziptie

    // CLAW
    public Servo clawGrabber, clawRotate;

    //Pusher
    public Servo pusher;

    // INTAKE
    public DcMotor intakeL, intakeR;

    // REV SENSORS
    public BNO055IMU imu;
    public Orientation theta;
    public DistanceSensor range;

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
    public boolean dualControl = false;

    // TOGGLES
    public int intakeToggle = 0, x = 0, b = 0, a  = 0, y = 0, r = 0, back = 0;

    // DRIVETRAIN

    public double flpower, frpower, blpower, brpower;
    public double driveSlow, verticalSlow;

    public final double WHEEL_DIAMETER_INCHES = 4;
    public final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20
    public final double DRIVE_GEAR_REDUCTION = 1;
    public final double DRIVE_TICKS_PER_INCH = (((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES));

    public final double ROBOT_RADIUS_INCHES  = 11; // temp
    public final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public final double DRIVE_INCHES_PER_DEGREE  = (ROBOT_CIRCUMFERENCE_INCHES / 360.0); // temp

    // LINEAR SLIDES
    public final double X_MAX_EXTENSION = 0.3;
    public final double X_MIN_EXTENSION = 1.0;

    // CLAW
    public final double open = 0.44;
    public final double close = 1;
    public final double out = 0.8;
    public final double sideways = 0.4;
    public final double in = 0.065;

    // HOOK
    public final double up = 0.85;
    public final double down = 0.15;

    // CAPPER
    public final double set = 0.7;
    public final double reset = 1.0;

    // PUSHER
    public final double retract = 0.5;
    public final double extend = 0.1;

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
        openClaw();
        flipClawIn();

        // HOOK
        hookL = hardwareMap.servo.get("hookL");
        hookR = hardwareMap.servo.get("hookR");
        hookR.setDirection(Servo.Direction.REVERSE);
        hookUp();

        // CAPPER
        capper = hardwareMap.servo.get("capper");
        resetCapstone();

        // PUSHER
        pusher = hardwareMap.servo.get("pusher");
        retractPusher();

        // INTAKE
        intakeL = (DcMotorEx)hardwareMap.dcMotor.get("intakeL");
        intakeR = (DcMotorEx)hardwareMap.dcMotor.get("intakeR");
        intakeL.setDirection(DcMotor.Direction.REVERSE);

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        range = hardwareMap.get(DistanceSensor.class, "range");

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

    // red side vuforia
    public void vuforiaScanRed(boolean saveBitmap) {

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

            int quarryWidth, quarryHeight;
            quarryWidth = quarry.getWidth();
            quarryHeight = quarry.getHeight();

            cropStartX = (int) (quarryWidth * 20.0 / 69.5);     // x initial | max: 31.5 original 26.0 / 69.5
            cropStartY = (int) (quarryHeight * 13.0 / 39.0);    // y initial | max: 33.0 original 13.0 / 39.0
            cropWidth = (int) (quarryWidth * 38.0 / 69.5);      // delta x
            cropHeight = (int) (quarryHeight * 6.0 / 39.0);     // delta y

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

            int yPos, xPos, pixel, tempColorSum;
            int bitmapWidth = quarry.getWidth();
            int bitmapHeight = quarry.getHeight();
            int maxColor = 1000;
            double factor = 0;              // moves yPos based on which stone
            double difference = 1.0 / 12;   // moves yPos based on the point on the stone

            yPos = (int) (bitmapHeight / 5);

            // cycles through the three stones furthest from the wall
            for (int currentStone = 4; currentStone <= 6; currentStone++) {

                // cycles through 3 points on the stone
                for (int i = 1; i <= 3; i++) {

                    // set yPosition
                    xPos = (int) (bitmapWidth * (factor + difference * i));

                    // gets the pixel
                    pixel = quarry.getPixel(xPos, yPos);

                    // finds the color sum of that pixel
                    tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);

                    // if it's the darkest pixel yet, it will set the skyStonePos to the currentStone
                    if (tempColorSum < maxColor) {

                        maxColor = tempColorSum;
                        skyStonePos = currentStone;
                    }
                }

                // updates factor so the next cycle will look at pixels on the next stone
                factor += 1.0 / 3.0;
            }

            telemetry.addLine("Position: " + skyStonePos);
            telemetry.update();
        }
    }

    // blue side vuforia
    public void vuforiaScanBlue(boolean saveBitmap) {

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

            int quarryWidth, quarryHeight;
            quarryWidth = quarry.getWidth();
            quarryHeight = quarry.getHeight();

            cropStartX = (int) (quarryWidth * 13.5 / 69.5);     // x initial | max: 30.5  // original 12.0 / 69.5
            cropStartY = (int) (quarryHeight * 14.75 / 39.0);     // y initial | max: 33.5  // original 8.0 / 39.0
            cropWidth = (int) (quarryWidth * 39.0 / 69.5);      // delta x
            cropHeight = (int) (quarryHeight * 5.5 / 39.0);     // delta y

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

            int yPos, xPos, pixel, tempColorSum;
            int bitmapWidth = quarry.getWidth();
            int bitmapHeight = quarry.getHeight();
            int maxColor = 1000;
            double factor = 0;              // moves yPos based on which stone
            double difference = 1.0 / 12;   // moves yPos based on the point on the stone

            yPos = (int) (bitmapHeight / 2);

            // cycles through the three stones furthest from the wall
            for (int currentStone = 4; currentStone <= 6; currentStone++) {

                // cycles through 3 points on the stone
                for (int i = 1; i <= 3; i++) {

                    // set yPosition
                    xPos = (int) (bitmapWidth * (factor + difference * i));

                    // gets the pixel
                    pixel = quarry.getPixel(xPos, yPos);

                    // finds the color sum of that pixel
                    tempColorSum = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);

                    // if it's the darkest pixel yet, it will set the skyStonePos to the currentStone
                    if (tempColorSum < maxColor) {

                        maxColor = tempColorSum;
                        skyStonePos = currentStone;
                    }
                }

                // updates factor so the next cycle will look at pixels on the next stone
                factor += 1.0 / 3.0;
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

    public void extendY(double verticalSlow) {
        ySlideL.setPower(verticalSlow);
        ySlideR.setPower(verticalSlow);
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

    public void flipClawSideways() {
        clawRotate.setPosition(sideways);
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

    // PUSHER

    public void extendPusher() {
        pusher.setPosition(extend);
    }

    public void retractPusher() {
        pusher.setPosition(retract);
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
    public void runEncoder(DcMotor m_motor, double power, double ticks) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)ticks);

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy()) {

            doTeleOp();

            m_motor.setPower(power);
        }

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void liftEncoder(double power, double ticks) {

        ySlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ySlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ySlideR.setTargetPosition((int)ticks);
        ySlideL.setTargetPosition((int)ticks);

        ySlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ySlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (ySlideL.isBusy() && ySlideR.isBusy()) {

            doTeleOp();

            ySlideL.setPower(power);
            ySlideR.setPower(power);
        }

        // Stop all motion;
        ySlideL.setPower(0);
        ySlideR.setPower(0);
        // Turn off RUN_TO_POSITION
        ySlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ySlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void liftHoldEncoder(double power, double ticks, boolean state) {

        ySlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ySlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ySlideR.setTargetPosition((int)ticks);
        ySlideL.setTargetPosition((int)ticks);

        ySlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ySlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (ySlideL.isBusy() && ySlideR.isBusy() && state) {

            doTeleOp();

            ySlideL.setPower(power);
            ySlideR.setPower(power);
        }

        // Stop all motion;
        ySlideL.setPower(0);
        ySlideR.setPower(0);
        // Turn off RUN_TO_POSITION
        ySlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ySlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void doTeleOp(){

        // DRIVETRAIN --------------------------------------------------------------------------

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
        frpower = Math.pow((lefty -leftx - rightx), 3);
        brpower = Math.pow((lefty + leftx - rightx), 3);

        // Motor Power is halved while the right trigger is held down to allow for more
        // precise robot control
        if (gamepad1.right_trigger > 0.8) {

            flpower /= 3;
            frpower /= 3;
            blpower /= 3;
            brpower /= 3;
        }

        else if (gamepad1.right_trigger > 0.1) {

            driveSlow = -0.8 * gamepad1.right_trigger + 1;

            flpower *= driveSlow;
            frpower *= driveSlow;
            blpower *= driveSlow;
            brpower *= driveSlow;
        }

        // Set Motor Powers
        frontLeft.setPower(flpower);
        backLeft.setPower(blpower);
        frontRight.setPower(frpower);
        backRight.setPower(brpower);

        // LINEAR SLIDES -----------------------------------------------------------------------

            /*if (gamepad1.left_trigger > 0.1)
                verticalSlow = -0.8 * gamepad1.left_trigger + 1;
            else
                verticalSlow = 0;

            if (gamepad1.dpad_up)
                extendY();
            else if (gamepad1.dpad_down)
                retractY();
            else
                yOff();


            if (gamepad1.dpad_right)
                extendX();
            else if (gamepad1.dpad_left)
                retractX();

            // CAPSTONE
            if (gamepad1.a && a == 0)
                a = 1;
            else if (!gamepad1.a && a == 1) {
                setCapstone();
                a = 2;
            }
            else if (gamepad1.a && a == 2)
                a = 3;
            else if (!gamepad1.a && a == 3) {
                resetCapstone();
                a = 0;
            }


            // CLAW ACTIVATE

            if (range.getDistance(DistanceUnit.CM) < 0.5) {
                closeClaw();
                x = 2;
                intakeOff();
                intakeToggle = 0;
            }

            if (gamepad1.x && x == 0)
                x = 1;
            else if (!gamepad1.x && x == 1) {
                closeClaw();
                x = 2;
            }
            else if (gamepad1.x && x == 2)
                x = 3;
            else if (!gamepad1.x && x == 3) {
                openClaw();
                x = 0;
            }

            // CLAW ROTATE

            if (gamepad1.y && y == 0)
                y = 1;
            else if (!gamepad1.y && y == 1) {
                flipClawOut();
                y = 2;
            }
            else if (gamepad1.y && y == 2)
                y = 3;
            else if (!gamepad1.y && y == 3) {
                flipClawIn();
                y = 0;
            }

            if (gamepad1.back && back == 0)
                back = 1;
            else if (!gamepad1.back && back == 1) {
                y = 0;
                flipClawSideways();
                back = 0;
            }

            // HOOK

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

            // INTAKE ------------------------------------------------------------------------------

            if (gamepad1.right_bumper && (intakeToggle == 4 || intakeToggle == 0))
                intakeToggle = 1;
            else if (!gamepad1.right_bumper && intakeToggle == 1) {
                intakeOn();
                intakeToggle = 2;
            }
            // REVERSE INTAKE
            else if (gamepad1.left_bumper && (intakeToggle == 0 || intakeToggle == 2))
                intakeToggle = 3;
            else if (!gamepad1.left_bumper && intakeToggle == 3) {
                intakeReverse();
                intakeToggle = 4;
            }
            // OFF
            else if ((gamepad1.right_bumper && intakeToggle == 2) || (gamepad1.left_bumper && intakeToggle == 4))
                intakeToggle = 5;
            else if(!gamepad1.right_bumper && !gamepad1.left_bumper && intakeToggle == 5) {
                intakeOff();
                intakeToggle = 0;
            }*/

        getHeading();
        displayTelemetry();

    }

    public void displayTelemetry() {

        telemetry.addLine("IMU AND ENCODERS:");
        telemetry.addLine("Distance: " + range.getDistance(DistanceUnit.CM));
        // telemetry.addLine("Global Angle: " + theta.firstAngle);
        telemetry.addLine("FL: " + frontLeft.getCurrentPosition() + "\t\tFR: " + frontRight.getCurrentPosition());
        telemetry.addLine("FL: " + backLeft.getCurrentPosition() + "\t\tFR: " + backRight.getCurrentPosition());
        telemetry.addLine("X-Slide Position: " + xSlide.getPosition());
        telemetry.addLine("Claw Grabber: " + clawGrabber.getPosition());
        telemetry.addLine("Claw Rotate: " + clawRotate.getPosition());
        telemetry.addLine("Hook" + hookR.getPosition());

        telemetry.update();
    }
}
