/*
 * Matt Gaetano and Alex Rogoff
 */

import java.awt.*;

public abstract class CircleObject {
    private int startX, startY, x, y, radius, mass;
    private Color color;
    private double maxSpeed;
    private Vector velocity;

    /*
     * Constructor
     */
    public CircleObject(int x, int y, double angle, double speed, double mSpeed, int radius, int mass, Color color) {
        startX = x;
        startY = y;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
        velocity = new Vector(angle, speed);
        maxSpeed = mSpeed;
    }

    /*
     * Draws the object and gives it a "3D" look.
     */
    public void draw(Graphics g) {
        g.setColor(getColor().brighter());
        g.fillOval(x - radius - 1, y - radius + 2, radius * 2, radius * 2);
        g.setColor(Color.white);
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
//        g.fillRect(x, y - radius * 2, 1, radius * 4); //for debug purposes; adds a vertical line through center of object
//        g.fillRect(x - radius * 2, y, radius * 4, 1); //for debug purposes; adds a horizontal line through center of object
//        g.fillArc(x - 50, y - 50, 100, 100, (int)(Math.toDegrees(getAngle())) - 1, 2); //for debug purposes; adds a line in the direction of the object's movement.
    }

    public abstract void move();

    public abstract double getSpeedX();

    public abstract double getSpeedY();

    public void setVelocity(Vector v) {
        velocity = v;
    }

    public void setAngle(double angle) {
        velocity.setAngle(angle);
    }

    public void setSpeed(double speed) {
        velocity.setSpeed(speed);
    }

    public double getAngle() {
        return velocity.getAngle();
    }

    public double getSpeed() {
        return velocity.getSpeed();
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public Color getColor() {
        return color;
    }

    /*
     * Sets speed to "maxSpeed" if the object's speed exceeds "maxSpeed".
     */
    public void checkSpeed() {
        if (velocity.getSpeed() > maxSpeed) {
            velocity.setSpeed(maxSpeed);
        }
    }

    /*
     * Sets the position of the object to its starting position.
     */
    public void reset() {
        x = startX;
        y = startY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getRadius() {
        return radius;
    }

    public int getMass() {
        return mass;
    }
}