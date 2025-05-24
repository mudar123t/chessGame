package com.chessgame.Network;

import com.chessgame.Board.Move;
import com.chessgame.Game.Game;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Game game;
    private int playerId;
    private boolean isMyTurn = false;

    public Client(String serverIp, int port, Game game) throws IOException {
        this.game = game;
        this.socket = new Socket(serverIp, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        try {
            this.playerId = in.readInt(); // 0 = white, 1 = black
            this.isMyTurn = (playerId == 0); // white starts
            System.out.println("Connected as player: " + (isWhite() ? "White" : "Black"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start background listener thread
        new Thread(this::listenForOpponentMove).start();
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isWhite() {
        return playerId == 0;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void sendMove(Move move) {
        try {
            out.writeObject(move);
            out.flush();
            isMyTurn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void listenForOpponentMove() {
//        try {
//            while (true) {
//                Move opponentMove = (Move) in.readObject();
//                System.out.println("Received opponent move: " + opponentMove.getFromX() + "," + opponentMove.getFromY() + " -> " + opponentMove.getToX() + "," + opponentMove.getToY());
//
//                // Set active piece and simulate move
//                game.active = Game.board.getPiece(opponentMove.getFromX(), opponentMove.getFromY());
//                game.move(opponentMove.getToX(), opponentMove.getToY());
//
//                isMyTurn = true;
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Disconnected from server.");
//        }
//    }
    private void listenForOpponentMove() {
        try {
            while (true) {
                Object input = in.readObject();

                if (input instanceof String && "WIN_BY_DISCONNECT".equals(input)) {
                    System.out.println("Opponent disconnected. You win!");
                    game.showWinByDisconnect(); // 🔔 You need to implement this method in Game class
                    break;
                }

                if (input instanceof Move) {
                    Move opponentMove = (Move) input;
                    System.out.println("Received opponent move: " + opponentMove.getFromX() + "," + opponentMove.getFromY() + " -> " + opponentMove.getToX() + "," + opponentMove.getToY());

                    game.active = Game.board.getPiece(opponentMove.getFromX(), opponentMove.getFromY());
                    game.move(opponentMove.getToX(), opponentMove.getToY());

                    isMyTurn = true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected from server.");
        }
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    

}
