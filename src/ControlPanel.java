// Represents a control panel for the arcade
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.*;

public class ControlPanel extends JPanel
        implements ActionListener
{
    private JavaArcade game;
    private GameStats gStats;
    private JButton startButton, pauseButton, stopButton, replayButton, instructionsButton, creditsButton;

    // Constructor
    public ControlPanel(JavaArcade t, GameStats g)
    {
        game = t;
        gStats = g;

        instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(this);
        add(instructionsButton);

        add(Box.createHorizontalStrut(40));

        startButton = new JButton("Drop Puck");
        startButton.addActionListener(this);
        add(startButton);
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        add(pauseButton);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        add(stopButton);
        replayButton = new JButton("Replay");
        replayButton.addActionListener(this);
        add(replayButton);

        add(Box.createHorizontalStrut(40));

        creditsButton = new JButton("Credits");
        creditsButton.addActionListener(this);
        add(creditsButton);
    }

    // Called when the start button is clicked
    public void actionPerformed(ActionEvent e)
    {

        JButton button = (JButton)e.getSource();

        if (button == startButton)
        {
            pauseButton.setEnabled(true);
            replayButton.setEnabled(true);
            if (startButton.getText().equals("Resume") || startButton.getText().equals("Restart")) {
                startButton.setText("Drop Puck");
            }

            ((JPanel) (game)).requestFocus(); //need to provide the JPanel focus
            game.startGame();
            gStats.repaint();
        }
        else if(button == pauseButton)
        {
            game.pauseGame();
            startButton.setText("Resume");
            repaint();

        }
        else if(button == stopButton)
        {
            game.stopGame();
            ((Rink)game).stopMusic();
            if (!game.running()) {
                pauseButton.setEnabled(false);
                gStats.gameOver(game.getPoints());
                gStats.repaint();
                startButton.setEnabled(true);
                startButton.setText("Restart");
            } else {
                startButton.setText("Drop Puck");
            }
            startButton.setEnabled(true);
            replayButton.setEnabled(true);
            repaint();
        }
        else if (button == replayButton)
        {
            game.startReplay();
        }
        else if(button == creditsButton)
        {
            String credits = game.getCredits();
            JOptionPane.showMessageDialog(this, credits, "Game Credits", JOptionPane.PLAIN_MESSAGE);

        }
        else if(button == instructionsButton)
        {
            String instructions = game.getInstructions();
            JOptionPane.showMessageDialog(this, instructions, "Game Rules", JOptionPane.PLAIN_MESSAGE);

        }
        ((JPanel)(game)).requestFocus();
    }

}