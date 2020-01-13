package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

// UPDATED

public abstract class Skystone10022Superclass extends LinearOpMode {

    //DECLARATION

    // DRIVETRAIN
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public double flpower, frpower, blpower, brpower;

    // CLAMP
    public Servo clamp;

    // INTAKE
    public DcMotor leftIntake, rightIntake;

    // HOOK
    public Servo setHookL;
    public Servo setHookR;

    // LINEAR SLIDES
    public DcMotor /*ySlideL,*/ ySlideR;
    public CRServo xSlide;

    // CONTROLLER VARIABLES
    public int bToggle = 0, xToggle = 0, rBumperToggle = 0;
    public double rTrigger = 0;

    // ROBOT CONSTANTS
    static final double COUNTS_PER_MOTOR_REV = 1680;
    static final double DRIVE_GEAR_REDUCTION = 1;
    static final double COUNTS_PER_INCH = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (8.8857658763)) / 2;
    static final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // REV IMU
    public BNO055IMU imu;
    public Orientation theta;
    public double temp;

    // PID Control
    PIDController pidRotate;

    ElapsedTime runtime = new ElapsedTime();

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
        setHookL = hardwareMap.servo.get("setHookL");
        setHookR = hardwareMap.servo.get("setHookR");
        setHookR.setDirection(Servo.Direction.REVERSE);

        // LINEAR SLIDES
        xSlide = hardwareMap.crservo.get("xSlide");

        //ySlideL = hardwareMap.dcMotor.get("ySlideL");
        ySlideR = hardwareMap.dcMotor.get("ySlideR");

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

    public void rotateLeft(double power, double angle) {

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

    public void rotateRight(double power, double angle) {

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

    public void extendY(double power, int ticks) {

        if (opModeIsActive()) {

            //ySlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ySlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //ySlideL.setTargetPosition(ticks);
            ySlideR.setTargetPosition(ticks);

            //ySlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            ySlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (opModeIsActive() /*&& ySlideL.isBusy()*/ && ySlideR.isBusy()) {

                ySlideR.setPower(power);
                //ySlideL.setPower(power);
            }

            // Stop all motion;
            ySlideR.setPower(0);
            //ySlideL.setPower(0);

            // Turn off RUN_TO_POSITION
            //ySlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ySlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void extendX(int msec) {

        runtime.reset();

        while (runtime.milliseconds() < msec) {

            xSlide.setPower(1);
        }

        xSlide.setPower(0);
    }

    // HOOK

    public void setHookDown() {

        setHookL.setPosition(0.775);
        setHookR.setPosition(0.775);
    }

    public void setHookUp() {

        setHookL.setPosition(0.15);
        setHookR.setPosition(0.15);
    }

    // Y SLIDES

    public void ySlidesUp() {

        //ySlideL.setPower(-0.8);
        ySlideR.setPower(-0.8);
    }

    public void ySlidesDown() {

        //ySlideL.setPower(0.8);
        ySlideR.setPower(0.8);
    }

    public void ySlidesStop() {

        //ySlideL.setPower(0.0);
        ySlideR.setPower(0.0);
    }

    // X SLIDES

    public void xSlideForward() {   //unused

        xSlide.setPower(-1.0);
    }

    public void xSlideBackward() {  //unused

        xSlide.setPower(1.0);
    }

    public void xSlideOff() {       //unused

        xSlide.setPower(0.0);
    }


    // INTAKE

    public void intake() {

        leftIntake.setPower(1.0);
        rightIntake.setPower(1.0);
    }

    public void outtake() {

        leftIntake.setPower(-1);
        rightIntake.setPower(-1);
    }

    public void intakeOff() {

        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }


    // CLAMP

    public void activateClamp() {

        clamp.setPosition(0.0);
    }

    public void deactivateClamp() {

        clamp.setPosition(0.875);
    }
}
