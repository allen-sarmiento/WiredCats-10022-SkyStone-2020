\package org.firstinspires.ftc.teamcode.States.R3Gen3.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.States.R3Gen3.R3Gen3Superclass;

@Autonomous (name = "B: Foundation Wall")

public class BFWP extends R3Gen3Superclass {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        backward(0.4, 44);
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
        
        strafeLeft(0.3, 35);
        hookUp();
        sleep(200);
        
        strafeRight(0.3, 35);
        sleep(200);
        
        backward(0.3, 40);
        sleep(200);
        
        strafeRight(0.3, 50);
        sleep(200);
        
        sleep(30000);
        
    }
}
