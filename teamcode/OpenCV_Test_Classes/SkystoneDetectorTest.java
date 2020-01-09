package org.firstinspires.ftc.teamcode.OpenCV_Test_Classes;


import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
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

@TeleOp(name = "OpenCV Test: Skystone Detector")

public class SkystoneDetectorTest extends Skystone10022Superclass {

    private OpenCvCamera phoneCam;
    private SkystoneDetector skyStoneDetector;
    double xPos, yPos;

    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        phoneCam.openCameraDevice();

        skyStoneDetector = new SkystoneDetector();
        phoneCam.setPipeline(skyStoneDetector);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        waitForStart();

        while (opModeIsActive()) {

            // Ideal X-Position: 92 || 85 < x < 100
            // L: 61  || 0 <= y < 97
            // M: 132 || 97 <= y < 168
            // R: 202 || 168 <= y <= 320

            xPos = skyStoneDetector.getScreenPosition().x;
            yPos = skyStoneDetector.getScreenPosition().y;

            telemetry.addData("Skystone Position X", xPos);
            telemetry.addData("Skystone Position Y", yPos);

            telemetry.addLine("");

            if (xPos > 85 && xPos < 100) {

                if (yPos >= 0 && yPos < 97) {

                    telemetry.addLine("Position: Left");
                }

                else if (yPos >= 97 && yPos < 168) {

                    telemetry.addLine("Position: Middle");
                }

                else if (yPos >= 168 && yPos <= 320) {

                    telemetry.addLine("Position: Right");
                }

                else {

                    telemetry.addLine("Position: Null");
                }
            }

            else {

                if (xPos <= 85) {

                    telemetry.addLine("Quarry too low");
                }

                else if (xPos >= 100) {

                    telemetry.addLine("Quarry too high");
                }
            }

            telemetry.update();
        }
    }
}
