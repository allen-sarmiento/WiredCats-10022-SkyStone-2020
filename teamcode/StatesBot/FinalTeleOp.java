package org.firstinspires.ftc.teamcode.StatesBot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.StatesBot.Skystone10022Superclass;

@TeleOp (name = "Leagues: TeleOp (Final)")

// ADD: If manual slide control, reset autostack

public class FinalTeleOp extends Skystone10022Superclass {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        TeleOpTime.reset();

        while (opModeIsActive()) {

            // 'Back' Button toggles between dual and solo control
            if ((gamepad1.back || gamepad2.back) && back == 0)
                back = 1;
            else if (!(gamepad1.back || gamepad2.back) && back == 1) {
                dualControl = false;
                back = 2;
            }
            else if ((gamepad1.back || gamepad2.back) && back == 2)
                back = 3;
            else if (!(gamepad1.back || gamepad2.back) && back == 3) {
                dualControl = true;
                back = 0;
            }

            // Solo Driver
            if (!dualControl)
                soloTeleOp();

                // Dual Driver
            else
                dualTeleOp();

        }
    }
}