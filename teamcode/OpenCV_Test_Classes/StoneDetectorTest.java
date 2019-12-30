package org.firstinspires.ftc.teamcode.Test;

import com.disnodeteam.dogecv.detectors.skystone.StoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

/*
 * Thanks to EasyOpenCV for the great API (and most of the example)
 *
 * Original Work Copright(c) 2019 OpenFTC Team
 * Derived Work Copyright(c) 2019 DogeDevs
 */

@TeleOp(name = "Test: Stone Detector")

public class StoneDetectorTest extends Skystone10022Superclass {

    private OpenCvCamera phoneCam;
    private StoneDetector stoneDetector;

    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        phoneCam.openCameraDevice();

        stoneDetector = new StoneDetector();
        phoneCam.setPipeline(stoneDetector);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        waitForStart();

        while (opModeIsActive()) {

            if(stoneDetector.isDetected()) {

                try {

                    for (int i = 0; i < stoneDetector.foundRectangles().size(); i++) {

                        telemetry.addData("Stone X-Position: " + (i + 1), stoneDetector.foundRectangles().get(i).x);
                        telemetry.addData("Stone Y-Position: " + (i + 1), stoneDetector.foundRectangles().get(i).y);
                    }

                } catch (Exception e) {

                    telemetry.addData("Stones", "Not Detected");
                }
            }

            telemetry.update();
        }
    }
}
