package src.network;

import gui.RegisterLogIn;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int port = 1234;

        RegisterLogIn loginWindow = new RegisterLogIn();
        loginWindow.startRegistration();
        boolean loginSuccess = loginWindow.getShowLoginWindow(); 

        if (loginSuccess) {
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
}
