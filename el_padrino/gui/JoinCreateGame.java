
package gui;

import game.Game;
import game.GameStorage;
import game.player.Player;
import src.network.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JoinCreateGame {
    private ArrayList<Game> activeGames = GameStorage.loadGames();
    private String username; // username to be passed to inner classes

    public void showMainMenu(String username) {
        this.username = username;
        JFrame frame = new JFrame("Choose Game Mode");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));
        frame.setLocationRelativeTo(null);

        JButton createButton = new JButton("Create Game");
        JButton joinButton = new JButton("Join Game");

        createButton.addActionListener(e -> {
            frame.dispose();
            Server server = new Server();

            (new Thread() {
                public void run() {
                    try {
                        System.out.println("Server");
                        server.start(1234);
                    } catch(Exception v) {
                        System.out.println(v);
                    }
                }
            }).start();
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
            frame.setSize(350, 200);
            frame.setLayout(new GridLayout(3, 2));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            String localIP = IPAdress.getPrivateIP();

            Client client = new Client();
            (new Thread() {
                public void run() {
                    try {
                        System.out.println("Client");
                        int worldCode = Integer.parseInt(localIP.substring(localIP.lastIndexOf(".") + 1));
                        client.start(worldCode);
                    } catch(Exception v) {
                        System.out.println(v);
                    }
                }
            }).start();

            JLabel maxLabel = new JLabel("Max Players:");
            JSpinner maxPlayers = new JSpinner(new SpinnerNumberModel(4, 2, 20, 1));

            JLabel blackLabel = new JLabel("Black Players:");
            JSpinner blackPlayers = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

            JButton createBtn = new JButton("Create");

            createBtn.addActionListener(e -> {
                int max = (int) maxPlayers.getValue();
                int black = (int) blackPlayers.getValue();
                int worldCode = Integer.parseInt(localIP.substring(localIP.lastIndexOf(".") + 1));
                System.out.println("Creating game with " + max + " players and " + black + " black players, Code: " + worldCode);

                Game game = new Game(max, black);
                game.setGameCode(worldCode);
                activeGames.add(game);
                GameStorage.saveGames(activeGames);

                frame.dispose();
                new WaitingList(username, max);
            });

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
            String localIP = IPAdress.getPrivateIP();

            Client client = new Client();
            (new Thread() {
                public void run() {
                    try {
                        System.out.println("Client");
                        int worldCode = Integer.parseInt(localIP.substring(localIP.lastIndexOf(".") + 1));
                        client.start(worldCode);
                    } catch(Exception v) {
                        System.out.println(v);
                    }
                }
            }).start();

            JLabel codeLabel = new JLabel("Game Code:");
            JTextField codeField = new JTextField();
            JButton joinBtn = new JButton("Join");

            joinBtn.addActionListener(e -> {
                String codeText = codeField.getText().trim();
                try {
                    int code = Integer.parseInt(codeText);
                    Game selectedGame = null;

                   for(Game game : activeGames){
                       if(game.getGameCode() == code){
                           selectedGame = game;
                           break;
                       }
                   }

                    if (selectedGame == null) {
                        JOptionPane.showMessageDialog(frame, "No game found with that code.", "Error", JOptionPane.ERROR_MESSAGE);
                        frame.dispose();
                        new JoinGameWindow().show();
                    } else {
                        Player[] players = selectedGame.getPlayers();
                        for (int i = 0; i < players.length; i++) {
                            if (players[i] == null) {
                                players[i] = new Player(i);
                                break;
                            }
                        }
                        selectedGame.setPlayers(players);
                        frame.dispose();
                        new WaitingList(username, players.length);
                    }
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
