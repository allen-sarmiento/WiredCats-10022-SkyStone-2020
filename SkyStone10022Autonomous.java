package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous

public class SkyStone10022Autonomous extends SkyStone10022LinearOpMode {

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    private static final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";

    // Class Members
    private VuforiaLocalizer vuforia = null;

    String sequence = "";
    int position = 0;
    double deliveryDistance;
    double returnDistance;

    @Override
    public void runOpMode() {

        initialize();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        targetsSkyStone.activate();

        waitForStart();

        // strafeLeft(0,0); // To foundation

        sleep(300);

        // setHookDown(); // Grab foundation

        sleep(300);

        // strafeRight(0,0); // Drag foundation to depot

        sleep(300);

        // setHookUp();

        sleep(300);

        // forward(0,0); // Align to first stone

        sleep(300);

        // rotateRight(0,0); // Face camera to quarry

        sleep(300);

        // forward(0,0); // Move to first stone

        sleep(300);

        // Find skystone position
        while (position == 0) {

            if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

                position = 1;

                telemetry.addLine("Position 1");
                telemetry.update();
            }

            else {

                // strafeLeft(0,0); // Move to next stone

                sleep(300);

                if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

                    position = 2;

                    telemetry.addLine("Position 2");
                    telemetry.update();
                }

                else {

                    position = 3;

                    telemetry.addLine("Position 3");
                    telemetry.update();
                }
            }
        }

        if (position == 1) {

            deliveryDistance = 0;
            returnDistance = 0;
        }

        else if (position == 2) {

            deliveryDistance = 0;
            returnDistance = 0;
        }

        else {

            deliveryDistance = 0;
            returnDistance = 0;
        }

        if (position == 1 || position == 2) {

            // rotateRight(0,0); // Face intake to skystone

            sleep(300);

            // intake();

            // forward(0,0); // Intake first skystone

            sleep(300);

            // backward(0,0);

            sleep(300);

            // rotateLeft(0,0);

            sleep(300);

            // forward(0, deliveryDistance);

            sleep(300);

            outtake();
        }
    }
}
