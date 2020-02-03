package org.firstinspires.ftc.teamcode.StatesBot.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@TeleOp (name = "Method Tester")

public class MethodTester extends Skystone10022Superclass {

    public int i = 0;

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addLine("Method Finished: " + i);
            telemetry.update();

            // Pressing 'B' runs method
            if (gamepad1.a && a == 0) {
                a = 1;
            }
            else if (!gamepad1.a && a == 1) {
                diagonal(0.3, 5, 1);
                i++;
                a = 0;
            }

            if (gamepad1.b && b == 0) {
                b = 1;
            }
            else if (!gamepad1.b && b == 1) {
                diagonal(0.3, 5, 3);
                i++;
                b = 0;
            }

            if (gamepad1.x && x == 0) {
                x = 1;
            }
            else if (!gamepad1.x && x == 1) {
                diagonal(0.3, 5, 2);
                i++;
                x = 0;
            }

            if (gamepad1.y && y == 0) {
                y = 1;
            }
            else if (!gamepad1.y && y == 1) {
                diagonal(0.3, 5, 4);
                i++;
                y = 0;
            }
        }
    }
}