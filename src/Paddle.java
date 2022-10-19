/*
 * Matt Gaetano and Alex Rogoff
 */

import java.awt.*;

public abstract class Paddle extends CircleObject {
    /*
     * Constructor
     */
    public Paddle(int x, int y, double maxSpeed, int radius) {
        super(x, y, 0, 0, maxSpeed, radius, 50, Color.RED.darker());
    }

    public abstract void move();

    public abstract double getSpeedX();

    public abstract double getSpeedY();

    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor().darker());
        g.fillOval(getX() - getRadius() + 3, getY() - getRadius() + 3, (getRadius() - 3) * 2, (getRadius() - 3) * 2);
        g.setColor(getColor());
        g.fillOval(getX() - getRadius() + 12, getY() - getRadius() + 12, (getRadius() - 12) * 2, (getRadius() - 12) * 2);
        g.setColor(Color.white.darker());
        g.drawOval(getX() - getRadius() + 14, getY() - getRadius() + 8, (getRadius() - 12) * 2, (getRadius() - 12) * 2);
        g.setColor(getColor());
        g.fillOval(getX() - getRadius() + 14, getY() - getRadius() + 8, (getRadius() - 12) * 2, (getRadius() - 12) * 2);
    }
}