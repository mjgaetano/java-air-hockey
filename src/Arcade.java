import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Arcade extends JFrame
{
    public Arcade()
    {
        super("AP Java Arcade");

        JavaArcade game = new Rink(750, 425);

        GameStats display = new GameStats(game); //passing in a JavaArcade, therefore I know I can call getHighScore(), getScore()

        ControlPanel controls = new ControlPanel(game, display); //Also passing in JavaArcade to ControlPanel, I know you will respond to buttons

        game.setDisplay(display); //provides game ability to update display

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(display, BorderLayout.NORTH);
        panel.add((JPanel)game, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        Container c = getContentPane();
        c.add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args)
    {
        Arcade window = new Arcade();
        window.setIconImage(new ImageIcon(("pics\\icon.png")).getImage());
        window.setBounds(25, 25, 900, 600);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }
}