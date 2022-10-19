/*
 * Matt Gaetano and Alex Rogoff
 */

import java.awt.*;

public class AirHockeyTable {
    private static final int LEN = 85, WID = 150, RAD = 35, SCALE = 5;

    public static void paintComponent(Graphics g, int width, int height) {
        // RINK BORDER
        g.setColor(Color.YELLOW);
        g.fillRoundRect((width-(WID + 2) * SCALE) / 2, (height-(LEN + 2) * SCALE) / 2, (WID + 2) * SCALE, (LEN + 2) * SCALE, (RAD + 2) * SCALE, (RAD + 2) * SCALE);

        // RINK
        g.setColor(Color.WHITE);
        g.fillRoundRect((width-WID * SCALE) / 2, (height-LEN * SCALE) / 2, WID * SCALE, LEN * SCALE, RAD * SCALE, RAD * SCALE);

        // BLUE LINES
        g.setColor(Color.BLUE);
        g.fillRect(width / 2 - 89, (height - LEN * SCALE) / 2, 8, LEN * SCALE);
        g.fillRect(width / 2 + 81, (height - LEN * SCALE) / 2, 8, LEN * SCALE);

        //LEFT AND RIGHT GOALS
        g.setColor(Color.RED.darker());
        g.drawOval(width / 2 - 343, height / 2 - 50, 60, 100);
        g.drawOval(width / 2 + 283, height / 2 - 50, 60, 100);
        g.setColor(Color.RED);
        g.drawArc(width / 2 - 357, height / 2 - 60, 120, 120, 270, 180);
        g.drawArc(width / 2 + 237, height / 2 - 60, 120, 120, 270, -180);

        g.setColor(Color.BLUE.brighter());
        g.fillArc(width / 2 - 357, height / 2 - 60, 120, 120, 270, 180);
        g.fillArc(width / 2 + 237, height / 2 - 60, 120, 120, 270, -180);

        // GOAL LINES
        g.setColor(Color.RED);
        g.fillRect(width / 2 - 303, (height - LEN * SCALE) / 2, 6, LEN * SCALE);
        g.fillRect(width / 2 + 297, (height - LEN * SCALE) / 2, 6, LEN * SCALE);

        // CENTER CIRCLE (OUTLINE)
        g.setColor(Color.BLUE);
        g.drawOval(width / 2 - 60, height / 2 - 60, 120, 120);

        // FACE OFF ZONE THING //UPPER LEFT
        g.setColor(Color.RED);
        g.fillOval(width / 2 - 210 - 4, height / 2 - 125 - 4, 8, 8);
        // FACE OFF CIRCLE (OUTLINE)
        g.drawOval(width / 2 - 210 - 60, height / 2 - 125 - 60, 120, 120);

        // FACE OFF ZONE THING //LOWER LEFT
        g.setColor(Color.RED);
        g.fillOval(width / 2 - 210 - 4, height / 2 + 125 - 4, 8, 8);
        // FACE OFF CIRCLE (OUTLINE)
        g.drawOval(width / 2 - 210 - 60, height / 2 + 125 - 60, 120, 120);

        // FACE OFF ZONE THING //UPPER RIGHT
        g.setColor(Color.RED);
        g.fillOval(width / 2 + 210 - 4, height / 2 - 125 - 4, 8, 8);
        // FACE OFF CIRCLE (OUTLINE)
        g.drawOval(width / 2 + 210 - 60, height / 2 - 125 - 60, 120, 120);

        // FACE OFF ZONE THING //LOWER RIGHT
        g.setColor(Color.RED);
        g.fillOval(width / 2 + 210 - 4, height / 2 + 125 - 4, 8, 8);
        // FACE OFF CIRCLE (OUTLINE)
        g.drawOval(width / 2 + 210 - 60, height / 2 + 125 - 60, 120, 120);
    }

    public static void paintCenterLine(Graphics g, int width, int height) {
        // CENTER LINE
        g.setColor(Color.white);
        g.fillRect(width / 2 - 5, (height-LEN * SCALE) / 2, 10, LEN * SCALE);
        g.setColor(Color.RED);
        g.fillRect(width / 2 - 4, (height-LEN * SCALE) / 2, 8, LEN * SCALE);

        // CENTER LINE DOTS
        g.setColor(Color.white);
        for (int i = (height-LEN * SCALE) / 2 + 1; i < LEN * SCALE; i += 25) {
            g.fillOval(width / 2 - 3, i, 6, 5);
            g.fillOval(width / 2 - 3, i + 8, 6, 5);
            g.fillOval(width / 2 - 3, i + 16, 6, 5);
        }

        //CENTER DOT
        g.drawOval(width / 2 - 4, height / 2 - 4, 8, 8);
        g.setColor(Color.BLUE);
        g.fillOval(width / 2 - 4, height / 2 - 4, 8, 8);
    }

    public static void paintCrossBar(Graphics g, int width, int height) {
        g.setColor(Color.RED.darker());
        g.fillRect(width / 2 - 305, height / 2 - 47, 3, 94);
        g.fillRect(width / 2 + 302, height / 2 - 47, 3, 94);
    }
}
