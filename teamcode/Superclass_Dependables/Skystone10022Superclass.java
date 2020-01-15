package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);

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
    public void yExtend(double power, int inches) {

        double target = inches * Y_TICKS_PER_INCH;

        runEncoder(ySlide, power, target);
    }

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

        double target = inches * X_TICKS_PER_INCH;

        if (target > X_MAX_EXTENSION)
            target = X_MAX_EXTENSION;

        runEncoder(xSlide, power, target);
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

    // VUFORIA -------------------------------------------------------------------------------------
    public int vuforiaScan(boolean saveBitmap, boolean redAlliance) {

        // Credit to Drew Kinneer of FTC 10435 Circuit Breakers

        int pos = -1;

        Image rgbImage = null;

        double yellowCountL = 1;
        double yellowCountC = 1;
        double yellowCountR = 1;

        double blackCountL = 1;
        double blackCountC = 1;
        double blackCountR = 1;

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); // Enables RGB565 format for image
        VuforiaLocalizer.CloseableFrame closeableFrame = null;
        vuforia.setFrameQueueCapacity(1); // Store only one frame at a time

        while (rgbImage == null) {

            try {

                // Turn on flashlight
                CameraDevice.getInstance().setFlashTorchMode(true);

                closeableFrame = vuforia.getFrameQueue().take();
                long numImages = closeableFrame.getNumImages();

                for (int i = 0; i < numImages; i++) {

                    if (closeableFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {

                        rgbImage = closeableFrame.getImage(i);

                        if (rgbImage != null) {

                            // Turn off flashlight
                            CameraDevice.getInstance().setFlashTorchMode(false);
                            break;
                        }
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

            // Set File Name
            String bitmapName;
            String croppedBitmapName;

            if (redAlliance) {
                bitmapName = "BitmapRED.png";
                croppedBitmapName = "BitmapCroppedRED.png";

            } else {
                bitmapName = "BitmapBLUE.png";
                croppedBitmapName = "BitmapCroppedBLUE.png";
            }

            // Save Bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, bitmapName);
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
            int cropStartX;
            int cropStartY;
            int cropWidth;
            int cropHeight;

            if (redAlliance) {
                // Temp Dimensions
                cropStartX = (int) ((140.0 / 720.0) * quarry.getWidth());
                cropStartY = (int) ((100.0 / 480.0) * quarry.getHeight());
                cropWidth = (int) ((550.0 / 720.0) * quarry.getWidth());
                cropHeight = (int) ((130.0 / 480.0) * quarry.getHeight());

            } else {
                // Temp Dimensions
                cropStartX = (int) ((370.0 / 1280.0) * quarry.getWidth());
                cropStartY = (int) ((170.0 / 720.0) * quarry.getHeight());
                cropWidth = (int) ((890.0 / 1280.0) * quarry.getWidth());
                cropHeight = (int) ((125.0 / 720.0) * quarry.getHeight());
            }

            telemetry.addLine("VuforiaScan \n"
                    + "Crop StartX: " + cropStartX
                    + "Crop StartY: " + cropStartY
                    + "Crop Width: " + cropStartY
                    + "Crop Height: " + cropStartY
                    + "Original Width: " + quarry.getWidth()
                    + "Original Height: " + quarry.getHeight());
            telemetry.update();

            // Create cropped bitmap to show only stones
            Bitmap quarryCropped = createBitmap(quarry, cropStartX, cropStartY, cropWidth, cropHeight);

            // Save cropped bitmap to file
            if (saveBitmap) {
                try {

                    File file = new File(path, croppedBitmapName);
                    out = new FileOutputStream(file);
                    quarryCropped.compress(Bitmap.CompressFormat.PNG, 100, out);

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
            quarryCropped = createScaledBitmap(quarryCropped, 110, 20, true);

            int height;
            int width;
            int pixel;
            int bitmapWidth = quarryCropped.getWidth();
            int bitmapHeight = quarryCropped.getHeight();
            int colWidth = (int) ((double) bitmapWidth / 6.0);
            int colorLStartCol = (int) ((double) bitmapWidth * (1.0 / 6.0) - ((double) colWidth / 2.0));
            int colorCStartCol = (int) ((double) bitmapWidth * (3.0 / 6.0) - ((double) colWidth / 2.0));
            int colorRStartCol = (int) ((double) bitmapWidth * (5.0 / 6.0) - ((double) colWidth / 2.0));

            // Traverse through each row
            for (height = 0; height < bitmapHeight; ++height) {

                // Traverse through Left Position Columns
                for (width = colorLStartCol; width < colorLStartCol + colWidth; ++width) {

                    pixel = quarryCropped.getPixel(width, height);

                    if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {

                        yellowCountL += Color.red(pixel);
                        blackCountL += Color.blue(pixel);
                    }
                }

                // Traverse through Center Position Columns
                for (width = colorCStartCol; width < colorCStartCol + colWidth; ++width) {

                    pixel = quarryCropped.getPixel(width, height);

                    if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {

                        yellowCountL += Color.red(pixel);
                        blackCountL += Color.blue(pixel);
                    }
                }

                // Traverse through Right Position Columns
                for (width = colorRStartCol; width < colorRStartCol + colWidth; ++width) {

                    pixel = quarryCropped.getPixel(width, height);

                    if (Color.red(pixel) < 200 || Color.green(pixel) < 200 || Color.blue(pixel) < 200) {

                        yellowCountL += Color.red(pixel);
                        blackCountL += Color.blue(pixel);
                    }
                }
            }

            double blackYellowRatioL = blackCountL / yellowCountL;
            double blackYellowRatioC = blackCountC / yellowCountC;
            double blackYellowRatioR = blackCountR / yellowCountR;

            // Record stone position
            if (blackYellowRatioL > blackYellowRatioC && blackYellowRatioL > blackYellowRatioR)
                pos = 1;
            else if (blackYellowRatioC > blackYellowRatioL && blackYellowRatioC > blackYellowRatioR)
                pos = 2;
            else
                pos = 3;
        }

        return pos;
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

    // DRIVETRAIN
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

    // DRIVE
    public void setDriveTarget(double dist, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * dist));
        backLeft.setTargetPosition((int) (bl * dist));
        frontRight.setTargetPosition((int) (fr * dist));
        backRight.setTargetPosition((int) (br * dist));
    }

    // ROTATE
    public void setRotateTarget(double deg, double fl, double fr, double bl, double br) {

        frontLeft.setTargetPosition((int) (fl * deg));
        backLeft.setTargetPosition((int) (bl * deg));
        frontRight.setTargetPosition((int) (fr * deg));
        backRight.setTargetPosition((int) (br * deg));
    }
}
