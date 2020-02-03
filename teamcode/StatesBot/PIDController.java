package org.firstinspires.ftc.teamcode.StatesBot;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {

    private double kP, kI, kD, proportional, integral, derivative;
    private double error, lastError, deltaError;
    public double correction;
    public boolean isFinished;

    public PIDController(double kP, double kI, double kD) {

        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        integral = 0;
        lastError = 0;
        isFinished = false;
    }

    public void performPID(double actualValue, double desiredValue, double tolerance, ElapsedTime timer) {

        timer.reset();
        error = actualValue - desiredValue;

        if (Math.abs(error) >= desiredValue * (tolerance/100) && error != lastError) {
            error = actualValue - desiredValue;
            deltaError = lastError - error;
            proportional = error;
            integral += deltaError * timer.time();
            derivative = deltaError / timer.time();
            correction = kP * proportional + kI * integral + kD * derivative;
            timer.reset();
        }

        else
            isFinished = true;
    }

    public void resetPID() {

        isFinished = false;
    }
}
