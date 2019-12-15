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

        backward(0.15, 7.1); // Move to first position stone

        telemetry.addLine("Done");
        telemetry.update();

        // Find skystone position
        while (position == 0) {

            telemetry.addLine("Scanning 1");
            telemetry.update();
            sleep(2000);

            if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

                telemetry.addLine("Position 1");
                telemetry.update();

                position = 1;
            }

            else {

                telemetry.addLine("Scanning 2");
                telemetry.update();
                sleep(2000);

                strafeRight(0.15, 2.8); // Move to next stone

                sleep(300);

                if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

                    telemetry.addLine("Position 2");
                    telemetry.update();

                    position = 2;
                }

                else {

                    telemetry.addLine("Scanning 3");
                    telemetry.update();
                    sleep(2000);

                    telemetry.addLine("Position 3");
                    telemetry.update();

                    position = 3;
                }
            }
        }

        telemetry.addLine("Finished Scan: " + position);
        telemetry.update();

        if (position == 1) {

            deliveryDistance = 0;
            returnDistance = 0;
        }

        else if (position == 2) {

            deliveryDistance = 0;
            returnDistance = 0;
        }

        else {

            // strafeLeft(0,0);

            sleep(300);

            deliveryDistance = 0;
            returnDistance = 0;
        }

        // rotateRight(0,0); // Face intake to skystone

        sleep(300);

        // intake();

        // forward(0,0); // Intake first skystone

        sleep(300);

        // backward(0,0);

        sleep(300);

        // rotateRight(0,0);

        sleep(300);

        // backward(0, deliveryDistance);

        sleep(300);

        // strafeLeft(0, 0);

        sleep(300);

        // setHookDown();

        // strafeRight(0,0);

        sleep(300);

        // rotateLeft(0,0);

        sleep(300);

        // setHookUp();

        // forward(0,0);

        sleep(300);

        // outtake();

        sleep(500);

        // strafeRight(0, returnDistance);

        sleep(300);

        // intake();

        // forward(0,0);

        sleep(300);

        // backward(0,0);

        sleep(300);

        // strafeLeft(0,0);

        sleep(300);

        // outtake();

        sleep(500);

        // strafeRight(0,0);

        sleep(10000);
    }
}
