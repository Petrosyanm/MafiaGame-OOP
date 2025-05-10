package src.network;

import gui.GameWindow;
import gui.WaitingList;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {

    public static int connectedPlayers = 1;
    public static PrintWriter out;

    public void start(int worldCode, String username) {
        String localIP = IPAdress.getPrivateIP();
        String serverIP = localIP.substring(0, localIP.lastIndexOf(".") + 1) + worldCode;
        int port = 1234;

        try (
                Socket socket = new Socket(serverIP, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out = output;
            output.println("USERNAME:" + username);

            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {

                if (serverResponse.startsWith("CONNECTED_PLAYERS:")) {
                    continue;
                }

                else if (serverResponse.contains("|")) {
                    String[] parts = serverResponse.split("\\|");
                    final int index = Integer.parseInt(parts[0]);
                    final String myRole = parts[1];
                    final String[] roleList = parts[2].split(",");

                    SwingUtilities.invokeLater(() -> {
                        GameWindow window = new GameWindow();
                        window.startFromRoleData(index, myRole, roleList);
                    });
                    continue;
                }

                else if (serverResponse.startsWith("NIGHT_CHAT:")) {
                    boolean mafia = serverResponse.endsWith("true");
                    SwingUtilities.invokeLater(() -> {
                        GameWindow.setChatEnabled(mafia);
                        GameWindow.setMafiaVotingEnabled(mafia);   // Mafia can vote
                        GameWindow.setSherifCheckEnabled(true);    // Sherif can check at night
                    });
                }

                else if (serverResponse.equals("ENABLE_MAFIA_VOTING")) {
                    SwingUtilities.invokeLater(() -> {
                        GameWindow.setSherifCheckEnabled(false);  // Lock Sherif
                        GameWindow.setMafiaVotingEnabled(true);   // Unlock Mafia
                    });
                }

                else if (serverResponse.equals("DISABLE_SHERIF")) {
                    SwingUtilities.invokeLater(() -> {
                        GameWindow.setSherifCheckEnabled(false);  // 🔒 disable Sherif after timer
                    });
                }

                else if (serverResponse.equals("CLEAR_CHAT")) {
                    SwingUtilities.invokeLater(GameWindow::clearChat);
                }

                else if (serverResponse.equals("DAY_START")) {
                    SwingUtilities.invokeLater(() -> {
                        GameWindow.setChatEnabled(true);
                        GameWindow.enableVoting();
                    });
                }

                else if (serverResponse.startsWith("ELIMINATED:")) {
                    int index = Integer.parseInt(serverResponse.substring("ELIMINATED:".length()));
                    SwingUtilities.invokeLater(() -> GameWindow.eliminatePlayer(index));
                }

                else if (serverResponse.startsWith("CHAT:")) {
                    String msg = serverResponse.substring("CHAT:".length());
                    SwingUtilities.invokeLater(() -> GameWindow.appendMessage(msg));
                }

                else if (serverResponse.startsWith("NEW_PLAYER:")) {
                    String newName = serverResponse.substring("NEW_PLAYER:".length());
                    SwingUtilities.invokeLater(() -> WaitingList.addPlayerToAll(newName));
                }

                else if (serverResponse.startsWith("GAME_OVER:")) {
                    String winner = serverResponse.split(":")[1];
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "🎉 Game Over! " + winner + "s win!");
                        System.exit(0);
                    });
                }

                else if (serverResponse.startsWith("SHERIF_RESULT:")) {
                    String[] parts = serverResponse.split(":");
                    int targetIndex = Integer.parseInt(parts[1]);
                    String role = parts[2];
                    SwingUtilities.invokeLater(() -> GameWindow.showSherifResult(targetIndex, role));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Connection failed: " + e.getMessage());
        }
    }
}
