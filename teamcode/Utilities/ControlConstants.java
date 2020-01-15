package org.firstinspires.ftc.teamcode.Utilities;

import static org.firstinspires.ftc.teamcode.Utilities.UniversalVariables.*;

public class ControlConstants {

    // TOGGLES
    public static int b = 0, x = 0, rBumper = 0;

    // DRIVE CONTROL
    public static double slow;
    public static double flpower, frpower, blpower, brpower;

    public static final double WHEEL_DIAMETER_INCHES = 4;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public static final double DRIVE_TICKS_PER_MOTOR_REV = 537.6;
    public static final double DRIVE_GEAR_REDUCTION = 1;
    public static final double DRIVE_TICKS_PER_INCH = ((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES);

    public static final double ROBOT_RADIUS_INCHES  = 9.933;
    public static final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public static final double DRIVE_INCHES_PER_DEGREE  = ROBOT_CIRCUMFERENCE_INCHES / 360;
    // public static final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // GENERAL CONTROL
    public static final double ON = 1;
    public static final double OFF = 0;
    public static final double REVERSE = -1;

    // Y SLIDES
    public static final double MAX_LEVEL = 6;
    public static final double MIN_EXTENSION_Y = GRND;
    public static final double MAX_EXTENSION_Y = PLTFM + MAX_LEVEL * STONE_HEIGHT;

    // X SLIDES
    public static final double MIN_EXTENSION_X = 0;
    public static final double MAX_EXTENSION_X = 0;

    // CLAMP
    public static final double CLAMP_DOWN = 0.875;
    public static final double CLAMP_UP = 0;

    // INTAKE
    public static final double HOOK_UP = 0.15;
    public static final double HOOK_DOWN = 0.775;
}
