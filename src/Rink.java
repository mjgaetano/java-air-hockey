/*
 * Matt Gaetano and Alex Rogoff
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Rink extends JPanel implements KeyListener, ActionListener, JavaArcade {
    private final int RINKXMIN = 140, RINKXMAX = 735, RINKYMIN = 15, RINKYMAX = 440,
                    LEFT = 0, RIGHT = 1;

    private ArrayList<Position> positions;

    private Player p1;
    private Paddle p2;
    private Puck puck;
    private GameStats display;

    private int width, height, leftPoints, rightPoints;
    private static int count;
    private boolean running, // Will be true for the entirety of the game until the game is stopped by the user.
                    paused, // Will be true if either the game or the replay is paused.
                    stopped, // Will be true when play is stopped -
                             // either after a goal or when the game is being restarted
                    twoPlayer, replay;

    private int interval = 16;
    private Timer timer = new Timer(interval, this);

    private Clip goal, goal2, paddleHit, start, reset, boardsHit, postHit;

    private BufferedImage homeImg, awayImg;
    private String home, away;

    /*
     * Constructor
     */
    public Rink(int width, int height) {
        this.width = width;
        this.height = height;
        addKeyListener(this);

        p1 = new Player(230, 230, 20);
        puck = new Puck(437, 228, 0, 0, 16);

        restartGame();
    }

    /*
    ------------------------------------------------------------------------------------------------------------------
    Start of interface methods
     */

    /*
     * Returns the running state of the game.
     */
    public boolean running() {
        return running;
    }

    /*
     * Starts the game and timer. Stops goal sounds.
     */
    public void startGame() {
        if (!running) {
            restartGame();
            running = true;
            paused = true;
        } else if (paused) {
            timer.start();
            paused = false;
        } else if (stopped) {
            positions.clear();
            timer.setDelay(interval);
            timer.restart();
            stopped = false;
        }
    }

    /*
     * Prompts the user for speed of replay and starts timer.
     */
    public void startReplay() {
        if (!replay) {
            paused = true;
            timer.stop();
            double factor;

            Object[] options = {"1.5x Slower", "2x Slower"};
            int replaySpeed = JOptionPane.showOptionDialog(null, "Select speed of replay:", "Replay Speed",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);

            switch (replaySpeed) {
                case 0:
                    factor = 1.5;
                    break;
                case 1:
                    factor = 2;
                    break;
                default:
                    factor = 1;
            }
            timer = new Timer((int) (interval * factor), this);
            timer.start();
            running = true;
            replay = true;
            count = 0;
        }
    }

    /*
     * Returns the state of the twoPlayer field.
     */
    public boolean isTwoPlayer() {
        return twoPlayer;
    }

    /*
     * Returns name of the game.
     */
    public String getGameName() {
        return "Air Hockey";
    }

    /*
     * Stops timer and sets paused to true.
     */
    public void pauseGame() {
        timer.stop();
        paused = true;
        repaint();
    }

    /*
     * Returns String of the instructions depending on twoPlayer.
     */
    public String getInstructions() {
        if (twoPlayer) {
            return "Player 1: Left Side" +
                    "\nPlayer 2: Right Side" +
                    "\n" +
                    "\nClick \"Drop Puck\" to start." +
                    "\nAfter every goal, click \"Drop Puck\" to continue." +
                    "\nClick \"Replay\" to view the past 6.5 seconds" +
                    "\nIf you click replay in the middle of play, you will need to resume" +
                    "\nplay at the end of the replay by clicking \"Drop Puck\" or \"Resume\"" +
                    "\n" +
                    "\nTwo Player Controls:" +
                    "\n" +
                    "\nPlayer 1:" +
                    "\nW - Up" +
                    "\nA - Left" +
                    "\nS - Right" +
                    "\nD - Down" +
                    "\n" +
                    "\nPlayer 2:" +
                    "\nUp Arrow - Up" +
                    "\nLeft Arrow - Left" +
                    "\nRight Arrow - Right" +
                    "\nDown Arrow - Down";
        } else {
            return "Player 1: Left Side" +
                    "\nCPU: Right Side" +
                    "\n" +
                    "\nClick \"Drop Puck\" to start." +
                    "\nAfter every goal, click \"Drop Puck\" to continue." +
                    "\nClick \"Replay\" to view the past 6.5 seconds" +
                    "\nAfter every replay, click \"Drop Puck\" to start the game again." +
                    "\nIf you click replay in the middle of play, you will need to resume" +
                    "\nplay at the end of the replay by clicking \"Drop Puck\" or \"Resume\"" +
                    "\n" +
                    "\nSingle Player Controls:" +
                    "\nW - Up" +
                    "\nA - Left" +
                    "\nS - Right" +
                    "\nD - Down";
        }
    }

    public String getCredits() {
        return "By Alex Rogoff & Matt Gaetano";
    }

    /*
     * Reads from file "highscore.txt" and returns String of the high scorer and their score.
     */
    public String getHighScore() {
        String highScore = "";

        try {
            Scanner fileReader = new Scanner(new File("highScore.txt"));
            highScore = fileReader.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return highScore;
    }

    /*
     * Stops the replay if it is running.
     * Otherwise, stops the timer and sets game running state to false.
     */
    public void stopGame() {
        if (replay) {
            stopReplay();
        } else {
            timer.stop();
            running = false;
            reset();
        }
        stopped = true;
    }

    /*
     * Returns the difference in points between the computer and player.
     */
    public int getPoints() {
        return leftPoints - rightPoints;
    }

    public void setDisplay(GameStats d) {
        display = d;
    }

    /*
    End of interface methods
    ------------------------------------------------------------------------------------------------------------------
     */

    /*
     * Provides the user the option of single player or two player.
     * If they select single player, they can select the computer difficulty.
     * Allows the user to select the home and away teams.
     * Sets moving pieces to their starting positions.
     */
    private void restartGame() {
        leftPoints = 0;
        rightPoints = 0;

        Object[] options = {"One Player", "Two Players"};
        int input = JOptionPane.showOptionDialog(null, "Select number of players:", "",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);

        Object[] teamsArray = {"Anaheim Ducks", "Arizona Coyotes", "Boston Bruins", "Buffalo Sabres",  "Calgary Flames",
                "Carolina Hurricanes", "Chicago Blackhawks", "Colorado Avalanche", "Columbus Blue Jackets",
                "Dallas Stars", "Detroit Red Wings", "Edmonton Oilers", "Florida Panthers", "Los Angeles Kings",
                "Minnesota Wild", "Montreal Canadiens", "Nashville Predators", "New Jersey Devils", "New York Islanders",
                "New York Rangers", "Ottawa Senators", "Philadelphia Flyers", "Pittsburgh Penguins", "San Jose Sharks",
                "St. Louis Blues", "Tampa Bay Lightning", "Toronto Maple Leafs", "Vancouver Canucks",
                "Vegas Golden Knights", "Washington Capitals"};

        ArrayList<Object> teams = new ArrayList<>(Arrays.asList(teamsArray));

        home = JOptionPane.showInputDialog(null, "Select home team (player 1):", "",
                JOptionPane.PLAIN_MESSAGE, null, teams.toArray(), teams.get(2)).toString();

        teams.remove(home);

        if (input == 0) {
            away = JOptionPane.showInputDialog(null, "Select away team (CPU):", "",
                    JOptionPane.PLAIN_MESSAGE, null, teams.toArray(), teams.get(25)).toString();

            Object[] options2 = {"Easy", "Medium", "Hard"};
            input = JOptionPane.showOptionDialog(null, "Select computer difficulty:", "",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options2, 0);
            switch (input) {
                case 0:
                    p2 = new CPU(650, 230, 20, 5, puck);
                    break;
                case 1:
                    p2 = new CPU(650, 230, 20, 7.5, puck);
                    break;
                case 2:
                    p2 = new CPU(650, 230, 20, 10, puck);
                    break;
                default:
                    break;
            }
            twoPlayer = false;
        } else {
            away = JOptionPane.showInputDialog(null, "Select away team (player 2):", "",
                    JOptionPane.PLAIN_MESSAGE, null, teams.toArray(), teams.get(25)).toString();

            p2 = new Player(650, 230, 20);
            twoPlayer = true;
        }

        puck.setVelocity(new Vector(Math.random() * 2 * Math.PI, Math.random() * 5 + 1));

        running = true;
        paused = true;
        stopped = false;

        loadSounds();

        loadPics();

        positions = new ArrayList<>(200);
        replay = false;

        repaint();
    }

    /*
     * Draws the ice surface, logos, and goals as well as the paddles and the puck.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(homeImg, 10, 10, 40, 40, this);
        g.drawImage(awayImg, getWidth() - 50, 10, 40, 40, this);

        g.setColor(Color.white);
        g.setFont(new Font("Monospaced", Font.BOLD, 30));
        g.drawString("" + leftPoints, 20, 80);
        g.drawString("" + rightPoints, getWidth() - 40, 80);

        AirHockeyTable.paintComponent(g, getWidth(), getHeight());

        if (home.equals("Tampa Bay Lightning")) {
            home = "Tampa Bay Center";
            loadPics();
        }
        g.drawImage(homeImg, getWidth() / 2 - 60, getHeight() / 2 - 60, 120, 120, this);

        AirHockeyTable.paintCenterLine(g, getWidth(), getHeight());

        puck.draw(g);
        p1.draw(g);
        p2.draw(g);

        AirHockeyTable.paintCrossBar(g, getWidth(), getHeight());

        if (replay) {
            if (count % 10 == 0 || count % 10 == 1 || count % 10 == 2 || count % 10 == 3 || count % 10 == 4) {
                g.setColor(Color.RED);
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                g.drawString("Instant Replay", width - 205, height + 7);
            }
        }

        if (paused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.BOLD, 10));
            g.drawString("Paused", 820, 440);
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (replay) {
            actionPerformedReplay();
        } else {
            actionPerformedLive();
        }
    }

    /*
     * Moves each paddle and the puck to a location given by the ArrayList "positions".
     */
    private void actionPerformedReplay() {
        if (positions.size() > count) {
            p1.setX(positions.get(count).getPlayerX());
            p1.setY(positions.get(count).getPlayerY());
            p2.setX(positions.get(count).getCompX());
            p2.setY(positions.get(count).getCompY());
            puck.setX(positions.get(count).getPuckX());
            puck.setY(positions.get(count).getPuckY());
            count++;
            repaint();
        } else {
            stopReplay();
        }
    }

    /*
     * Resets the timer back to the normal speed and sets "replay" to false.
     */
    private void stopReplay() {
        timer.stop();
        timer.setDelay(interval);
        if (stopped) {
            reset();
        }
        replay = false;
    }

    /*
     * Calls the paddle and puck "move" methods.
     * Ensures the paddles and puck are inside the boundaries of the rink.
     * Plays the sounds of puck-to-paddle, puck-to-edge, or puck-to-post collision.
     * Check if the puck is inside the goal. If it is, "score" method is called.
     */
    private void actionPerformedLive() {
        addPositions();
        resetSounds();

        p1.move();
        if (p1.getX() - p1.getRadius() <= RINKXMIN) {
            p1.setX(RINKXMIN + p1.getRadius());
        } else if (p1.getX() + p1.getRadius() >= (RINKXMAX + RINKXMIN) / 2) {
            p1.setX((RINKXMAX + RINKXMIN) / 2 - p1.getRadius());
        }

        p2.move();
        if (p2.getX() + p2.getRadius() >= RINKXMAX) {
            p2.setX(RINKXMAX - p2.getRadius());
        } else if (p2.getX() - p2.getRadius() <= (RINKXMAX + RINKXMIN) / 2) {
            p2.setX((RINKXMAX + RINKXMIN) / 2 + p2.getRadius());
        }

        puck.move();
        if (puck.getSpeed() > 0) {
            start.start();
        }

        p1.setVelocity(new Vector(Math.atan2(p1.getSpeedY(), p1.getSpeedX()), p1.getSpeed()));
        p2.setVelocity(new Vector(Math.atan2(p2.getSpeedY(), p2.getSpeedX()), p2.getSpeed()));

        if (puck.paddleCollision(p1) || puck.paddleCollision(p2)) {
            paddleHit.start();
        }

        if (puck.postCollision(740, 182) || puck.postCollision(740, 274) ||
                puck.postCollision(135, 182) || puck.postCollision(135, 274)) {
            postHit.start();
        }

        repaint();

        if (puck.getSpeedX() != 0 || puck.getSpeedY() != 0) {
            double DECEL = 0.998;
            puck.setSpeed(puck.getSpeed() * DECEL);
        }

        if (puck.checkBounce(RINKXMIN, RINKXMAX, RINKYMIN, RINKYMAX)) {
            boardsHit.start();
        }

        if (puck.checkGoal()) {
            if (puck.getX() + (puck.getRadius()) < RINKXMIN) {
                score(LEFT);
            } else if (puck.getX() - puck.getRadius() > RINKXMAX) {
                score(RIGHT);
            }
        }
    }

    /*
     * Adds the locations of the paddles and puck to the ArrayList of positions
     * to be used by the replay function.
     */
    private void addPositions() {
        if (positions.size() >= 200) {
            positions.remove(0);
        }
        positions.add(new Position(p1.getX(), p1.getY(), p2.getX(), p2.getY(), puck.getX(), puck.getY()));
    }

    /*
     * Stops timer and runs through the goal procedure.
     */
    private void score(int side) {
        stopped = true;
        timer.stop();

        /*
         * Restarts the timer with the replay interval and sets replay to true.
         */
        timer = new Timer((interval * 2), this);
        timer.start();
        count = 0;
        replay = true;
        stopMusic();

        /*
         * Plays the goal horn and song for the appropriate team.
         * Adds a point to the appropriate team.
         */
        if (side == RIGHT) {
            goal.start();
            leftPoints++;
            puck.setX(760);
            puck.setY(230);
        } else {
            goal2.start();
            rightPoints++;
            puck.setX(115);
            puck.setY(230);
        }

        addPositions();

        if (!start.isRunning()) {
            start.setFramePosition(0);
        }
    }

    /*
     * Loads the logos of the home and away teams.
     */
    private void loadPics() {
        try {
            homeImg = ImageIO.read(new File("pics\\logos\\" + home + ".png"));
            awayImg = ImageIO.read(new File("pics\\logos\\" + away + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Loads the goal sounds for each team as well as other sound effects.
     */
    private void loadSounds() {
        try {
            //getClass().getResourceAsStream("/filename.txt")
            goal = AudioSystem.getClip();
            goal.open(AudioSystem.getAudioInputStream(new File("sounds/songs/" + home + ".wav")));
            goal.setFramePosition(0);

            paddleHit = AudioSystem.getClip();
            paddleHit.open(AudioSystem.getAudioInputStream(new File("sounds\\paddleHit.wav")));
            paddleHit.setFramePosition(0);

            start = AudioSystem.getClip();
            start.open(AudioSystem.getAudioInputStream(new File("sounds\\start.wav")));
            start.setFramePosition(0);

            boardsHit = AudioSystem.getClip();
            boardsHit.open(AudioSystem.getAudioInputStream(new File("sounds\\boardsHit.wav")));
            boardsHit.setFramePosition(0);

            goal2 = AudioSystem.getClip();
            goal2.open(AudioSystem.getAudioInputStream(new File("sounds\\songs\\" + away + ".wav")));
            goal2.setFramePosition(0);

            postHit = AudioSystem.getClip();
            postHit.open(AudioSystem.getAudioInputStream(new File("sounds\\post.wav")));
            postHit.setFramePosition(0);

            reset = AudioSystem.getClip();
            reset.open(AudioSystem.getAudioInputStream(new File("sounds\\reset.wav")));
            reset.setFramePosition(0);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /*
     * Sets the frame position of each sound to 0 if they are running.
     */
    private void resetSounds() {
        if (!goal.isRunning()) {
            goal.setFramePosition(0);
        }
        if (!paddleHit.isRunning()) {
            paddleHit.setFramePosition(0);
        }
        if (!boardsHit.isRunning()) {
            boardsHit.setFramePosition(0);
        }
        if (!goal2.isRunning()) {
            goal2.setFramePosition(0);
        }
        if (!postHit.isRunning()) {
            postHit.setFramePosition(0);
        }
        if (!reset.isRunning()) {
            reset.setFramePosition(0);
        }
    }

    /*
     * Resets the paddles and puck.
     * Gives the puck a new velocity vector.
     */
    private void reset() {
        reset.start();
        puck.reset();
        puck.setVelocity(new Vector(Math.random() * 2 * Math.PI, (Math.random() * 5) + 1));
        p1.reset();
        p2.reset();
        repaint();
    }

    public void keyTyped(KeyEvent keyEvent) {

    }

    /*
     * Called when the user presses a key.
     * Changes the player(s) accelerations to true.
     */
    public void keyPressed(KeyEvent keyEvent) {
        if (twoPlayer) {
            Player p = (Player)p2;  //Here we know it's not a CPU; it's a player so we can cast it to a player.
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                p.setUpAccel(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                p.setDownAccel(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                p.setLeftAccel(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                p.setRightAccel(true);
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
            p1.setUpAccel(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            p1.setDownAccel(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
            p1.setLeftAccel(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
            p1.setRightAccel(true);
        }
    }

    /*
     * Called when the user releases a key.
     * Changes the player(s) accelerations to false.
     */
    public void keyReleased(KeyEvent keyEvent) {
        if (twoPlayer) {
            Player p = (Player)p2; //Here we know it's not a CPU; it's a player so we can cast it to a player.
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                p.setUpAccel(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                p.setDownAccel(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                p.setLeftAccel(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                p.setRightAccel(false);
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
            p1.setUpAccel(false);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            p1.setDownAccel(false);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
            p1.setLeftAccel(false);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
            p1.setRightAccel(false);
        }
    }

    public void stopMusic() {
        goal.stop();
        goal2.stop();
    }
}