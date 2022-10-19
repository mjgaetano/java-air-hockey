/*
 * Matt Gaetano and Alex Rogoff
 */

public class CPU extends Paddle {
    private final int RINKXMIN = 140, RINKXMAX = 735;
    private Puck puck;

    /*
     * Constructor
     */
    public CPU(int x, int y, int radius, double maxSpeed, Puck p) {
        super(x, y, maxSpeed, radius);
        puck = p;
    }

    public double getSpeedX() {
        return getSpeed() * Math.cos(getAngle());
    }

    public double getSpeedY() {
        return -(getSpeed() * Math.sin(getAngle()));
    }

    /*
     * Changes the x and y position by the x and y speeds, respectively.
     */
    public void move() {
        setSpeed(getMaxSpeed());
        if (puck.getX() > (RINKXMAX + RINKXMIN) / 2) {
            checkLoc(puck.getX(), puck.getY());
            setX(getX() + (int) (getSpeedX()));
            setY(getY() + (int) (getSpeedY()));
        } else {
            checkLoc(getStartX(), getStartY());
            if (getX() != getStartX() && getY() != getStartY()) {
                setX(getX() + (int) (getSpeedX()));
                setY(getY() + (int) (getSpeedY()));
            }
        }

        checkSpeed();
    }

    /*
     * Changes the angle of the CPU so that it points toward the given x and y position.
     */
    private void checkLoc(int x, int y) {
        int dx = x - getX();
        int dy = getY() - y;

        setAngle(Math.atan2(dy, dx));
    }
}