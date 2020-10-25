package org.firstinspires.ftc.teamcode.States.R3Gen3.DriverOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

import java.util.concurrent.TimeUnit;

@TeleOp (name = "Tester")

public class Tester extends R3Gen3Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        TeleOpTime.reset();

        while (opModeIsActive()) {
            
            if (gamepad1.b && b == 0)
                b = 1;
            else if (!gamepad1.b && b == 1) {
                extendPusher();
                b = 2;
            }
            else if (gamepad1.b && b == 2)
                b = 3;
            else if (!gamepad1.b && b == 3) {
                retractPusher();
                b = 0;
            }
        }
    }
}
