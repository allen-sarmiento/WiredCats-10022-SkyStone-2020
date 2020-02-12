package org.firstinspires.ftc.teamcode.StatesBot.Midas;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@TeleOp

public class Accel extends MidasSuperclass {

    double[] velocities = new double[10];
    double[] times = new double[10];
    double[] accelerations = new double[10];
    ElapsedTime runtime = new ElapsedTime();
    double totalAccel = 0;
    double totalTime = 0;
    double avgAccel;
    double vel = 146;
    double time = 0;

    @Override
    public void runOpMode() {

        for (int i = 0; i < 10; i++) {
            velocities[i] = 14.6 * (i + 1);
        }

        initialize();

        waitForStart();

        setLeftVel(vel);
        setRightVel(vel);

        runtime.reset();

        for (int i = 0; i < 10; i++) {

            if (getVelocityLeft() < velocities[i]){

                times[i] = runtime.seconds();
                totalTime += times[i];

                //telemetry.addLine("Time " + i + ": " + times[i]);
            }
        }

        telemetry.addLine("Total Time: " + totalTime);
        telemetry.update();

        setDrivePower(0);

        sleep(30000);

        /*

        velocities[0] = 14.6;
        velocities[1] = 29.2;
        velocities[2] = 43.8;
        velocities[3] = 58.4;
        velocities[4] = 73;
        velocities[5] = 87.6;
        velocities[6] = 102.2;
        velocities[7] = 116.8;
        velocities[8] = 136.4;
        velocities[9] = 146;

        initialize();

        waitForStart();

        setLeftVel(vel);
        setRightVel(vel);

        runtime.startTime();

        if (getVelocityLeft() < vel - 3 || getVelocityLeft() > vel + 3){

            time = runtime.milliseconds();
        }

        telemetry.addLine("Time: " + time);
        telemetry.addLine("Vel: " + getVelocityLeft());
        telemetry.update();
        sleep(30000);

         */
    }
}