package src.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private final List<PrintWriter> clientWriters = new ArrayList<>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New client connected: " + client.getInetAddress());

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                writer.println("Connected to Mafia Game server!");

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
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    broadcast("Client said: " + inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

}
