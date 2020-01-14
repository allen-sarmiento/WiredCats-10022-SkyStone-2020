package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.Utilities.RobotVariables.*;

public class Control {

    // Encoder

    public static void runEncoder(DcMotor m_motor, double power, double inches) {

        m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m_motor.setTargetPosition((int)(inches * COUNTS_PER_INCH));

        m_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (m_motor.isBusy()) {

            m_motor.setPower(power);
        }

        // Stop all motion;
        m_motor.setPower(0);

        // Turn off RUN_TO_POSITION
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
