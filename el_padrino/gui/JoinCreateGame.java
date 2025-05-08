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

            // Show game code to user
            JLabel codeLabel = new JLabel("Game Code (Share this): " + gameCode);

            // Start the host client
            new Thread(() -> {
                Client client = new Client();
                client.start(Integer.parseInt(gameCode), username);
            }).start();

            JLabel maxLabel = new JLabel("Max Players:");
            JSpinner maxPlayers = new JSpinner(new SpinnerNumberModel(4, 2, 20, 1));

            JLabel blackLabel = new JLabel("Black Players:");
            JSpinner blackPlayers = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

            JButton createBtn = new JButton("Create");

            createBtn.addActionListener(e -> {
                int max = (int) maxPlayers.getValue();
                int black = (int) blackPlayers.getValue();

                frame.dispose();
                new WaitingList(username, max);
            });

            frame.add(codeLabel);
            frame.add(new JLabel());  // filler
            frame.add(maxLabel);
            frame.add(maxPlayers);
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

                    // Start client in a thread
                    new Thread(() -> {
                        try {
                            Client client = new Client();
                            client.start(code, username);
                            Thread.sleep(1000); // wait a moment to receive data
                            SwingUtilities.invokeLater(() ->
                                    new WaitingList(username, Client.connectedPlayers)
                            );
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame, "No game found with that code.", "Error", JOptionPane.ERROR_MESSAGE);
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