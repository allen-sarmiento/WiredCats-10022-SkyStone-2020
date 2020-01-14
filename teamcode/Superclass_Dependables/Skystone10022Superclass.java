package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

import static org.firstinspires.ftc.teamcode.Utilities.Control.*;
import static org.firstinspires.ftc.teamcode.Utilities.UniversalVariables.*;
import static org.firstinspires.ftc.teamcode.Utilities.RobotVariables.*;

// UPDATED

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

        // PID Control
        pidRotate = new PIDController(0,0,0);
    }

    // METHODS

    public void forward(double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void backward(double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void strafeLeft(double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void strafeRight(double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void diagonal(double power, double inches, int quadrant) {

        // accepts quadrants:
        // 2 1
        // 3 4
        // can update this to accept angles at a later time

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // goes a certain direction based on the quadrant integer
            {
                if (quadrant == 1) {
                    frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
                    backRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
                }
                if (quadrant == 2) {
                    backLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
                    frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
                }
                if (quadrant == 3) {
                    frontLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
                    backRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
                }
                if (quadrant == 4) {
                    backLeft.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
                    frontRight.setTargetPosition((int) (-inches * COUNTS_PER_INCH));
                }
            }

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void rotateLeft(double power, int angle) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backLeft.setTargetPosition((int) (-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            frontRight.setTargetPosition((int) (angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backRight.setTargetPosition((int) (angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void rotateRight(double power, int angle) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int) (angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backLeft.setTargetPosition((int) (angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            frontRight.setTargetPosition((int) (-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backRight.setTargetPosition((int) (-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));


            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() && frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            // Stop all motion;
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    // HOOK

    public void hookDown() {

        hookL.setPosition(HOOK_DOWN);
        hookR.setPosition(HOOK_DOWN);
    }

    public void hookUp() {

        hookL.setPosition(HOOK_UP);
        hookR.setPosition(HOOK_UP);
    }

    // Y SLIDES

    public void extendY() {

        ySlide.setPower(ON);
    }

    public void retractY() {

        ySlide.setPower(REVERSE);
    }

    public void yOff() {

        ySlide.setPower(OFF);
    }

    // X SLIDES

    public void extendX(double power, int inches) {

        runEncoder(xSlide, power, inches);
    }

    public void extendX() {

        xSlide.setPower(ON);
    }

    public void retractX() {

        xSlide.setPower(REVERSE);
    }

    public void xOff() {

        xSlide.setPower(OFF);
    }


    // INTAKE

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


    // CLAMP

    public void closeClamp() {

        clamp.setPosition(CLAMP_DOWN);
    }

    public void openClamp() {

        clamp.setPosition(CLAMP_UP);
    }
}
