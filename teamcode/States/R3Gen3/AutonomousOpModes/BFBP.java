org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "B: Foundation Bridge")

public class BFBP extends R3Gen3Superclass {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        backward(0.3, 44);
        sleep(200);

        rotateLeft(0.3, 110);
        sleep(200);

        backward(0.3,10);
        hookDown();
        sleep(300);

        forward(0.3, 10);
        sleep(200);

        rotateLeft(0.3,140);
        sleep(300);

        backward(0.3, 40);
        sleep(200);

        strafeLeft(0.3, 32);
        hookUp();
        sleep(200);

        strafeRight(0.3, 32);
        sleep(200);

        backward(0.3, 20);
        sleep(200);

        strafeRight(0.3, 70);
        sleep(200);

        sleep(30000);

    }
}
