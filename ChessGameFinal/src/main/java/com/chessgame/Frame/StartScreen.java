//package com.chessgame.Frame;
//
//import com.chessgame.Game.Game;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import com.chessgame.Network.Client;
//
//public class StartScreen extends JFrame {
//
//    private JTextField ipField;
//    private JButton startButton;
//    private JLabel titleLabel;
//    private JLabel ipLabel;
//
//    public StartScreen() {
//        setTitle("Chess Game - Start Screen");
//        setSize(400, 250);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setResizable(false);
//
//        // Layout
//        setLayout(new BorderLayout());
//
//        titleLabel = new JLabel("Welcome to Chess", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        add(titleLabel, BorderLayout.NORTH);
//
//        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));
//
//        ipLabel = new JLabel("Server IP:");
//        ipField = new JTextField("127.0.0.1");
//
//        centerPanel.add(ipLabel);
//        centerPanel.add(ipField);
//
//        add(centerPanel, BorderLayout.CENTER);
//
//        startButton = new JButton("Start Game");
//        add(startButton, BorderLayout.SOUTH);
//
//        startButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String ip = ipField.getText();
//                try {
//                    Client client = new Client(ip, 5000);
//                    Game game = new Game(client);
//                    client.setGame(game);
//                    new Frame(client); // لعرض اللوحة
//                    dispose(); // Close start screen
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(StartScreen.this,
//                            "Failed to connect to server at " + ip,
//                            "Connection Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        setVisible(true);
//    }
//}
