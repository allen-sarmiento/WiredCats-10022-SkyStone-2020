package FieldCentric;

public class FieldCentricCase {

    private double flpower, frpower, blpower, brpower;
    private double max = Math.abs(flpower);
    private double temp, forward, right, clockwise;

    private double angleRad, angleDeg;
    private double joystickValue;
    private String joystickAxis;

    private String theString = "";
    private static String[] acceptableAxes = {"leftx", "lefty", "rightx"};
    private static Exception InvalidAxisException = new Exception();

    public FieldCentricCase(double angleDeg, String joystickAxis, double joystickValue) {

        try {

            checkJoystickAxis(joystickAxis);
        }

        catch (Exception InvalidAxisException) {

            System.out.println("INVALID AXIS (FieldCentricCase): Enter \"leftx\", \"lefty\" or \"rightx\".");
            System.out.println();
            theString = "null";
        }

        this.angleDeg = angleDeg;
        this.angleRad = Math.toRadians(angleDeg);
        this.joystickValue = joystickValue;
        this.joystickAxis = joystickAxis;
    }

    public FieldCentricCase(double angleDeg, String joystickAxis) {

        try {

            checkJoystickAxis(joystickAxis);
        }

        catch (Exception InvalidAxisException) {

            System.out.println("INVALID AXIS (FieldCentricCase): Enter \"leftx\", \"lefty\" or \"rightx\".");
            System.out.println();
            theString = "null";
        }

        this.angleDeg = angleDeg;
        this.angleRad = Math.toRadians(angleDeg);
        this.joystickValue = 1;
        this.joystickAxis = joystickAxis;
    }


    public void initializeCase() {

        switch (joystickAxis) {

            case "lefty":
                forward = joystickValue;
                right = 0;
                clockwise = 0;
                break;

            case "leftx":
                right = joystickValue;
                forward = 0;
                clockwise = 0;

            case "rightx":
                clockwise = joystickValue;
                forward = 0;
                right = 0;
        }

        if (angleDeg > 0) {

            temp = forward * Math.cos(angleRad) + right * Math.sin(angleRad);
            right = -forward * Math.sin(angleRad) + right * Math.cos(angleRad);
            forward = temp;
        }

        if (angleDeg <= 0) {

            temp = forward * Math.cos(angleRad) - right * Math.sin(angleRad);
            right = forward * Math.sin(angleRad) + right * Math.cos(angleRad);
            forward = temp;
        }

        flpower = forward + clockwise + right;
        frpower = forward - clockwise - right;
        blpower = forward + clockwise - right;
        brpower = forward - clockwise + right;

        if (Math.abs(frpower) > max) {

            max = Math.abs(frpower);
        }

        if (Math.abs(blpower) > max) {

            max = Math.abs(blpower);
        }

        if (Math.abs(brpower) > max) {

            max = Math.abs(brpower);
        }

        if (max > 1) {

            flpower /= max;
            frpower /= max;
            blpower /= max;
            brpower /= max;
        }

        if(theString.equals("")) {

            theString = "\n"
                    + "############## CASE ##############" + "\n"
                    + "ANGLE: " + angleDeg + "\t\t\t" + "AXIS: " + joystickAxis + "\n"
                    + "FL: " + direction(flpower) + "\t\t"
                    + "FR: " + direction(frpower) + "\n"
                    + "BL: " + direction(blpower) + "\t\t"
                    + "BR: " + direction(brpower) + "\n"
                    + "#################################";
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

    private String direction(double power) {

        if (power > 0.999) {

            power = 1;
        }

        else if (power < -0.999) {

            power = -1;
        }

        else if (power < 0.001 && power > -0.001) {

            power = 0;
        }

        if (power > 0) {
            return "POS" + " | " + power;
        }

        else if (power < 0) {
            return "NEG" + " | " + power;
        }

        else {
            return "ZERO" + " | " + power;
        }
    }

    @Override
    public String toString() {

        return theString;
    }
}
