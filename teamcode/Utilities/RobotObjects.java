package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

public class RobotObjects {

    // DRIVETRAIN
    public static DcMotor frontLeft, frontRight, backLeft, backRight;

    // CLAMP
    public static Servo clamp;

    // INTAKE
    public static DcMotor leftIntake, rightIntake;

    // HOOK
    public static Servo hookL, hookR;

    // LINEAR SLIDES
    public static DcMotor ySlide, xSlide;

    // REV IMU
    public static BNO055IMU imu;
    public static Orientation theta;
    public static double temp;

    // PID ControlConstants
    public static PIDController pidRotate;

    public static ElapsedTime runtime = new ElapsedTime();
}
