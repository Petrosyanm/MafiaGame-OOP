package gui;

import user.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class RegisterLogIn {
    public void startRegistration(){
        JFrame frame = new JFrame("Login Page");
        frame.setSize(400, 250); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel userBox = new JPanel();
        userBox.setLayout(new FlowLayout(FlowLayout.LEFT)); 
        JLabel userLabel = new JLabel("Enter the username:");
        JTextField user = new JTextField(20); 
        userBox.add(userLabel);
        userBox.add(user);
        
        JPanel passwordBox = new JPanel();
        passwordBox.setLayout(new FlowLayout(FlowLayout.LEFT)); 
        JLabel passLabel = new JLabel("Enter the password:");
        JPasswordField password = new JPasswordField(20); 
        passwordBox.add(passLabel);
        passwordBox.add(password);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JButton createNewAccount = new JButton("Create new account");
        JButton logIn = new JButton("Log In to existing account");
        buttonBox.add(createNewAccount);
        buttonBox.add(logIn);

        panel.add(userBox);
        panel.add(passwordBox);
        panel.add(buttonBox);
        frame.add(panel);

        frame.setVisible(true);

        createNewAccount.addActionListener(e -> createAccount(user.getText(),String.valueOf(password.getPassword()),frame));
        logIn.addActionListener(e -> logIn(user.getText(),String.valueOf(password.getPassword()),frame));
    }
    private void createAccount(String username, String userPassword, JFrame frame){
        if (username.isEmpty() || userPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            User user = new User(username, userPassword, true);
            UserDataManaging.saveUser(user); 
            JOptionPane.showMessageDialog(frame, "Account created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            JoinCreateGame lobby = new JoinCreateGame();
            lobby.showMainMenu(username);
        } catch (LogInRegisterException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void logIn(String username, String userPassword, JFrame frame){
        if (username.isEmpty() || userPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            User user = new User(username, userPassword, false);
            UserDataManaging.logIn(username,userPassword); 
            JOptionPane.showMessageDialog(frame, "You logged in successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            JoinCreateGame lobby = new JoinCreateGame();
            lobby.showMainMenu(username);
        } catch (LogInRegisterException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterLogIn registerLogIn = new RegisterLogIn();
            registerLogIn.startRegistration(); //Write on one line??
        });
    }
}

