package gui;

import src.network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GameWindow {

    private static GameWindow instance;

    private int myIndex;
    public String myRole;
    private String[] roleList;

    private boolean hasVoted = false;
    private boolean isAlive = true;
    private boolean hasChecked = false;

    public Map<Integer, JButton> voteButtons = new HashMap<>();

    private JTextArea texts;
    private JTextField messageField;
    private JButton sendButton;

    private JFrame frame;
    private JPanel buttonPanel;
    private JPanel chatPanel;

    public void startFromRoleData(int playerIndex, String myRole, String[] roleList) {
        this.myIndex = playerIndex;
        this.myRole = myRole;
        this.roleList = roleList;
        GameWindow.instance = this;

        buildGameGUI(roleList.length);
    }

    private void buildGameGUI(int totalPlayers) {
        frame = new JFrame("Mafia Game - Player " + myIndex);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 50));

        JLabel roleLabel = new JLabel("Your Role: " + myRole, SwingConstants.CENTER);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(roleLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel(null);
        buttonPanel.setPreferredSize(new Dimension(600, 900));
        buttonPanel.setBackground(new Color(30, 30, 50));
        buttonPanel.setLayout(new CircleLayout(250));

        for (int i = 0; i < totalPlayers; i++) {
            String label = "Player " + i;
            JButton button = new JButton(label);
            button.setEnabled(false);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(60, 60, 90));
            button.setPreferredSize(new Dimension(150, 40));
            voteButtons.put(i, button);
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.WEST);

        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 900));
        chatPanel.setBackground(new Color(30, 30, 50));

        texts = new JTextArea();
        texts.setEditable(false);
        texts.setFont(new Font("Arial", Font.PLAIN, 16));
        texts.setForeground(Color.WHITE);
        texts.setBackground(new Color(50, 50, 70));

        JScrollPane scrollPane = new JScrollPane(texts);
        scrollPane.setPreferredSize(new Dimension(390, 800));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputText = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setForeground(Color.WHITE);
        messageField.setBackground(new Color(50, 50, 70));
        messageField.setCaretColor(Color.WHITE);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(60, 60, 90));
        sendButton.setPreferredSize(new Dimension(100, 40));

        inputText.add(messageField, BorderLayout.CENTER);
        inputText.add(sendButton, BorderLayout.EAST);
        chatPanel.add(inputText, BorderLayout.SOUTH);
        frame.add(chatPanel, BorderLayout.EAST);

        sendButton.addActionListener(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                messageField.setText("");
                texts.append("Me: " + msg + "\n");
                if (Client.out != null) {
                    Client.out.println("CHAT:" + msg);
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean isMafia() {
        return myRole.equalsIgnoreCase("Don") || myRole.equalsIgnoreCase("Black");
    }

    public static void setChatEnabled(boolean enabled) {
        if (instance == null || !instance.isAlive) return;
        instance.messageField.setEnabled(enabled);
        instance.sendButton.setEnabled(enabled);
        instance.chatPanel.setVisible(enabled);
    }

    public static void enableVoting() {
        if (instance == null || !instance.isAlive) return;
        instance.hasVoted = false;

        for (Map.Entry<Integer, JButton> entry : instance.voteButtons.entrySet()) {
            JButton button = entry.getValue();
            button.setEnabled(true);
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
            button.addActionListener(e -> {
                if (!instance.hasVoted) {
                    instance.hasVoted = true;
                    Client.out.println("VOTE:" + entry.getKey());
                    button.setEnabled(false);
                    instance.disableAllVoteButtons();
                }
            });
        }
    }

    private void disableAllVoteButtons() {
        for (JButton btn : voteButtons.values()) {
            btn.setEnabled(false);
        }
    }

    public static void eliminatePlayer(int index) {
        if (instance == null) return;

        JButton btn = instance.voteButtons.get(index);
        if (btn != null) {
            btn.setText("❌ " + btn.getText());
            btn.setEnabled(false);
        }

        appendMessage("☠️ Player " + index + " has been eliminated.");

        if (index == instance.myIndex) {
            instance.isAlive = false;
            appendMessage("❌ You have been eliminated. You can no longer participate.");
            instance.messageField.setEnabled(false);
            instance.sendButton.setEnabled(false);
            instance.chatPanel.setVisible(false);
            instance.disableAllVoteButtons();
        }
    }

    public static void setMafiaVotingEnabled(boolean enabled) {
        if (instance == null || !instance.isAlive) return;

        for (Map.Entry<Integer, JButton> entry : instance.voteButtons.entrySet()) {
            JButton button = entry.getValue();
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
            button.setEnabled(false);

            if (enabled && instance.isMafia()) {
                button.setEnabled(true);
                button.addActionListener(e -> {
                    Client.out.println("MAFIA_VOTE:" + entry.getKey());
                    instance.disableAllVoteButtons();
                });
            }
        }
    }

    public static void setSherifCheckEnabled(boolean enabled) {
        if (instance == null || !instance.isAlive) return;
        if (!instance.myRole.equalsIgnoreCase("Sherif")) return;
        instance.hasChecked = false;

        for (Map.Entry<Integer, JButton> entry : instance.voteButtons.entrySet()) {
            JButton button = entry.getValue();
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }

            button.setEnabled(false);
            if (enabled && !instance.hasChecked) {
                button.setEnabled(true);
                button.addActionListener(e -> {
                    Client.out.println("SHERIF_CHECK:" + entry.getKey());
                    instance.hasChecked = true;
                    instance.disableAllVoteButtons();
                });
            }
        }
    }

    public static void showSherifResult(int index, String role) {
        if (instance == null || !instance.isAlive) return;
        if (!instance.myRole.equalsIgnoreCase("Sherif")) return;

        JOptionPane.showMessageDialog(null,
                "Player " + index + " is a " + role + ".",
                "Sherif's Investigation",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void appendMessage(String msg) {
        if (instance != null && instance.texts != null) {
            instance.texts.append(msg + "\n");
        }
    }

    public static void clearChat() {
        if (instance != null && instance.texts != null) {
            instance.texts.setText("");
        }
    }

    static class CircleLayout implements LayoutManager {
        private int radius;

        public CircleLayout(int radius) {
            this.radius = radius;
        }

        @Override
        public void layoutContainer(Container parent) {
            int count = parent.getComponentCount();
            int width = parent.getWidth();
            int height = parent.getHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            double angleStep = 2 * Math.PI / count;

            for (int i = 0; i < count; i++) {
                Component comp = parent.getComponent(i);
                Dimension pref = comp.getPreferredSize();

                int x = (int) (centerX + radius * Math.cos(i * angleStep) - pref.width / 2);
                int y = (int) (centerY + radius * Math.sin(i * angleStep) - pref.height / 2);

                // Clamp to keep inside bounds (optional)
                x = Math.max(0, Math.min(x, width - pref.width));
                y = Math.max(0, Math.min(y, height - pref.height));

                comp.setBounds(x, y, pref.width, pref.height);
            }
        }
        @Override public Dimension preferredLayoutSize(Container parent) { return parent.getPreferredSize(); }
        @Override public Dimension minimumLayoutSize(Container parent) { return parent.getMinimumSize(); }
        @Override public void addLayoutComponent(String name, Component comp) {}
        @Override public void removeLayoutComponent(Component comp) {}
    }
}
