package gui;

import game.Game;
import game.player.Player;
import src.network.*;

import javax.swing.*;
import java.awt.*;

public class JoinCreateGame {
    private String username;

    public void showMainMenu(String username) {
        this.username = username;
        JFrame frame = new JFrame("Choose Game Mode");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(2, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JButton createButton = new JButton("Create Game");
        JButton joinButton = new JButton("Join Game");

        createButton.addActionListener(e -> {
            frame.dispose();
            Server server = new Server();
            new Thread(() -> server.start(1234)).start();
            new CreateGameWindow().show();
        });

        joinButton.addActionListener(e -> {
            frame.dispose();
            new JoinGameWindow().show();
        });

        frame.add(createButton);
        frame.add(joinButton);
        frame.setVisible(true);
    }

    private class CreateGameWindow {
        public void show() {
            JFrame frame = new JFrame("Create Game");
            frame.setSize(350, 250);
            frame.setLayout(new GridLayout(4, 2));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            String localIP = IPAdress.getPrivateIP();
            String gameCode = localIP.substring(localIP.lastIndexOf(".") + 1);
            JLabel codeLabel = new JLabel("Game Code (Share this): " + gameCode);

            new Thread(() -> {
                Client client = new Client();
                client.start(Integer.parseInt(gameCode), username);
            }).start();

            JLabel totalLabel = new JLabel("Total Players:");
            JSpinner totalPlayers = new JSpinner(new SpinnerNumberModel(4, 2, 20, 1));

            JLabel blackLabel = new JLabel("Black Players:");
            JSpinner blackPlayers = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

            JButton createBtn = new JButton("Create");

            createBtn.addActionListener(e -> {
                int players = (int) totalPlayers.getValue();
                int blacks = (int) blackPlayers.getValue();

                if (players < 4) {
                    JOptionPane.showMessageDialog(frame, "Total players must be at least 4.", "Invalid Player Count", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxAllowedBlacks = players / 2;

                if(blacks > maxAllowedBlacks) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Too many black players. Maximum number allowed is " + maxAllowedBlacks + " for " + players + " total players.",
                            "Invalid Black Player Count",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }


                frame.dispose();
                new WaitingList(username, players);
            });

            frame.add(codeLabel);
            frame.add(new JLabel());
            frame.add(totalLabel);
            frame.add(totalPlayers);
            frame.add(blackLabel);
            frame.add(blackPlayers);
            frame.add(new JLabel());
            frame.add(createBtn);

            frame.setVisible(true);
        }
    }

    private class JoinGameWindow {
        public void show() {
            JFrame frame = new JFrame("Join Game");
            frame.setSize(300, 120);
            frame.setLayout(new GridLayout(2, 2));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JLabel codeLabel = new JLabel("Game Code:");
            JTextField codeField = new JTextField();
            JButton joinBtn = new JButton("Join");

            joinBtn.addActionListener(e -> {
                String codeText = codeField.getText().trim();
                try {
                    int code = Integer.parseInt(codeText);
                    if (code < 0 || code > 255) {
                        JOptionPane.showMessageDialog(frame, "Invalid game code. Must be between 0 and 255.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new Thread(() -> {
                        try {
                            Client client = new Client();
                            client.start(code, username);
                            Thread.sleep(1000);
                            SwingUtilities.invokeLater(() ->
                                    new WaitingList(username, Client.connectedPlayers)
                            );
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame, "No game found at this code. Make sure the room is created first.", "Error", JOptionPane.ERROR_MESSAGE);
                                frame.dispose();
                                new JoinGameWindow().show();
                            });
                        }
                    }).start();

                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Invalid Code", JOptionPane.WARNING_MESSAGE);
                }
            });

            frame.add(codeLabel);
            frame.add(codeField);
            frame.add(new JLabel());
            frame.add(joinBtn);
            frame.setVisible(true);
        }
    }
}