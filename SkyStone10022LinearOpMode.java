package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public abstract class SkyStone10022LinearOpMode extends LinearOpMode {

    //DECLARATION

    // DRIVETRAIN
    DcMotor frontLeft, frontRight, backLeft, backRight;
    double flpower, frpower, blpower, brpower;

    // CLAW
    Servo clawActivate;
    Servo clawRotate;

    // INTAKE
    DcMotor leftIntake, rightIntake;

    // HOOK
    Servo setHookL;
    Servo setHookR;

    // LINEAR SLIDES
    DcMotor ySlideOne, ySlideTwo;
    CRServo xSlide;

    // VARIABLES
    int bToggle = 0, yToggle = 0, xToggle = 0, backToggle = 0, rBumperToggle = 0, lBumperToggle = 0;

    static final double     COUNTS_PER_MOTOR_REV    = 1680;
    static final double     DRIVE_GEAR_REDUCTION    = 1;
    static final double     COUNTS_PER_INCH         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (8.8857658763))/2;
    static final double     DRIVE_INCHES_PER_DEGREE = 22.0/90;

    //starting positions
    double clampInitPosition;
    double ySlideOnePos;

    // REV IMU
    BNO055IMU imu;
    Orientation theta;
    double temp;


    public void initialize() {

        //DEVICE INITIALIZATION

        //Drivetrain
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Intake
        leftIntake = hardwareMap.dcMotor.get("leftIntake");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake.setDirection(DcMotor.Direction.REVERSE);

        // claw
        clawActivate = hardwareMap.servo.get("clawActivate");
        clawRotate = hardwareMap.servo.get("clawRotate");

        //Hook
        setHookL = hardwareMap.servo.get("setHookL");
        setHookR = hardwareMap.servo.get("setHookR");
        setHookR.setDirection(Servo.Direction.REVERSE);

        //slides
        xSlide = hardwareMap.crservo.get("xSlide");
        ySlideOne = hardwareMap.dcMotor.get("ySlideOne");
        ySlideTwo = hardwareMap.dcMotor.get("ySlideTwo");
        ySlideTwo.setDirection(DcMotor.Direction.REVERSE);
        ySlideOnePos = ySlideOne.getCurrentPosition();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
    }

    public void forward (double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int)(inches * COUNTS_PER_INCH));


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

    public void backward (double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int)(-inches * COUNTS_PER_INCH));


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

    public void strafeLeft (double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int)(-inches * COUNTS_PER_INCH));


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

    public void strafeRight (double power, double inches) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(inches * COUNTS_PER_INCH));
            backLeft.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int)(-inches * COUNTS_PER_INCH));
            backRight.setTargetPosition((int)(inches * COUNTS_PER_INCH));


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

    public void rotateLeft (double power, double angle) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backLeft.setTargetPosition((int)(-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            frontRight.setTargetPosition((int)(angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backRight.setTargetPosition((int)(angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));


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

    public void rotateRight (double power, double angle) {

        if (opModeIsActive()) {

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition((int)(angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backLeft.setTargetPosition((int)(angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            frontRight.setTargetPosition((int)(-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));
            backRight.setTargetPosition((int)(-angle * COUNTS_PER_INCH * DRIVE_INCHES_PER_DEGREE));


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

    // hook methods

    public void setHookDown() {

        setHookL.setPosition(0.9);
        setHookR.setPosition(0.9);
    }

    public void setHookUp() {

        setHookL.setPosition(0.375);
        setHookR.setPosition(0.375);
    }

    // y slides methods

    public void ySlidesUp() {

        ySlideOne.setPower(0.6);
        ySlideTwo.setPower(0.6);

    }

    public void ySlidesDown() {

        ySlideOne.setPower(-0.6);
        ySlideTwo.setPower(-0.6);

    }

    public void ySlidesStop() {

        ySlideOne.setPower(0.0);
        ySlideTwo.setPower(0.0);

    }

    // intake/outtake/off methods

    public void intake() {

        leftIntake.setPower(1);
        rightIntake.setPower(1);

    }

    public void outtake() {

        leftIntake.setPower(-1);
        rightIntake.setPower(-1);

    }

    public void intakeOff() {

        leftIntake.setPower(0);
        rightIntake.setPower(0);

    }

    // x slides

    public void xSlideForward() {

        xSlide.setPower(1.0);

    }

    public void xSlideBackward() {

        xSlide.setPower(-1.0);

    }

    public void xSlideOff() {

        xSlide.setPower(0.0);

    }

    // claw rotate

    public void rotateClawReset() {

        clawRotate.setPosition(0.0);

    }

    public void rotateClaw() {

        clawRotate.setPosition(0.32);

    }

    // claw activate

    public void activateClaw() {
        clawActivate.setPosition(0.6);
    }

    public void deactivateClaw() {
        clawActivate.setPosition(0.8);
    }

    // direction

    public String direction(double power) {

        if (power > 0) {
            return "Positive";
        }

        else if (power < 0) {
            return "Negative";
        }

        else {
            return "Zero";
        }
    }
}
