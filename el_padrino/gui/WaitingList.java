
package gui;

import javax.swing.*;
import java.awt.*;

public class WaitingList extends JFrame {
    private JPanel playerPanel;
    private JLabel statusLabel;
    private int maxPlayers;
    private int currentPlayers = 0;

    public WaitingList(String initialUsername, int maxPlayers) {
        this.maxPlayers = maxPlayers;

        setTitle("Waiting Room");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Players joined: 0/" + maxPlayers, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(playerPanel);

        add(statusLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addPlayer(initialUsername);
        setVisible(true);
    }

    public void addPlayer(String username) {
        JLabel nameLabel = new JLabel("🧑 " + username);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        playerPanel.add(nameLabel);
        playerPanel.revalidate();
        playerPanel.repaint();

        currentPlayers++;
        updateStatus();

        if (currentPlayers == maxPlayers) {
            startGame();
        }
    }

    private void updateStatus() {
        statusLabel.setText("Players joined: " + currentPlayers + "/" + maxPlayers);
    }

    private void startGame() {
        JOptionPane.showMessageDialog(this, "All players joined. Starting the game!");
        dispose();
        GameWindow newWindow = new GameWindow();
        newWindow.startTheGame();
    }
}
