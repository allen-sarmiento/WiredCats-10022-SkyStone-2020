package FieldCentric;

public class FieldCentricTester {

    public static void main(String[] args) {

        System.out.println();

        // Create an ideal case and an experimental case
        // Joystick axis must be "leftx", "lefty", or "rightx"
        IdealCase ideal0 = new IdealCase(0, "lefty");
        FieldCentricCase case0 = new FieldCentricCase(0, "lefty");

        // Initialize cases
        case0.initializeCase();
        ideal0.initializeIdeal();

        // Print and compare cases
        System.out.println(case0);
        System.out.println(ideal0);
    }
}