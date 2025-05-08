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
            ip = IPAdress.getPrivateIP();

            while (true) {
                Socket client = serverSocket.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                writer.println("ROOM_ACTIVE"); // handshake
                writer.println("Connected to Mafia Game server!");
                writer.println("CONNECTED_PLAYERS: " + clientWriters.size());

                new Thread(new ClientHandler(client, writer)).start();
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
                    if (inputLine.startsWith("USERNAME:")) {
                        String username = inputLine.substring("USERNAME:".length()).trim();
                        playerNames.put(out, username);

                        // Send existing players to the new client
                        for (String existing : playerNames.values()) {
                            out.println("NEW_PLAYER:" + existing);
                        }

                        // Inform everyone
                        broadcast("NEW_PLAYER:" + username);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientWriters.remove(out);
                String name = playerNames.remove(out);
                if (name != null) broadcast("A player left: " + name);
                broadcast("CONNECTED_PLAYERS: " + clientWriters.size());
            }
        }
    }

    private void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
                writer.println("CONNECTED_PLAYERS: " + clientWriters.size());
            }
        }
    }
}