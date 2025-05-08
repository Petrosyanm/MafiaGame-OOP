package src.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private final List<PrintWriter> clientWriters = Collections.synchronizedList(new ArrayList<>());
    private final Map<PrintWriter, String> playerNames = Collections.synchronizedMap(new HashMap<>());
    public String ip;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            this.ip = IPAdress.getPrivateIP();
            System.out.println("Server IP: " + ip);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New client connected: " + client.getInetAddress());

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                writer.println("Connected to Mafia Game server!");
                writer.println("CONNECTED_PLAYERS: " + clientWriters.size());

                new Thread(new ClientHandler(client, writer)).start();
                // Removed: broadcast("A new player has joined.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;

        ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received: " + inputLine);

                    if (inputLine.startsWith("USERNAME:")) {
                        String username = inputLine.substring("USERNAME:".length()).trim();
                        playerNames.put(out, username);
                        broadcast("NEW_PLAYER:" + username);
                        System.out.println("Registered username: " + username);
                    } else {
                        broadcast("Client said: " + inputLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientWriters.remove(out);
                String name = playerNames.remove(out);
                if (name != null) {
                    broadcast("A player left: " + name);
                }
                broadcast("CONNECTED_PLAYERS: " + clientWriters.size());
            }
        }
    }

    private void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }

            // Send player count to all
            String countMsg = "CONNECTED_PLAYERS: " + clientWriters.size();
            for (PrintWriter writer : clientWriters) {
                writer.println(countMsg);
            }
        }
    }
}