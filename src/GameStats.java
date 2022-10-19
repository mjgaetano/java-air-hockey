// Represents current Game Stats
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameStats extends JPanel
{
    private JLabel p1ScoreText, p2ScoreText;
    private String[] highScore;
    private JavaArcade game;

    // Constructor
    public GameStats(JavaArcade t)
    {
        super(new GridLayout(2, 4, 10, 0));
        setBorder(new EmptyBorder(0, 0, 5, 0));
        Font gameNameFont = new Font("Monospaced", Font.BOLD, 24);

        JLabel gName = new JLabel(" " + t.getGameName());

        gName.setForeground(Color.red);
        gName.setFont(gameNameFont);
        add(gName);

        highScore = t.getHighScore().split(": ");

        add(new JLabel(" Current High Scorer (vs. Computer): " + highScore[0]));
        add(new JLabel(""));
        add(new JLabel(" Current High Score (vs. Computer): " + highScore[1]));

        game = t;
    }

    public void gameOver(int points)
    {
        if(points > Integer.parseInt(highScore[1]) && !game.isTwoPlayer()) {
            String s = (String)JOptionPane.showInputDialog(this, "You are the new high scorer. Congratulations!\n Enter your name: ", "High Score", JOptionPane.PLAIN_MESSAGE, null, null,"name");

            try {
                PrintWriter writer = new PrintWriter(new File("highScore.txt"));
                writer.println(s + ": " + points);
                writer.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }
}