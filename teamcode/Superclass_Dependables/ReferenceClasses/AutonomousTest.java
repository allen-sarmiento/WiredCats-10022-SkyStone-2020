package org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

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

        pidDiagonal.resetPID();

        while (opModeIsActive() && frontLeft.isBusy() && backRight.isBusy()) {

            pidDiagonal.performPID(getDrivePosition(), target, 1, pidTimer);
            frontLeft.setPower(pidDiagonal.correction);
            backRight.setPower(pidDiagonal.correction);

            if (pidDiagonal.isFinished) {

                setDriveTarget(5,
                        1,1,
                        1,1);

                pidDrive.resetPID();

                while (opModeIsActive() && driveIsBusy() && !pidDrive.isFinished) {

                    pidDrive.performPID(getDrivePosition(), target, 1, pidTimer);
                    setDrivePower(pidDrive.correction);
                }
            }
        }
    }
}