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
    int skyStonePos;

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

        forward(0.3, 4);
        
        sleep(1000);

        // Scans all trackables
        for (VuforiaTrackable trackable : allTrackables) {

            // If any trackable is visible
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                if (trackable.getName().equals("Blue Perimeter 1")) {

                    telemetry.addLine("Blue Perimeter 1 is visible");
                    sleep(1500);
                    
                    sequence = "Blue Perimeter 1";
                }

                else if (trackable.getName().equals("Blue Perimeter 2")) {

                    telemetry.addLine("Blue Perimeter 2 is visible");
                    sleep(1500);
                    
                    sequence = "Blue Perimeter 2";
                }

                else if (trackable.getName().equals("Red Perimeter 1")) {

                    telemetry.addLine("Red Perimeter 1 is visible");
                    sleep(1000);
                    
                    sequence = "Red Perimeter 1";
                }

                else if (trackable.getName().equals("Red Perimeter 2")) {

                    telemetry.addLine("Red Perimeter 2 is visible");
                    sleep(1500);
                    
                    sequence = "Red Perimeter 2";
                }

                if (trackable.getName().equals("Stone Target")) {

                    telemetry.addLine("SkyStone is visible");
                    sleep(1500);
                    
                    isSkyStone = true;
                }

                else {

                    isSkyStone = false;
                }

                telemetry.addLine("Nothing is visible");
                telemetry.update();
            }
        }

        if (sequence.equals("Blue Perimeter 1")){

            strafeLeft(0.2, 5);
            
            sleep(500);
            
            intake();
            
            forward(0.2, 5.5);
            
            rotateRight(0.2, 12.5);
            
            forward(0.2, 3.25);
            
            rotateLeft(0.2, 49);
            
            forward(0.4, 15);
            
            intakeOff();
            
            outtake();
        }

        if (sequence.equals("Blue Perimeter 2")){

            forward(0.2, 14); // move in front of build plate

            // rotateRight(0,90); // face claw to build plate

            // backward(0,0); // center robot to build plate

            // setHookDown();

            // strafeRight(0,0); // drag foundation to build zone

            // setHookUp();

            // forward(0,0);
        }

        if (sequence.equals("Red Perimeter 1")) {

            strafeRight(0.2, 5);
            
            sleep(750);
            
            intake();
            
            forward(0.2, 6.5);
            
            rotateRight(0.2, 23);
        }

        if (sequence.equals("Red Perimeter 2")) {

            forward(0,0); // move in front of build plate

            // rotateLeft(0,90); // face claw to build plate

            // forward(0,0); // center robot to build plate

            // setHookDown();

            // strafeRight(0,0); // drag foundation to build zone

            // setHookUp();

            // backward(0,0);
        }

        telemetry.update();
    }

    public void findSkyStonePositionRed() {

        if(isSkyStone) {

            skyStonePos = 1;
        }

        else {
            
            sleep(1000);

            strafeRight(0.2, 3.5);

            sleep(1000);

            if (isSkyStone) {

                skyStonePos = 2;
            }

            else {

                skyStonePos = 3;
            }
        }
    }

    public void deliverStones() {

        if (skyStonePos == 1) {

            rotateLeft(0, 90);

            backward(0,0);

            strafeLeft(0, 0);

            intake();

            forward(0,0);

            strafeLeft(0,0);

            backward(0,0);

            rotateLeft(0, 90);

            outtake();

            sleep(750);

            intakeOff();

            rotateRight(0,90);

            forward(0,90);

            strafeLeft(0,0);

            intake();

            forward(0,0);

            strafeRight(0,0);

            backward(0,0);

            rotateLeft(0,90);
            
            intakeOff();

            outtake();

            sleep(750);

            strafeRight(0,0);
        }

        else if (skyStonePos == 2) {


        }

        else{


        }
    }
}
