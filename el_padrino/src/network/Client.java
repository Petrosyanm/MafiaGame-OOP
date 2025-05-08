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

        System.out.println("serverIP: " + serverIP);

        try (
                Socket socket = new Socket(serverIP, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Send username to the server
            output.println("USERNAME:" + username);

            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = input.readLine()) != null) {
                        if (serverResponse.startsWith("CONNECTED_PLAYERS:")) {
                            String[] parts = serverResponse.split(":");
                            if (parts.length == 2) {
                                connectedPlayers = Integer.parseInt(parts[1].trim());
                                System.out.println("Connected players: " + connectedPlayers);
                            }
                        } else if (serverResponse.startsWith("NEW_PLAYER:")) {
                            String newPlayer = serverResponse.substring("NEW_PLAYER:".length());
                            System.out.println("New player joined: " + newPlayer);
                            if (WaitingList.instance != null) {
                                SwingUtilities.invokeLater(() -> WaitingList.instance.addPlayer(newPlayer));
                            }
                        } else {
                            System.out.println("Server: " + serverResponse);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Optional: local console input for manual messages (can be removed)
            String userInput;
            while ((userInput = console.readLine()) != null) {
                output.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}