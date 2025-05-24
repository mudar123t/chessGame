
package com.chessgame;

import com.chessgame.Frame.Frame;
import com.chessgame.Game.Game;
import com.chessgame.Network.Client;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showStartScreen);
    }

    public static void showStartScreen() {
        JFrame startFrame = new JFrame("Chess - Start");
        JButton startButton = new JButton("Start Game");

        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.addActionListener(e -> {
            startFrame.dispose(); // Close the start screen

            SwingUtilities.invokeLater(() -> {
                try {
                    Client client = new Client("ec2-13-61-26-108.eu-north-1.compute.amazonaws.com", 5000, null);


                    Frame gameFrame = new Frame(client);

                    // Optional: when user manually closes game window
                    gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            showEndScreen();
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Failed to connect to server.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.getContentPane().setLayout(new GridBagLayout());
        startFrame.getContentPane().add(startButton);
        startFrame.setSize(300, 200);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

    public static void showEndScreen() {
        JFrame endFrame = new JFrame("Game Over");
        JLabel label = new JLabel("Game Over", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Start");

        label.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        backButton.addActionListener(e -> {
            endFrame.dispose();
            showStartScreen(); // Go back to start
        });

        endFrame.setLayout(new BorderLayout());
        endFrame.add(label, BorderLayout.CENTER);
        endFrame.add(backButton, BorderLayout.SOUTH);
        endFrame.setSize(300, 150);
        endFrame.setLocationRelativeTo(null);
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setVisible(true);
    }
}
