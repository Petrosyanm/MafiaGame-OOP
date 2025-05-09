package gui;

import src.network.IPAdress;

import javax.swing.*;
import java.awt.*;

public class WaitingList extends JFrame {
    public static WaitingList instance;

    private JPanel playerPanel;
    private JLabel statusLabel;
    private JLabel codeLabel;
    private int maxPlayers;
    private int currentPlayers = 0;

    public WaitingList(String initialUsername, int maxPlayers) {
        this.maxPlayers = maxPlayers;
        WaitingList.instance = this;

        String localIP = IPAdress.getPrivateIP();
        String gameCode = localIP.substring(localIP.lastIndexOf(".") + 1);

//        Frame
        setTitle("Waiting Room");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(30, 30, 50));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

//        Title Label
        JLabel titleLabel = new JLabel("Waiting Room", SwingConstants.CENTER);
        titleLabel.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

//        Status Title Label
        statusLabel = new JLabel("Players joined: 0/" + maxPlayers, SwingConstants.CENTER);
        titleLabel.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, gbc);


//       Player Panel
        playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(playerPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

//       Game Code Label
        codeLabel = new JLabel("Game Code: " + gameCode, SwingConstants.CENTER);
        codeLabel.setForeground(Color.GRAY);
        codeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(codeLabel, gbc);

        addPlayer(initialUsername);
        setVisible(true);
    }

    public void addPlayer(String username) {
//        Player Name Label
        JPanel playerNamePanel = new JPanel();
        playerNamePanel.setOpaque(false);
        playerNamePanel.setPreferredSize(new Dimension(280, 50));
        playerNamePanel.setMaximumSize(new Dimension(280, 50));
        playerNamePanel.setMinimumSize(new Dimension(280, 50));

        playerNamePanel.setLayout(new BorderLayout());
        playerNamePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(6, 0, 6, 0),
                BorderFactory.createLineBorder(new Color(50, 50, 70), 1)
        ));

//        Name Label
        JLabel nameLabel = new JLabel("🧑 " + username);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        playerNamePanel.add(nameLabel, BorderLayout.WEST);
        playerPanel.add(playerNamePanel);
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

        GameWindow gameWindow = new GameWindow();
        gameWindow.startTheGame();
    }
}