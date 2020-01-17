package org.firstinspires.ftc.teamcode.Utilities;

import static org.firstinspires.ftc.teamcode.Utilities.UniversalVariables.*;

public class ControlConstants {

    // TOGGLES
    public static int a = 0, b = 0, x = 0, y = 0, up = 0, down = 0, left = 0;

    // GENERAL CONTROL -----------------------------------------------------------------------------
    public static final double ON = 1;
    public static final double OFF = 0;
    public static final double REVERSE = -1;

    // DRIVE CONTROL -------------------------------------------------------------------------------
    public static double slow;
    public static double flpower, frpower, blpower, brpower;

    public static final double WHEEL_DIAMETER_INCHES = 4;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * Math.PI;
    public static final double DRIVE_TICKS_PER_MOTOR_REV = 537.6; // NeveRest Orbital 20
    public static final double DRIVE_GEAR_REDUCTION = 1;
    public static final double DRIVE_TICKS_PER_INCH = ((DRIVE_TICKS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_INCHES);

    public static final double ROBOT_RADIUS_INCHES  = 9.933;
    public static final double ROBOT_CIRCUMFERENCE_INCHES  = 2 * ROBOT_RADIUS_INCHES * Math.PI;
    public static final double DRIVE_INCHES_PER_DEGREE  = ROBOT_CIRCUMFERENCE_INCHES / 360;
    // public static final double DRIVE_INCHES_PER_DEGREE = 13.75 / 180;

    // Y SLIDES ------------------------------------------------------------------------------------
    public static int yTargetInches = 0;

    public static final double MAX_LEVEL = 6;
    public static final double Y_MIN_EXTENSION = GRND;
    public static final double Y_MAX_EXTENSION = PLTFM + MAX_LEVEL * STONE_HEIGHT;

    public static final double Y_SPOOL_DIAMETER_INCHES = 50;
    public static final double Y_SPOOL_CIRCUMFERENCE_INCHES = Y_SPOOL_DIAMETER_INCHES * Math.PI;
    public static final double Y_TICKS_PER_MOTOR_REV = 1120; // NeveRest Classic 40
    public static final double Y_GEAR_REDUCTION = 1;
    public static final double Y_TICKS_PER_INCH = ((Y_TICKS_PER_MOTOR_REV * Y_GEAR_REDUCTION) / Y_SPOOL_CIRCUMFERENCE_INCHES);

    // X SLIDES ------------------------------------------------------------------------------------
    public static final double X_MIN_EXTENSION = 0;
    public static final double X_MAX_EXTENSION = 0; // temp

    public static final double X_SPOOL_DIAMETER_INCHES = 50;
    public static final double X_SPOOL_CIRCUMFERENCE_INCHES = X_SPOOL_DIAMETER_INCHES * Math.PI;
    public static final double x_TICKS_PER_MOTOR_REV = 560; // NeveRest Classic 20
    public static final double X_GEAR_REDUCTION = 1;
    public static final double X_TICKS_PER_INCH = ((x_TICKS_PER_MOTOR_REV * X_GEAR_REDUCTION) / X_SPOOL_CIRCUMFERENCE_INCHES);

    // CLAMP ---------------------------------------------------------------------------------------
    public static final double CLAMP_DOWN = 0.875;
    public static final double CLAMP_UP = 0;
    public static boolean isLoaded;

    // INTAKE --------------------------------------------------------------------------------------
    public static final double HOOK_UP = 0.15;
    public static final double HOOK_DOWN = 0.775;
}
