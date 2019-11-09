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

@Autonomous

public class SkyStone10022Autonomous extends SkyStone10022LinearOpMode {

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camra
    private static final boolean PHONE_IS_PORTRAIT = false ;
    private static final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";

    // Class Members
    private VuforiaLocalizer vuforia = null;

    String sequence = "";

    boolean isSkyStone;

    int skyStonePos = 0;

    @Override public void runOpMode() {

        initialize();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        targetsSkyStone.activate();

        waitForStart();

        // Run if in Blue Perimeter 1
        if (sequence.equals("")) {

            backward(.8, 24); // Move In front of left stone

            sleep(1000);

            // Scans all trackables
            for (VuforiaTrackable trackable : allTrackables) {

                // If any trackable is visible
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                    if (trackable.getName().equals("Stone Target")) {

                        telemetry.addLine("Stone Target is visible");

                        skyStonePos = 1;

                        isSkyStone = true;
                    }
                }
            }

        }

        // Run if in Blue Perimeter 2
        if (sequence.equals("BluePer2")) {

            // Insert Execution

            // Scans all trackables
            for (VuforiaTrackable trackable : allTrackables) {

                // If any trackable is visible
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                    if (trackable.getName().equals("Stone Target")) {

                        telemetry.addLine("Stone Target is visible");
                    }
                }
            }
        }

        // Run if in Red Perimeter 1
        if(sequence.equals("RedPer1")) {

            // Insert Execution

            // Scans all trackables
            for (VuforiaTrackable trackable : allTrackables) {

                // If any trackable is visible
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                    if (trackable.getName().equals("Stone Target")) {

                        telemetry.addLine("Stone Target is visible");
                    }
                }
            }
        }

        // Run if in Red Perimeter 2
        while (opModeIsActive()  && sequence.equals("RedPer2")) {

            // Insert Execution

            // Scans all trackables
            for (VuforiaTrackable trackable : allTrackables) {

                // If any trackable is visible
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                    if (trackable.getName().equals("Stone Target")) {

                        telemetry.addLine("Stone Target is visible");
                    }
                }
            }
        }

        telemetry.update();
    }
}
