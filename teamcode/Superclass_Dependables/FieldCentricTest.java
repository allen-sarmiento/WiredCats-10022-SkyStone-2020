package org.firstinspires.ftc.teamcode.Superclass_Dependables;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

@TeleOp (name = "Test: Field-Centric Drive")

public class FieldCentricTest extends Skystone10022Superclass {

    double rotationDeg = 0;
    double rotation;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        while(!(isStarted()  || isStopRequested())) {

            idle();
        }

        waitForStart();

        while (opModeIsActive()) {

            for (int i = 1; i < 9; i++) {

                // Print Robot Orientation
                rotation = Math.toRadians(rotationDeg);

                double forward = i;
                double right = gamepad1.left_stick_x;
                double clockwise = gamepad1.right_stick_x;

                // If theta is measured clockwise from zero reference
                if (rotation <= 0) {

                    temp = forward * Math.cos(rotation) + right * Math.sin(rotation);
                    right = -forward * Math.sin(rotation) + right * Math.cos(rotation);
                    forward = temp;
                }

                // If theta is measured counterclockwise from zero reference

                if (rotation > 0) {

                    // Theta is reversed to account for IMU measurement
                    temp = forward * Math.cos(rotation) - right * Math.sin(rotation);
                    right = forward * Math.sin(rotation) + right * Math.cos(rotation);
                    forward = temp;
                }

                flpower = forward + clockwise + right;
                frpower = forward - clockwise - right;
                blpower = forward + clockwise - right;
                brpower = forward - clockwise + right;

                double max = Math.abs(flpower);

                if (Math.abs(frpower) > max) {

                    max = Math.abs(frpower);
                }

                if (Math.abs(blpower) > max) {

                    max = Math.abs(blpower);
                }

                if (Math.abs(brpower) > max) {

                    max = Math.abs(brpower);
                }

                if (max > 1) {

                    flpower /= max;
                    frpower /= max;
                    blpower /= max;
                    brpower /= max;
                }

                // Print Motor Power Direction
                telemetry.addLine();
                telemetry.addLine("Angle: " + rotationDeg);
                telemetry.addLine();
                telemetry.addLine("FL: " + direction(flpower) + ": " + flpower + "\t\t\t" + "FR: " + ": " + frpower + direction(frpower));
                telemetry.addLine();
                telemetry.addLine("BL: " + direction(blpower) + ": " + blpower + "\t\t\t" + "BR: " + ": " + brpower + direction(brpower));
                telemetry.addLine();
                telemetry.addLine("----------------------------------------------------------------------");

                rotationDeg += 45;
            }

            telemetry.update();

            sleep(999999999);
        }
    }
}


