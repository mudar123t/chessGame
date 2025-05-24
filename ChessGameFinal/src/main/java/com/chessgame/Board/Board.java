
package com.chessgame.Board;

import java.util.*;
import com.chessgame.Game.Game;
import com.chessgame.Pieces.*;

public class Board implements Cloneable {

    public static final int ROWS = 8;
    public static final int COLUMNS = 8;

    public int[][] grid;
    public Piece[][] pieces;
    public Piece lastPieceMoved;
    public Move lastMove;
    public Piece died;
    private boolean flipped = false;

    public Stack<Move> lastMoves = new Stack<>();
    public Stack<Piece> deadPieces = new Stack<>();
    public List<Piece> piecesList = new ArrayList<Piece>();

    public Board() {
        grid = new int[ROWS][COLUMNS];
        pieces = new Piece[ROWS][COLUMNS];
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void setPieceIntoBoard(int x, int y, Piece piece) {
        if (piece != null) {
            grid[x][y] = piece.getValueInTheboard();
            pieces[x][y] = piece;
            piecesList.add(piece);
        } else {
            grid[x][y] = 0;
            pieces[x][y] = null;
        }
    }

    public void updatePieces(int fromX, int fromY, int toX, int toY, Piece piece) {
        lastMove = new Move(fromX, fromY, toX, toY);
        lastMoves.add(lastMove);

        if (pieces[toX][toY] != null) {
            died = pieces[toX][toY];
            deadPieces.add(died);
            piecesList.remove(died);
            Game.AllPieces.remove(died);
            Game.fillPieces();
        } else {
            deadPieces.add(null);
        }

        piece.setXcord(toX);
        piece.setYcord(toY);

        grid[fromX][fromY] = 0;
        grid[toX][toY] = piece.getValueInTheboard();
        pieces[fromX][fromY] = null;
        pieces[toX][toY] = piece;
    }

//    public void undoMove() {
//        if (!lastMoves.isEmpty()) {
//            Move move = lastMoves.pop();
//            Piece dead = deadPieces.pop();
////			grid[move.fromX][move.fromY] = move.getPiece().getValueInTheboard();
////			pieces[move.fromX][move.fromY] = move.getPiece();
//            Piece moved = this.getPiece(move.getToX(), move.getToY());
//            grid[move.fromX][move.fromY] = moved.getValueInTheboard();
//            pieces[move.fromX][move.fromY] = moved;
//
//            move.getPiece().setXcord(move.fromX);
//            move.getPiece().setYcord(move.fromY);
//
//            if (move.getPiece() instanceof Pawn) {
//                if (move.getPiece().getYcord() == (move.getPiece().isWhite() ? 6 : 1)) {
//                    ((Pawn) move.getPiece()).setFirstMove(true);
//                }
//            }
//
//            if (move.getPiece() instanceof Rook) {
//                if (((Rook) move.getPiece()).isJustMoved()) {
//                    ((Rook) move.getPiece()).setHasMoved(false);
//                    ((Rook) move.getPiece()).setJustMoved(false);
//                }
//            }
//
//            if (dead != null) {
//                Game.AllPieces.add(dead);
//                Game.fillPieces();
//                grid[move.toX][move.toY] = dead.getValueInTheboard();
//                pieces[move.toX][move.toY] = dead;
//                dead.setXcord(move.getToX());
//                dead.setYcord(move.getToY());
//            } else {
//                grid[move.toX][move.toY] = 0;
//                pieces[move.toX][move.toY] = dead;
//            }
//            Game.changeSide();
//        }
//        return;
//    }
    public void undoMove() {
        if (!lastMoves.isEmpty()) {
            Move move = lastMoves.pop();
            Piece dead = deadPieces.pop();

            // Get moved piece from destination
            Piece moved = this.getPiece(move.getToX(), move.getToY());

            // Move it back
            grid[move.fromX][move.fromY] = moved.getValueInTheboard();
            pieces[move.fromX][move.fromY] = moved;

            // Clear destination
            grid[move.getToX()][move.getToY()] = 0;
            pieces[move.getToX()][move.getToY()] = null;

            // Reset position
            moved.setXcord(move.getFromX());
            moved.setYcord(move.getFromY());

            // Special handling
            if (moved instanceof Pawn) {
                if (moved.getYcord() == (moved.isWhite() ? 6 : 1)) {
                    ((Pawn) moved).setFirstMove(true);
                }
            }

            if (moved instanceof Rook) {
                if (((Rook) moved).isJustMoved()) {
                    ((Rook) moved).setHasMoved(false);
                    ((Rook) moved).setJustMoved(false);
                }
            }

            // Restore captured piece if there was one
            if (dead != null) {
                Game.AllPieces.add(dead);
                Game.fillPieces();
                grid[move.getToX()][move.getToY()] = dead.getValueInTheboard();
                pieces[move.getToX()][move.getToY()] = dead;
                dead.setXcord(move.getToX());
                dead.setYcord(move.getToY());
            }

            Game.changeSide();
        }
    }

    public Piece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public int getXY(int x, int y) {
        return grid[x][y];
    }

    public void setXY(int x, int y, int v) {
        grid[x][y] = v;
    }

    public Board getNewBoard() {
        Board b = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.getPiece(i, j) != null) {
                    b.setPieceIntoBoard(i, j, this.getPiece(i, j).getClone());
                }
            }
        }
        return b;
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(grid[j][i] + "  ");
            }
            System.out.println();
        }
    }

}