package src.network;

import gui.WaitingList;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    public static int connectedPlayers = 1;

    public void start(int worldCode, String username) {
        String localIP = IPAdress.getPrivateIP();
        String serverIP = localIP.substring(0, localIP.lastIndexOf(".") + 1) + worldCode;
        int port = 1234;

        try (
                Socket socket = new Socket(serverIP, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in))
        ) {
            socket.setSoTimeout(2000); // wait max 2 sec for room verification
            String roomCheck = input.readLine();
            if (!"ROOM_ACTIVE".equals(roomCheck)) {
                throw new IOException("No active game room on this server.");
            }
            socket.setSoTimeout(0); // disable timeout after handshake

            output.println("USERNAME:" + username);

            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = input.readLine()) != null) {
                        if (serverResponse.startsWith("CONNECTED_PLAYERS:")) {
                            connectedPlayers = Integer.parseInt(serverResponse.split(":")[1].trim());
                        } else if (serverResponse.startsWith("NEW_PLAYER:")) {
                            String newPlayer = serverResponse.substring("NEW_PLAYER:".length());
                            if (WaitingList.instance != null) {
                                SwingUtilities.invokeLater(() -> WaitingList.instance.addPlayer(newPlayer));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while ((console.readLine()) != null); // keep alive if needed

        } catch (IOException e) {
            throw new RuntimeException("Connection failed: " + e.getMessage());
        }
    }
}