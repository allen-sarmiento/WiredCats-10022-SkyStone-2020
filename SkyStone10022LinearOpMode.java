package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

public abstract class SkyStone10022LinearOpMode extends LinearOpMode {

    //DECLARATION

    //Drivetrain
    DcMotor frontLeft, frontRight, backLeft, backRight;

    //Intake
    Servo clampActivate;    // open or close
    Servo clampRotate;      // rotate mechanism

    //Hook
    Servo setHookL;
    Servo setHookR;

    //Linear Slides
    // Y (Vertical)
    DcMotor ySlide;
    //X (Horizontal)
    CRServo xSlide;

    //Variables
    int toggle1 = 0, toggle2 = 0, toggle3 = 0, yToggle = 0;

    static final double     COUNTS_PER_MOTOR_REV    = 1680;
    static final double     DRIVE_GEAR_REDUCTION    = 1;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final double     COUNTS_PER_INCH         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (8.8857658763))/2;
    static final double     DRIVE_INCHES_PER_DEGREE = 22.0/90;

    //Arm Motor
    static final double ARM_TICKS_REV = 28000;
    static final double COUNTS_PER_DEGREE = (ARM_TICKS_REV) / (360);

    //starting positions
    double clampInitPosition;   // claw
    int ySlidePosition;      // vertical slides initial position

    // y slide stuff
    int change1, change;


    public void initialize() {

        //DEVICE INITIALIZATION

        //Drivetrain
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //Intake
        clampActivate = hardwareMap.servo.get("clampActivate");
        clampRotate = hardwareMap.servo.get("clampRotate");
        clampInitPosition = clampRotate.getPosition();  //starting position

        //Hook
        setHookL = hardwareMap.servo.get("setHookL");
        setHookR = hardwareMap.servo.get("setHookR");
        setHookR.setDirection(Servo.Direction.REVERSE);

        //slides
        xSlide = hardwareMap.crservo.get("xSlide");     // x
        ySlide = hardwareMap.dcMotor.get("ySlide");     // y
        ySlidePosition = ySlide.getCurrentPosition();   // initial y position

        change1 = 100;          // CHANGE THIS
        change = 150;           // THESE ARE RANDOM VALUES -- PLEASE TEST, FIND, AND ENTER REAL ONES
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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
                /*
                telemetry.addData("Path1",  "Running to %7d :%7d", BL.getTargetPosition(), BR.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d", BL.getCurrentPosition(), BR.getCurrentPosition());
                telemetry.update();
                 */
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

    public void setHookDown() {

        setHookL.setPosition(0.1);
        setHookR.setPosition(0.1);
    }

    public void setHookUp() {

        setHookL.setPosition(0.7);
        setHookR.setPosition(0.7);
    }
}
