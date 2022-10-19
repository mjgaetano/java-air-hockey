/*
 * Matt Gaetano and Alex Rogoff
 */

public class Vector {
    private double angle, speed;

    /*
     * Constructor
     * Takes in an angle and speed.
     */
    public Vector(double angle, double speed) {
        this.angle = angle;
        this.speed = speed;
    }

    /*
     * Constructor
     * Takes in another Vector as a parameter.
     */
    public Vector(Vector v) {
        angle = v.getAngle();
        speed = v.getSpeed();
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /*
     * Adds Vectors "v1" and "v2".
     */
    public static Vector addVectors(Vector v1, Vector v2) {
        // Calculates the sum of the two x-components.
        double x = Math.cos(v1.getAngle()) * v1.getSpeed() + Math.cos(v2.getAngle()) * v2.getSpeed();
        // Calculates the sum of the two y-components.
        double y = Math.sin(v1.getAngle()) * v1.getSpeed() + Math.sin(v2.getAngle()) * v2.getSpeed();

        // Calculates the speed of the new Vector
        double speed = Math.hypot(x, y);
        // Calculates the angle of the new Vector
        double angle = Math.atan2(y, x);

        return new Vector(angle, speed);
    }

    @Override
    public String toString() {
        return "Angle=" + Math.toDegrees(angle) +
                ", Speed=" + speed;
    }
}