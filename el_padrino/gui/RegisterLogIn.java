package gui;

import user.*;

import java.io.*;
import javax.swing.*;
import java.awt.*;

public class RegisterLogIn {
    public void startRegistration(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int fullWidth = (int) screenSize.getWidth();
        int fullHeight = (int) screenSize.getHeight();

        int windowWidth = (int) (fullWidth * 0.4);
        int windowHeight = (int) (fullHeight * 0.6);

//       Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);

//        Main Frame
        JFrame frame = new JFrame("Mafia Game");
        frame.setSize(windowWidth, windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

//       Main Title
        JLabel titleLabel = new JLabel("Mafia Game", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

//        Login / Register Title
        JLabel loginRegisterLabel = new JLabel("Login / Register", SwingConstants.CENTER);
        loginRegisterLabel.setForeground(Color.WHITE);
        loginRegisterLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loginRegisterLabel.setHorizontalAlignment(JLabel.CENTER);


//        Inputs Panel
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setOpaque(false);

        Dimension labelSize = new Dimension((int)(windowWidth * 0.6), 40);
        Dimension fieldSize = new Dimension((int)(windowWidth * 0.6), 40);
        Dimension buttonSize = new Dimension((int)(windowWidth * 0.4), 40);

//        Username Label
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 15));
        userLabel.setPreferredSize(labelSize);
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setHorizontalAlignment(JLabel.LEFT);
        inputPanel.add(userLabel);

//        Username Field
        JTextField usernameField = new JTextField();
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setPreferredSize(fieldSize);
        usernameField.setBackground(new Color(50, 50, 70));
        usernameField.setForeground(Color.WHITE);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(usernameField);

//        Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        passwordLabel.setPreferredSize(labelSize);
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordLabel.setHorizontalAlignment(JLabel.LEFT);
        inputPanel.add(passwordLabel);

//        Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setBackground(new Color(50, 50, 70));
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(passwordField);

        inputPanel.add(Box.createVerticalStrut(20));

//       Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);

//        Create Account Button
        JButton createButton = new JButton("Create Account");
        createButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createButton.setPreferredSize(buttonSize);
        createButton.setBackground(new Color(60, 60, 90));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setFocusPainted(false);

        createButton.addActionListener(e -> createAccount(
                usernameField.getText(),
                String.valueOf(passwordField.getPassword()),
                frame
        ));


//        Login Button
        JButton loginButton = new JButton("Log In");
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(buttonSize);
        loginButton.setBackground(new Color(60, 60, 90));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> logIn(
                usernameField.getText(),
                String.valueOf(passwordField.getPassword()),
                frame
        ));

        buttonsPanel.add(createButton);
        buttonsPanel.add(loginButton);
        inputPanel.add(buttonsPanel);

//        Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerPanel.add(inputPanel, centerGbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(titleLabel, gbc);
        mainPanel.add(loginRegisterLabel, gbc);
        mainPanel.add(centerPanel, gbc);
        mainPanel.add(Box.createVerticalStrut(20));


        frame.add(mainPanel);
        frame.setVisible(true);
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
            registerLogIn.startRegistration();
        });
    }
}

