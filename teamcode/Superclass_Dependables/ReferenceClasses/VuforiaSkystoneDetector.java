package org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;
import org.firstinspires.ftc.teamcode.Utilities.RobotObjects.*;

import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.CAMERA_CHOICE;
import static org.firstinspires.ftc.teamcode.Utilities.RobotObjects.VUFORIA_KEY;

@Autonomous (name = "Vuforia Test: Skystone Detector")

public class VuforiaSkystoneDetector extends Skystone10022Superclass {

    @Override
    public void runOpMode() throws  InterruptedException {

        initialize();

        waitForStart();

        int skystone = vuforiaScan(true);

        telemetry.addLine("SCAN FINISHED");
        telemetry.addLine("Stone Position: " + skystone);
        telemetry.update();
        sleep(10000);
    }
}