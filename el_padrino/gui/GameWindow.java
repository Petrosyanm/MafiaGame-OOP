package gui;

import javax.swing.*;
import java.awt.*;

public class GameWindow{
    public void startTheGame(){
        JFrame frame = new JFrame("Game Window");
        frame.setSize(900, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel board = new JPanel();
        board.setBackground(Color.LIGHT_GRAY);
        board.setPreferredSize(new Dimension(500, 900));

        JPanel chat = new JPanel();
        chat.setLayout(new BorderLayout());
        chat.setPreferredSize(new Dimension(400, 900));
        JTextArea texts = new JTextArea();
        texts.setEditable(false);
        texts.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(texts);
        chat.add(chatScrollPane, BorderLayout.CENTER);
        JPanel inputText = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        inputText.add(messageField, BorderLayout.CENTER);
        inputText.add(sendButton, BorderLayout.EAST);

        chat.add(inputText, BorderLayout.SOUTH);


        frame.add(board, BorderLayout.WEST);
        frame.add(chat, BorderLayout.EAST);

        frame.setVisible(true);
    }
}