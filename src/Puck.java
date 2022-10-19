/*
 * Matt Gaetano and Alex Rogoff
 */

import java.awt.*;

public class Puck extends CircleObject {

    /*
     * Constructor
     */
    public Puck(int x, int y, double angle, double speed, int radius) {
        super(x, y, angle, speed, 13, radius, 10, Color.BLACK);
    }

    /*
     * Returns the x-component of the puck's speed.
     */
    public double getSpeedX() {
        return getSpeed() * Math.cos(getAngle());
    }

    /*
     * Returns the y-component of the puck's speed.
     */
    public double getSpeedY() {
        return getSpeed() * -Math.sin(getAngle());
    }

    /*
     * Changes the puck's x-coordinate by its speed along the x-axis.
     * Changes the puck's y-coordinate by its speed along the y-axis.
     */
    public void move() {
        setX(getX() + (int)(getSpeedX()));
        setY(getY() + (int)(getSpeedY()));

        checkSpeed();
    }

    /*
     * Checks if the puck collides with Paddle p.
     * Returns true if they are touching.
     */
    public boolean paddleCollision(Paddle p) {
        int dx = getX() - p.getX();
        int dy = getY() - p.getY();

        // Determines the distance between the puck and paddle.
        double distance = Math.hypot(dx, dy);

        if (distance > getRadius() + p.getRadius()) {
            return false;
        }

        // Determines the angle from the center of the puck to the center of the paddle.
        double tempAngle = Math.atan2(dy, -dx);

        if (p.getSpeed() < 1) {
            setAngle(getAngle() - 2 * tempAngle);

            double offset = 1.5 * (getRadius() + p.getRadius() - distance + 1); //prevents paddle and puck sticking
            p.setSpeed(0);
            setX(getX() - (int)(offset * Math.cos(tempAngle)));
            setY(getY() + (int)(offset * Math.sin(tempAngle)));
        } else {
            // Determines the components of the vector that has angle "tempAngle".
            // In other words, these lines find the speed that the puck hits the paddle,
            // then separates them into components.
            Vector puckComponentP = new Vector(tempAngle, Math.abs(getSpeed() * Math.cos(tempAngle))), // P = primary
                    puckComponentS = new Vector(tempAngle + Math.PI / 2, Math.abs(getSpeed() * Math.sin(tempAngle))), // S = secondary
                    paddleComponentP = new Vector(Math.PI + tempAngle, Math.abs(p.getSpeed() * Math.sin(Math.PI + tempAngle))), // P = primary
                    paddleComponentS = new Vector(tempAngle - Math.PI / 2, Math.abs(p.getSpeed() * Math.cos(Math.PI + tempAngle))); // S = secondary

            int totalMass = getMass() + p.getMass();

            // Uses momentum equations to find the new primary component for the puck, "vecC".
            Vector vecA = new Vector(puckComponentP.getAngle(), puckComponentP.getSpeed() * (getMass() - p.getMass()) / totalMass);
            Vector vecB = new Vector(paddleComponentP.getAngle(), paddleComponentP.getSpeed() * 2 * p.getMass() / totalMass);
            Vector vecC = new Vector(Vector.addVectors(vecA, vecB));

            // Changes puck velocity to new velocity.
            setVelocity(Vector.addVectors(vecC, puckComponentS));

            // Uses momentum equations to find the new primary component for the paddle, "vecC".
            vecA.setSpeed(puckComponentP.getSpeed() * 2 * getMass() / totalMass);
            vecB.setSpeed(paddleComponentP.getSpeed() * (p.getMass() - getMass()) / totalMass);
            vecC = new Vector(Vector.addVectors(vecA, vecB));

            // Changes paddle velocity to new velocity
            p.setVelocity(Vector.addVectors(vecC, paddleComponentS));

            double offset = 1.5 * (getRadius() + p.getRadius() - distance + 1); //prevents paddle and puck sticking
            setX(getX() - (int) (offset * Math.cos(tempAngle)));
            setY(getY() + (int) (offset * Math.sin(tempAngle)));
        }
        return true;
    }

    /*
     * Checks if the puck collides with the goal post at point (x, y).
     * Returns true if it touches a post.
     */
    public boolean postCollision(int x, int y) {
        int dx = getX() - x;
        int dy = getY() - y;

        // Determines the distance between the puck and paddle.
        double distance = Math.hypot(dx, dy);

        if (distance > getRadius()) {
            return false;
        }

        // Determines the angle from the center of the puck to the post.
        double tempAngle = Math.atan2(dy, -dx);

        // Changes the angle so that the puck bounces off the post with
        // and angle equal to the supplement of the angle the puck came at the post with.
        setAngle(getAngle() - 2 * tempAngle);

        double offset = 0.5 * (getRadius() - distance + 1); //prevents paddle and puck sticking
        setX(getX() - (int)(offset * Math.cos(tempAngle)));
        setY(getY() + (int)(offset * Math.sin(tempAngle)));
        return true;
    }

    /*
     * Checks if the puck collides with a border.
     * Returns true if it touches a border.
     */
    public boolean checkBounce(int xMin, int xMax, int yMin, int yMax) { //returns true if goal is scored
        boolean ret = false;

        // Determines if the puck touches the left boundary.
        if (getX() - getRadius() < xMin && !checkGoal()) {
            setX(xMin + getRadius());
            setAngle(Math.PI - getAngle());
            ret = true;
        // Determines if the puck touches the right boundary.
        } else if (getX() + getRadius() >= xMax && !checkGoal()) {
            setX(xMax - getRadius());
            setAngle(Math.PI - getAngle());
            ret = true;
        }

        // Determines if the puck touches the top boundary.
        if (getY() - getRadius() < yMin) {
            setY(yMin + getRadius());
            setAngle(-getAngle());
            ret = true;
        // Determines if the puck touches the bottom boundary.
        } else if (getY() + getRadius() > yMax) {
            setY(yMax - getRadius());
            setAngle(-getAngle());
            ret = true;
        }

        return ret;
    }

    /*
     * Checks if the puck has the appropriate y-coordinate for it to go into the goal.
     */
    public boolean checkGoal() {
        return getY() > 182 && getY() < 274;
    }

    public void draw(Graphics g) {
        super.draw(g);
    }

    @Override
    public void reset() {
        super.reset();
    }
}