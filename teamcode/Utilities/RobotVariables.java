package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

public class RobotVariables {

    // DRIVETRAIN
    public static DcMotor frontLeft, frontRight, backLeft, backRight;
    public static double flpower, frpower, blpower, brpower;

    // CLAMP
    public static Servo clamp;

    // INTAKE
    public static DcMotor leftIntake, rightIntake;

    // HOOK
    public static Servo hookL, hookR;

    // LINEAR SLIDES
    public static DcMotor ySlide, xSlide;

    // ROBOT CONSTANTS
    public static final double COUNTS_PER_MOTOR_REV = 1680;
    public static final double DRIVE_GEAR_REDUCTION = 1;
    public static final double COUNTS_PER_INCH = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (8.8857658763)) / 2;
    public static final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // REV IMU
    public static BNO055IMU imu;
    public static Orientation theta;
    public static double temp;

    // PID Control
    public static PIDController pidRotate;

    public static ElapsedTime runtime = new ElapsedTime();
}
