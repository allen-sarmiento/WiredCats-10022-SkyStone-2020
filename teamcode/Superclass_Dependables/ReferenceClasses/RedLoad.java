package org.firstinspires.ftc.teamcode.Superclass_Dependables.ReferenceClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Superclass_Dependables.Skystone10022Superclass;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous (name = "Experimental: RedLoad")

public class RedLoad extends Skystone10022Superclass {

    int stonePosition;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;//use back camera
    private static final String VUFORIA_KEY = "AUBZ9tz/////AAABmcD+zzeDb02lt3WkDlYB/vZi/6YK4SHRNFLfBrST0dK16ImQjN+KG3vN2MFs/EAcJydoBmOH0nbqY9lxbtNPKSpxOMtnrDwQmZWds+Z74kjqSmMCKDGT1a3LIIOF+6jbEWgITgiBRlY+gGD/b8m6Ck3jCoe+CyVhXv1zyOKtcjlWLBHGhBSQ/xJbHGdkDsah2WkFJGUaaXRWLHqnYyit/FKJzbQ5UjyFUraZZoTTXjgjfRvM7/YcwwDf+CXYCYObPKANY0g/y9YaArDYS2bgDL/5Fh9E3SUhAv8CpNprIA2T8GCZhMDzFJYme87N1+1DspG7+2AsEyabBSKhst11vV6Z8tWRcHCfspKeEO/LtO/B";

    // Class Members
    private VuforiaLocalizer vuforia = null;

    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");

        targetsSkyStone.activate();

        initialize();

        waitForStart();

        // backward(0.4, 10);
        sleep(250);

        telemetry.addLine("Scanning First");
        telemetry.update();

        if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

            telemetry.addLine("Position 1");
            telemetry.update();
            sleep(3000);

            stonePosition = 1;

        } else {

            // strafeRight(0.4, 2);
            // sleep(250);

            telemetry.addLine("Scanning Next");
            telemetry.update();
            sleep(3000);

            if (((VuforiaTrackableDefaultListener)stoneTarget.getListener()).isVisible()) {

                telemetry.addLine("Position 2");
                telemetry.update();
                sleep(3000);

                stonePosition = 2;

            } else {

                telemetry.addLine("Scan Failed: Position 3");
                telemetry.update();
                sleep(3000);

                stonePosition = 3;
            }
        }

        telemetry.addLine("Tracking Finished");
        telemetry.update();

        // forward(0.4, 5);
        sleep(250);

        // activateClamp();
        sleep(500);

        // backward(0.4, 5);
        sleep(250);

        // rotateRight(0.4, 90)
        sleep(250);

        if (stonePosition == 1) {

            // forward(0.4, 20);
            sleep(250);

            // outtake();
            // backward(0.4, 20);
            sleep(250);
        }

        else if (stonePosition == 2) {

            // forward(0.4, 24);
            sleep(250);

            // outtake();
            // backward(0.4, 24);
            sleep(250);
        }

        else {

            // forward(0.4, 28);
            sleep(250);

            // outtake();
            // backward(0.4, 28);
            sleep(250);
        }

        //rotateLeft(0.4,90);
        sleep(250);

        //intake();
        //forward(0.4,5);
        sleep(250);

        // backward(0.4,250)
        sleep(250);

        //rotateRight(0.4, 250)
        sleep(250);

        if (stonePosition == 1) {

            // forward(0.4, 20);
            sleep(250);
        }

        else if (stonePosition == 2) {

            // forward(0.4, 24)
            sleep(250);
        }

        else {

            // forward(0.4, 28);
            sleep(250);
        }

        //outtake();
        //backward(0.4, 5);
        sleep(250);



        sleep(30000);

        /*

        //get skystone
        if (stonePosition == 4) {

            /*
            strafeLeft(0.75, 10);

            //gather skystone
            {
                intake();
                forward(0.75, 2);
                activateClamp();
                backward(0.75, 2);
                intakeOff();
            }

            //travel field
            strafeRight(0.75, 25);
            ySlidesUp();
            strafeRight(0.75, 25);

            //place
            forward(0.75, 2);
            ySlidesStop();
            outtake();
            deactivateClamp();

            //return
            backward(0.75, 2);
            strafeLeft(0.75, 60);   //50 tow travel field + 10 to reach the next skystone

            //get other skystone
            {
                intake();
                forward(0.75, 2);
                activateClamp();
                backward(0.75, 2);
                intakeOff();
            }

            //deliver
            strafeRight(0.75, 25);
            ySlidesUp();
            strafeRight(0.75, 35);

        } else if (stonePosition == 5) {

            // repeat for stone position

        } else {
            // for stonePosition == 6, don't have to move
            // in case of error in which stonePosition never receives a valid value,
            // the robot will, by default, choose the quickest route.

            // repeat for stone position

        }

        //place
        ySlidesStop();
        outtake();
        deactivateClamp();

        //move foundation
        rotateRight(0.75, 90);
        setHookDown();
        rotateRight(0.75, 180);
        backward(0.75, 5.25);
        strafeLeft(0.75, 5.25);
        setHookUp();

        //park
        forward(0.75, 5);
        // may need left or right movements as well

         */

    }
}
