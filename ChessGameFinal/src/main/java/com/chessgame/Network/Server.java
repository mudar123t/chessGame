
package com.chessgame.Network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {

    private static final int PORT = 5000;
    private Socket[] clients = new Socket[2];
    private ObjectOutputStream[] outs = new ObjectOutputStream[2];
    private ObjectInputStream[] ins = new ObjectInputStream[2];

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started, waiting for players...");

        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; i++) {
            clients[i] = serverSocket.accept();
            outs[i] = new ObjectOutputStream(clients[i].getOutputStream());
            ins[i] = new ObjectInputStream(clients[i].getInputStream());
            outs[i].writeInt(i); // send player number (0 = white, 1 = black)
            outs[i].flush();
            System.out.println("Player " + i + " connected");
        }

        pool.execute(() -> handleMoves(0, 1)); // White to Black
        pool.execute(() -> handleMoves(1, 0)); // Black to White
    }

    private void handleMoves(int from, int to) {
        try {
            while (true) {
                Object input = ins[from].readObject();
                outs[to].writeObject(input);
                outs[to].flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + from + " disconnected.");
            notifyWinByDisconnect(to);
        } finally {
            closeConnections(from);
        }
    }

    private void notifyWinByDisconnect(int playerIndex) {
        try {
            if (outs[playerIndex] != null) {
                outs[playerIndex].writeObject("WIN_BY_DISCONNECT");
                outs[playerIndex].flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to notify player " + playerIndex + ": " + e.getMessage());
        }
    }

    private void closeConnections(int index) {
        try {
            if (clients[index] != null) clients[index].close();
            if (outs[index] != null) outs[index].close();
            if (ins[index] != null) ins[index].close();
        } catch (IOException e) {
            System.err.println("Error closing resources for player " + index + ": " + e.getMessage());
        }
    }
}
