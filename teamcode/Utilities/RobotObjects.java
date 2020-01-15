package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses.PIDController;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

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

    // VUFORIA
    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    public static final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";
    public static VuforiaLocalizer vuforia = null;

    // PID ControlConstants
    public static PIDController pidRotate;

    public static ElapsedTime runtime = new ElapsedTime();
}
