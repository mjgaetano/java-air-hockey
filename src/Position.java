/*
 * Matt Gaetano and Alex Rogoff
 */

public class Position {
    private int playerX, pY, cX, cY, puckX, puckY;

    /*
     * Constructor
     */
    public Position (int playerX, int playerY, int compX, int compY, int puckX, int puckY) {
        this.playerX = playerX;
        pY = playerY;
        cX = compX;
        cY = compY;
        this.puckX = puckX;
        this.puckY = puckY;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return pY;
    }

    public int getCompX() {
        return cX;
    }

    public int getCompY() {
        return cY;
    }

    public int getPuckX() {
        return puckX;
    }

    public int getPuckY() {
        return puckY;
    }
}