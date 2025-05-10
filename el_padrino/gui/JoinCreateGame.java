package gui;

import src.network.*;

import javax.swing.*;
import java.awt.*;

public class JoinCreateGame {
    private String username;

    public void showMainMenu(String username) {
        this.username = username;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int fullWidth = (int) screenSize.getWidth();
        int fullHeight = (int) screenSize.getHeight();

        int windowWidth = (int) (fullWidth * 0.4);
        int windowHeight = (int) (fullHeight * 0.4);

//        Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 10);

//        Main Frame
        JFrame frame = new JFrame("Game Menu");
        frame.setSize(windowWidth, windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


//        Welcome Title
        JLabel welcomeLabel = new JLabel("Hi, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Inputs Panel
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setOpaque(false);

        Dimension buttonSize = new Dimension((int)(windowWidth * 0.4), 40);

//        Create Button
        JButton createButton = new JButton("Create Game");
        createButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createButton.setPreferredSize(buttonSize);
        createButton.setBackground(new Color(60, 60, 90));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setFocusPainted(false);

        createButton.addActionListener(e -> {
            frame.dispose();
            Server server = new Server();
            new Thread(() -> server.start(1234)).start();
            new CreateGameWindow().show();
        });

//       Join Button
        JButton joinButton = new JButton("Join Game");
        joinButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        joinButton.setPreferredSize(buttonSize);
        joinButton.setBackground(new Color(60, 60, 90));
        joinButton.setForeground(Color.WHITE);
        joinButton.setFont(new Font("Arial", Font.BOLD, 16));
        joinButton.setFocusPainted(false);

        joinButton.addActionListener(e -> {
            frame.dispose();
            new JoinGameWindow().show();
        });

//       Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerGbc.insets = new Insets(10, 10, 10, 10);

        inputPanel.add(createButton);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(joinButton);

        centerPanel.add(inputPanel, centerGbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(welcomeLabel, gbc);
        mainPanel.add(centerPanel, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private class CreateGameWindow {
        public void show() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int fullWidth = (int) screenSize.getWidth();
            int fullHeight = (int) screenSize.getHeight();

            int windowWidth = (int) (fullWidth * 0.5);
            int windowHeight = (int) (fullHeight * 0.6);

            String localIP = IPAdress.getPrivateIP();
            String gameCode = localIP.substring(localIP.lastIndexOf(".") + 1);

            new Thread(() -> {
                Client client = new Client();
                client.start(Integer.parseInt(gameCode), username);
            }).start();


            // Main Panel
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(30, 30, 50));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(2, 10, 2, 10);

            // Main Frame
            JFrame frame = new JFrame("Create Game");
            frame.setSize(windowWidth, windowHeight);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // Return Label
            JLabel returnLabel = new JLabel("Return");
            returnLabel.setForeground(Color.GRAY);
            returnLabel.setFont(new Font("Arial", Font.ROMAN_BASELINE, 16));
            returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            returnLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    frame.dispose();
                    showMainMenu(username);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    returnLabel.setForeground(Color.WHITE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    returnLabel.setForeground(Color.GRAY);
                }
            });

            gbc.insets = new Insets(6, 6, 6, 6);

            mainPanel.add(returnLabel, gbc);

            // Title
            JLabel titleLabel = new JLabel("Create Game", SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(JLabel.CENTER);

            // Inputs Panel
            JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            inputPanel.setOpaque(false);

            Dimension labelSize = new Dimension((int)(windowWidth * 0.4), 40);
            Dimension spinnerSize = new Dimension((int)(windowWidth * 0.4), 40);
            Dimension createButtonSize = new Dimension((int)(windowWidth * 0.2), 40);

            // Total Players Label
            JLabel totalLabel = new JLabel("Total Players");
            totalLabel.setPreferredSize(labelSize);
            totalLabel.setForeground(Color.LIGHT_GRAY);
            totalLabel.setHorizontalAlignment(JLabel.LEFT);
            inputPanel.add(totalLabel);

            // Total Players Spinner
            JSpinner totalPlayers = new JSpinner(new SpinnerNumberModel(4, 2, 20, 1));
            totalPlayers.setPreferredSize(spinnerSize);
            customizeSpinner(totalPlayers);
            inputPanel.add(totalPlayers);

            inputPanel.add(Box.createVerticalStrut(20));

            // Create Button
            JButton createButton = new JButton("Create");
            createButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            createButton.setPreferredSize(createButtonSize);
            createButton.setBackground(new Color(60, 60, 90));
            createButton.setForeground(Color.WHITE);
            createButton.setFont(new Font("Arial", Font.BOLD, 16));
            createButton.setFocusPainted(false);
            inputPanel.add(createButton);


            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            GridBagConstraints centerGbc = new GridBagConstraints();
            centerGbc.weightx = 1.0;
            centerGbc.weighty = 1.0;
            centerPanel.add(inputPanel, centerGbc);


            gbc.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(returnLabel, gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            mainPanel.add(titleLabel, gbc);
            mainPanel.add(centerPanel, gbc);


            createButton.addActionListener(e -> {
                int players = (int) totalPlayers.getValue();

//                Validation Logic
                if (players < 4) {
                    JOptionPane.showMessageDialog(frame, "Total players must be at least 4.", "Invalid Player Count", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxAllowedBlacks = players / 2;

                int total = (int) totalPlayers.getValue();
                frame.dispose();
                new WaitingList(username, total);
            });


            frame.add(mainPanel);
            frame.setVisible(true);
        }



        private void customizeSpinner(JSpinner spinner) {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                textField.setBackground(new Color(50, 50, 70));
                textField.setForeground(Color.WHITE);
                textField.setFont(new Font("Arial", Font.PLAIN, 16));
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        }
    }

    private class JoinGameWindow {
        public void show() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int fullWidth = (int) screenSize.getWidth();
            int fullHeight = (int) screenSize.getHeight();

            int windowWidth = (int) (fullWidth * 0.4);
            int windowHeight = (int) (fullHeight * 0.4);

//            Main Panel
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(30, 30, 50));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(2, 10, 2, 10);

//            Main Frame
            JFrame frame = new JFrame("Join Game");
            frame.setSize(windowWidth, windowHeight);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

//            Return Label
            JLabel returnLabel = new JLabel("Return");
            returnLabel.setForeground(Color.GRAY);
            returnLabel.setFont(new Font("Arial", Font.ROMAN_BASELINE, 16));
            returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            returnLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    frame.dispose();
                    showMainMenu(username);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    returnLabel.setForeground(Color.WHITE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    returnLabel.setForeground(Color.GRAY);
                }
            });

            gbc.insets = new Insets(6, 6, 6, 6);

//            Title
            JLabel titleLabel = new JLabel("Join Game", SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(JLabel.CENTER);

//            Inputs Label
            JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            inputPanel.setOpaque(false);

            Dimension labelSize = new Dimension((int)(windowWidth * 0.6), 40);
            Dimension fieldSize = new Dimension((int)(windowWidth * 0.6), 40);
            Dimension buttonSize = new Dimension((int)(windowWidth * 0.4), 40);

//            Game Code Label
            JLabel codeLabel = new JLabel("Game Code");
            codeLabel.setPreferredSize(labelSize);
            codeLabel.setForeground(Color.LIGHT_GRAY);
            codeLabel.setHorizontalAlignment(JLabel.LEFT);
            inputPanel.add(codeLabel);

//            Game Code Field
            JTextField codeField = new JTextField();
            codeField.setCaretColor(Color.WHITE);
            codeField.setPreferredSize(fieldSize);
            codeField.setBackground(new Color(50, 50, 70));
            codeField.setForeground(Color.WHITE);
            codeField.setFont(new Font("Arial", Font.PLAIN, 16));
            codeField.setHorizontalAlignment(JTextField.CENTER);
            codeField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            inputPanel.add(codeField);

            inputPanel.add(Box.createVerticalStrut(20));

//           Join Button
            JButton joinBtn = new JButton("Join");
            joinBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            joinBtn.setPreferredSize(buttonSize);
            joinBtn.setBackground(new Color(60, 60, 90));
            joinBtn.setForeground(Color.WHITE);
            joinBtn.setFont(new Font("Arial", Font.BOLD, 16));
            joinBtn.setFocusPainted(false);
            inputPanel.add(joinBtn);

//            Center Panel
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            GridBagConstraints centerGbc = new GridBagConstraints();
            centerGbc.weightx = 1.0;
            centerGbc.weighty = 1.0;
            centerPanel.add(inputPanel, centerGbc);

            joinBtn.addActionListener(e -> {
                String codeText = codeField.getText().trim();
                try {
                    int code = Integer.parseInt(codeText);
                    if (code < 0 || code > 255) {
                        JOptionPane.showMessageDialog(frame, "Invalid game code. Must be between 0 and 255.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new Thread(() -> {
                        try {
                            Client client = new Client();
                            client.start(code, username);
                            Thread.sleep(1000);
                            SwingUtilities.invokeLater(() ->
                                    new WaitingList(username, Client.connectedPlayers)
                            );
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame, "No game found at this code. Make sure the room is created first.", "Error", JOptionPane.ERROR_MESSAGE);
                                frame.dispose();
                                new JoinGameWindow().show();
                            });
                        }
                    }).start();

                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Invalid Code", JOptionPane.WARNING_MESSAGE);
                }
            });

            gbc.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(returnLabel, gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            mainPanel.add(titleLabel, gbc);
            mainPanel.add(centerPanel, gbc);

            frame.add(mainPanel);
            frame.setVisible(true);
        }
    }
}