package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.transition.Slide;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Bitmap.*;
import static org.firstinspires.ftc.teamcode.Utilities.ControlConstants.*;
import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.*;
import static org.firstinspires.ftc.teamcode.Utilities.UniversalVariables.*;

public abstract class Skystone10022Superclass extends LinearOpMode {

    // Initialize robot
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

        // REV Sensors
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        range = hardwareMap.get(DistanceSensor.class, "range");

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

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); // Enables RGB565 format for image
        vuforia.setFrameQueueCapacity(1); // Store only one frame at a time

        // PID ControlConstants
        pidRotate = new PIDController(1,1,1);
        pidDrive = new PIDController(0,0,0);
        pidXSlides = new PIDController(0,0,0);
        pidYSlides = new PIDController(0,0,0);
    }

    // Extend X-Slides and place stone
    public void stack() {

        // Checks if X-Slides are moving before stacking
        if (!xSlide.isBusy()) {

            // Place stone
            yRetract(1,PLTFM);
            openClamp();
            yExtend(1,PLTFM);
        }
    }

    // Intake-ready position
    public void resetRobot() {

        openClamp();
        xRetract(1, getXPosInches() - X_MIN_EXTENSION);
        yRetract(1, getYPosInches() - Y_MIN_EXTENSION);
        intake();
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

    // X SLIDES ------------------------------------------------------------------------------------
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

    // VUFORIA -------------------------------------------------------------------------------------

    // INCOMPLETE
    public int vuforiaScan(boolean saveBitmap) {

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

                    File file = new File(path, "Bitmap");
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

            /*
            // Create cropped bitmap to show only stones
            quarry = createBitmap(quarry, cropStartX, cropStartY, cropWidth, cropHeight);

            // Save cropped bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, "CroppedBitmap");
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
             */

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
            sleep(20000);
        }

        return position = 999;
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

    public void setRotateTarget(double deg, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * deg));
        backLeft.setTargetPosition((int) (bl * deg));
        frontRight.setTargetPosition((int) (fr * deg));
        backRight.setTargetPosition((int) (br * deg));
    }
}
