package org.firstinspires.ftc.teamcode.StatesBot.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@Autonomous (name = "Test: Autonomous")

public class AutonomousTest extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        resetDriveEncoders();

        double target = 5 * DRIVE_TICKS_PER_INCH;

        frontLeft.setTargetPosition((int)target);
        backRight.setTargetPosition((int)target);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}