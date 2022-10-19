/*
 * Matt Gaetano and Alex Rogoff
 */

public class Player extends Paddle {
    private double xSpeed, ySpeed;
    private final int RINKXMIN = 140, RINKXMAX = 735, RINKYMIN = 15, RINKYMAX = 440;
    private boolean upAccel, downAccel, leftAccel, rightAccel;
    private final double FRICTION = .9;

    /*
     * Constructor
     */
    public Player(int x, int y, int radius) {
        super(x, y, 10, radius);
    }

    public void move() {
        moveX();
        moveY();
        setAngle(Math.atan2(-getSpeedY(), getSpeedX()));
    }

    public double getSpeedX() {
        return xSpeed;
    }

    public double getSpeedY() {
        return ySpeed;
    }

    @Override
    public double getSpeed() {
        return Math.hypot(getSpeedX(), getSpeedY());
    }

    /*
     * Increases the speed along the x-axis if "leftAccel" or "rightAccel" are true.
     * Limits the x-axis speed to the max speed.
     * Prevents the paddle from moving outside of the left and right boundaries.
     */
    private void moveX() {
        if (leftAccel) {
            setSpeedX(getSpeedX() - 2);
        } else if (rightAccel) {
            setSpeedX(getSpeedX() + 2);
        } else {
            setSpeedX(getSpeedX() * FRICTION);
        }

        if (getSpeedX() >= getMaxSpeed()) {
            setSpeedX(getMaxSpeed());
        } else if (getSpeedX() <= -getMaxSpeed()) {
            setSpeedX(-getMaxSpeed());
        }

        setX(getX() + (int)getSpeedX());

        if (getX() - getRadius() <= RINKXMIN) {
            setSpeedX(0);
            setX(RINKXMIN + getRadius());
        } else if (getX() + getRadius() >= RINKXMAX) {
            setSpeedX(0);
            setX(RINKXMAX - getRadius());
        }
    }

    /*
     * Increases the speed along the y-axis if "upAccel" or "downAccel" are true.
     * Limits the y-axis speed to the max speed.
     * Prevents the paddle from moving outside of the top and bottom boundaries.
     */
    private void moveY() {
        if (upAccel) {
            setSpeedY(getSpeedY() - 2);
        } else if (downAccel) {
            setSpeedY(getSpeedY() + 2);
        } else {
            setSpeedY(getSpeedY() * FRICTION);
        }

        if (getSpeedY() >= getMaxSpeed()) {
            setSpeedY(getMaxSpeed());
        } else if (getSpeedY() <= -getMaxSpeed()) {
            setSpeedY(-getMaxSpeed());
        }

        setY(getY() + (int)getSpeedY());

        if (getY() - getRadius() <= RINKYMIN) {
            setSpeedY(0);
            setY(RINKYMIN + getRadius());
        } else if (getY() + getRadius() >= RINKYMAX) {
            setSpeedY(0);
            setY(RINKYMAX - getRadius());
        }
    }

    public void setSpeedX(double x) {
        xSpeed = x;
    }

    public void setSpeedY(double y) {
        ySpeed = y;
    }

    public void setUpAccel(boolean upAccel) {
        this.upAccel = upAccel;
    }

    public void setDownAccel(boolean downAccel) {
        this.downAccel = downAccel;
    }

    public void setLeftAccel(boolean leftAccel) {
        this.leftAccel = leftAccel;
    }

    public void setRightAccel(boolean rightAccel) {
        this.rightAccel = rightAccel;
    }
}