package FieldCentric;

public class IdealCase {

    private int angle;
    private String theString;
    private String joystickAxis;

    private static String[] acceptableAxes = {"leftx", "lefty", "rightx"};
    private static Exception InvalidAxisException = new Exception();

    public IdealCase(int angle, String joystickAxis) {

        try {

            checkJoystickAxis(joystickAxis);
        }

        catch (Exception InvalidAxisException) {

            System.out.println("INVALID AXIS (IdealCase): Enter \"leftx\", \"lefty\" or \"rightx\".");
            System.out.println();
        }

        this.angle = angle;
        this.joystickAxis = joystickAxis;
    }

    public void initializeIdeal() {

        switch (angle) {

            case 0:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 45:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "ZERO | 0.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "ZERO | 0.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "ZERO | 0.0" + "\n"
                                + "BL: " + "ZERO | 0.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 90:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 135:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "ZERO | 0.0" + "\n"
                                + "BL: " + "ZERO | 0.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "ZERO | 0.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "ZERO | 0.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 180:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "POS | 1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 225:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "ZERO | 0.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "ZERO | 0.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "ZERO | 0.0" + "\n"
                                + "BL: " + "ZERO | 0.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 270:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "NEG | -1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;

            case 315:

                switch (joystickAxis) {

                    case "lefty":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "ZERO | 0.0" + "\n"
                                + "BL: " + "ZERO | 0.0" + "\t\t"
                                + "BR: " + "POS | 1.0" + "\n"
                                + "#################################";
                        break;

                    case "leftx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "ZERO | 0.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "NEG | -1.0" + "\t\t"
                                + "BR: " + "ZERO | 0.0" + "\n"
                                + "#################################";
                        break;

                    case "rightx":
                        theString = "\n"
                                + "############# IDEAL #############" + "\n"
                                + "ANGLE: " + angle + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                                + "FL: " + "POS | 1.0" + "\t\t"
                                + "FR: " + "NEG | -1.0" + "\n"
                                + "BL: " + "POS | 1.0" + "\t\t"
                                + "BR: " + "NEG | -1.0" + "\n"
                                + "#################################";
                        break;
                }
                break;
        }
    }

    private void checkJoystickAxis(String input) throws Exception {

        boolean isValid = false;

        for(int i = 0; i < acceptableAxes.length; i++) {

            if(input.equals(acceptableAxes[i])) {

                isValid = true;
            }
        }

        if(!isValid) {

            throw InvalidAxisException;
        }
    }

    @Override
    public String toString() {

        return theString;
    }
}
