package org.firstinspires.ftc.teamcode.StatesBot.Midas;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name = "Midas: TeleOp")

public class MidasTeleOp extends MidasSuperclass {

    int i = 1;

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            setLeftVel(90);
            setRightVel(90);
            displayTelemetry();
        }
    }
}