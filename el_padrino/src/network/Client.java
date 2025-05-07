package src.network;

import java.io.*;
import java.net.*;


public class Client {
    public void start(int worldCode) {
        System.out.println(worldCode);
        String localIP = IPAdress.getPrivateIP();
        String serverIP = localIP.substring(0,localIP.lastIndexOf(".")) + worldCode;
        int port = 1234;
        
        try (
            Socket socket = new Socket(serverIP, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Thread for reading server messages
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = input.readLine()) != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String userInput;
            while ((userInput = console.readLine()) != null) {
                output.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
