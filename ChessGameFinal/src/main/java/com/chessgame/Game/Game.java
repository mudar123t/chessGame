package com.chessgame.Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.chessgame.Network.Client;
import com.chessgame.Board.Board;
import com.chessgame.Board.Move;
import com.chessgame.Pieces.*;
import com.chessgame.Pieces.Piece;
import com.chessgame.Main;

public class Game {

    public static Board board = new Board();

    static King wk;
    static King bk;
    static ArrayList<Piece> wPieces = new ArrayList<Piece>();
    static ArrayList<Piece> bPieces = new ArrayList<Piece>();

    private Client client;

    static boolean player = true;
    public Piece active = null;
    public static boolean drag = false;
    public static ArrayList<Piece> AllPieces = new ArrayList<Piece>();

    ArrayList<Move> allPossiblesMoves = new ArrayList<Move>();

    static List<Move> allPlayersMove = new ArrayList<Move>();
    public static List<Move> allEnemysMove = new ArrayList<Move>();
    private static boolean gameOver = false;

    private boolean isFlipped = false;

    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
        board.setFlipped(flipped);
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public Game(Client client) {
        this.client = client;
        client.setGame(this);
        new PieceImages();
        loadFenPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        start();
    }

    public Game() {
        this(null); // no client
    }

    public void start() {
        fillPieces();
        generatePlayersTurnMoves(board);
        generateEnemysMoves(board);
        checkPlayersLegalMoves();
    }

    public void draw(Graphics g, int x, int y, JPanel panel) {
        drawBoard(g);
        drawPiece(g, panel);
        drawPossibleMoves(g, panel);
        drag(x, y, g, panel);
        drawKingInCheck(player, g, panel);
    }

    public static void generatePlayersTurnMoves(Board board) {
        allPlayersMove = new ArrayList<Move>();
        for (Piece p : AllPieces) {
            if (p.isWhite() == player) {
                p.fillAllPseudoLegalMoves(board);
                allPlayersMove.addAll(p.getMoves());
            }
        }
    }

    public static void generateEnemysMoves(Board board) {
        allEnemysMove = new ArrayList<Move>();
        for (Piece p : AllPieces) {
            if (p.isWhite() != player) {
                p.fillAllPseudoLegalMoves(board);
                allEnemysMove.addAll(p.getMoves());
            }
        }
    }

    public static void changeSide() {
        player = !player;
        generateEnemysMoves(board);
        generatePlayersTurnMoves(board);
        checkPlayersLegalMoves();
        checkMate();
    }

    public void randomPlay() {
        if (gameOver) {
            return;
        }
        if (!player) {
            Random r = new Random();
            active = bPieces.get(r.nextInt(bPieces.size()));
            while (active.getMoves().isEmpty()) {
                active = bPieces.get(r.nextInt(bPieces.size()));
            }
            Move m = active.getMoves().get(r.nextInt(active.getMoves().size()));
            move(m.getToX(), m.getToY());
        }
    }

    public void selectPiece(int x, int y) {
        if (active == null && board.getPiece(x, y) != null && board.getPiece(x, y).isWhite() == player) {
            active = board.getPiece(x, y);
            active.oldX = x;
            active.oldY = y;
        }
    }

    public static void checkMate() {
        if (player) {
            for (Piece p : wPieces) {
                if (!p.getMoves().isEmpty()) {
                    return;
                }
            }
        } else {
            for (Piece p : bPieces) {
                if (!p.getMoves().isEmpty()) {
                    return;
                }
            }
        }
        if (player) {
            if (wk.isInCheck()) {
                JOptionPane.showMessageDialog(null, "check mate " + (!player ? "white" : "black") + " wins");
            } else {
                JOptionPane.showMessageDialog(null, "stalemate ");

            }
        } else {
            if (bk.isInCheck()) {
                JOptionPane.showMessageDialog(null, "check mate " + (!player ? "white" : "black") + " wins");
            } else {
                JOptionPane.showMessageDialog(null, "stalemate ");

            }
        }
        gameOver = true;
    }

    public static void checkPlayersLegalMoves() {
        List<Piece> pieces = null;
        if (player) {
            pieces = wPieces;
        } else {
            pieces = bPieces;
        }
        for (Piece p : pieces) {
            checkLegalMoves(p);
        }
    }

    public static void checkLegalMoves(Piece piece) {
        List<Move> movesToRemove = new ArrayList<Move>();
        Board clonedBoard = board.getNewBoard();
        Piece clonedActive = piece.getClone();

        for (Move move : clonedActive.getMoves()) {
            clonedBoard = board.getNewBoard();
            clonedActive = piece.getClone();

            clonedActive.makeMove(move.getToX(), move.getToY(), clonedBoard);

            List<Piece> enemyPieces = new ArrayList<Piece>();
            Piece king = null;

            if (piece.isWhite()) {
                enemyPieces = bPieces;
                king = wk;
            } else {
                enemyPieces = wPieces;
                king = bk;
            }

            for (Piece enemyP : enemyPieces) {

                Piece clonedEnemyPiece = enemyP.getClone();
                clonedEnemyPiece.fillAllPseudoLegalMoves(clonedBoard);

                for (Move bMove : clonedEnemyPiece.getMoves()) {
                    if (!(clonedActive instanceof King) && bMove.getToX() == king.getXcord()
                            && bMove.getToY() == king.getYcord()
                            && clonedBoard.getGrid()[enemyP.getXcord()][enemyP.getYcord()] == enemyP
                            .getValueInTheboard()) {
                        movesToRemove.add(move);
                    } else if (clonedActive instanceof King) {
                        if (bMove.getToX() == clonedActive.getXcord() && bMove.getToY() == clonedActive.getYcord()
                                && clonedBoard.getGrid()[enemyP.getXcord()][enemyP.getYcord()] == enemyP
                                .getValueInTheboard()) {
                            movesToRemove.add(move);
                        }
                    }
                }

            }

        }

        for (Move move : movesToRemove) {
            piece.getMoves().remove(move);
        }
    }

    public void drag(int x, int y, Graphics g, JPanel panel) {
        if (active != null && drag) {
            if (isFlipped) {
                x = panel.getWidth() - x;
                y = panel.getHeight() - y;
            }
            active.draw2(g, player, x, y, panel);
        }
    }

//    public void move(int x, int y) {
//        if (active != null) {
//            if (active.makeMove(x, y, board)) {
//                tryToPromote(active);
//
//                // Send move if it's our turn and connected to server
//                if (client != null && client.isMyTurn()) {
//                    Move move = new Move(active.getOldX(), active.getOldY(), x, y, active);
//                    client.sendMove(move);
//                }
//
//                changeSide();
//                active = null;
//            }
//            drag = false;
//        }
//    }
    public void move(int x, int y) {
        if (active != null) {
            int fromX = active.getXcord();
            int fromY = active.getYcord();

            if (active.makeMove(x, y, board)) {
                tryToPromote(active);

                // Send move to server (without sending the Piece)
                if (client != null && client.isMyTurn()) {
                    Move move = new Move(fromX, fromY, x, y);
                    client.sendMove(move);
                }

                changeSide();
                active = null;
            }
            drag = false;
        }
    }

    public void showWinByDisconnect() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Opponent disconnected. You win!",
                    "Game Over",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            Main.showEndScreen(); // or switch to end screen if you have one
        });
    }

    public void applyNetworkMove(Move move) {
        active = board.getPiece(move.getFromX(), move.getFromY());
        move(move.getToX(), move.getToY()); // triggers all game logic
    }

    public void drawKingInCheck(boolean isWhite, Graphics g, JPanel panel) {
        g.setColor(Color.RED);

        if (isWhite && wk.isInCheck()) {
            int drawX = isFlipped ? 7 - wk.getXcord() : wk.getXcord();
            int drawY = isFlipped ? 7 - wk.getYcord() : wk.getYcord();
            g.drawRect(drawX * Piece.size, drawY * Piece.size, Piece.size, Piece.size);
        } else if (bk.isInCheck()) {
            int drawX = isFlipped ? 7 - bk.getXcord() : bk.getXcord();
            int drawY = isFlipped ? 7 - bk.getYcord() : bk.getYcord();
            g.drawRect(drawX * Piece.size, drawY * Piece.size, Piece.size, Piece.size);
        }

        panel.revalidate();
        panel.repaint();
    }

    public void drawBoard(Graphics g) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int drawX = isFlipped ? 7 - i : i;
                int drawY = isFlipped ? 7 - j : j;

                if ((i + j) % 2 == 1) {
                    g.setColor(new Color(118, 150, 86));
                } else {
                    g.setColor(new Color(238, 238, 210));
                }
                g.fillRect(drawX * Piece.size, drawY * Piece.size, Piece.size, Piece.size);
            }
        }
    }

    public void tryToPromote(Piece p) {
        if (p instanceof Pawn) {
            if (((Pawn) p).madeToTheEnd()) {
                choosePiece(p, showMessageForPromotion());
            }
        }
    }

    public int showMessageForPromotion() {
        Object[] options = {"Queen", "Rook", "Knight", "Bishop"};

        drag = false;
        return JOptionPane.showOptionDialog(null, "Choose Piece To Promote to", null, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public static void choosePiece(Piece p, int choice) {
        switch (choice) {
            case 0:
                AllPieces.remove(p);
                p = new Queen(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 8 : -8);
                AllPieces.add(p);

                break;
            case 1:
                AllPieces.remove(p);
                p = new Rook(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 5 : -5);
                AllPieces.add(p);
                break;
            case 2:
                AllPieces.remove(p);
                p = new Knight(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 3 : -3);
                AllPieces.add(p);
                break;
            case 3:
                AllPieces.remove(p);
                p = new Bishop(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 3 : -3);
                AllPieces.add(p);
                break;
            default:
                AllPieces.remove(p);
                p = new Queen(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 8 : -8);
                AllPieces.add(p);
                break;
        }
        fillPieces();
    }

    public void drawPossibleMoves(Graphics g, JPanel panel) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        if (active != null) {
            active.showMoves(g2, panel);
        }

    }

//    public void drawPiece(Graphics g, JPanel panel) {
//        for (Piece p : AllPieces) {
//            int pieceX = p.getXcord(); // real position on board
//            int pieceY = p.getYcord();
//
//            int drawX = isFlipped ? 7 - pieceX : pieceX;
//            int drawY = isFlipped ? 7 - pieceY : pieceY;
//
//            p.drawAt(g, drawX, drawY, panel); // ✅ this must be used
//        }
//    }
    public void drawPiece(Graphics g, JPanel panel) {
        List<Piece> snapshot;
        synchronized (AllPieces) {
            snapshot = new ArrayList<>(AllPieces); // take a thread-safe snapshot
        }

        for (Piece p : snapshot) {
            int pieceX = p.getXcord(); // real position on board
            int pieceY = p.getYcord();

            int drawX = isFlipped ? 7 - pieceX : pieceX;
            int drawY = isFlipped ? 7 - pieceY : pieceY;

            p.drawAt(g, drawX, drawY, panel); // ✅ this must be used
        }
    }

    public void loadFenPosition(String fenString) {
        String[] parts = fenString.split(" ");
        String position = parts[0];
        int row = 0, col = 0;
        for (char c : position.toCharArray()) {
            if (c == '/') {
                row++;
                col = 0;
            }
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c)) {
                    addToBoard(col, row, c, false);
                } else {
                    addToBoard(col, row, c, true);
                }
                col++;
            }
            if (Character.isDigit(c)) {
                col += Integer.parseInt(String.valueOf(c));
            }
        }

        if (parts[1].equals("w")) {
            player = true;
        } else {
            player = false;
        }

    }

    public static void fillPieces() {
        wPieces = new ArrayList<Piece>();
        bPieces = new ArrayList<Piece>();
        for (Piece p : AllPieces) {
            if (p.isWhite()) {
                wPieces.add(p);
            } else {
                bPieces.add(p);
            }
        }
    }

    public void addToBoard(int x, int y, char c, boolean isWhite) {
        switch (String.valueOf(c).toUpperCase()) {
            case "R":
                AllPieces.add(new Rook(x, y, isWhite, board, isWhite ? 5 : -5));
                break;
            case "N":
                AllPieces.add(new Knight(x, y, isWhite, board, isWhite ? 3 : -3));
                break;
            case "B":
                AllPieces.add(new Bishop(x, y, isWhite, board, isWhite ? 3 : -3));
                break;
            case "Q":
                AllPieces.add(new Queen(x, y, isWhite, board, isWhite ? 8 : -8));
                break;
            case "K":
                King king = new King(x, y, isWhite, board, isWhite ? 10 : -10);
                AllPieces.add(king);
                if (isWhite) {
                    wk = king;
                } else {
                    bk = king;
                }
                break;
            case "P":
                AllPieces.add(new Pawn(x, y, isWhite, board, isWhite ? 1 : -1));
                break;
        }
    }

}
