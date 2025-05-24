package com.chessgame.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.chessgame.Board.Board;
import com.chessgame.Board.Move;
import com.chessgame.Game.Game;
import java.io.Serializable;

public abstract class Piece implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;
    protected int xCord;
    protected int yCord;
    public int oldX;
    public int oldY;
    protected boolean isWhite;
    protected boolean isAlive;
    protected int valueInTheBoard;
    protected Board board;
    protected String pieceImage;
    protected Color pieceColor;
    public static int size = 80;
    protected List<Move> moves = new ArrayList<>();
    protected ImageIcon image;

    public boolean makeMove(int toX, int toY, Board board) {
        Move move = new Move(xCord, yCord, toX, toY);
        if (!alive()) {
            return false;
        }
        for (Move m : moves) {
            if (m.compareTo(move) == 0) {
                board.updatePieces(xCord, yCord, toX, toY, this);
                xCord = toX;
                yCord = toY;
                return true;
            }
        }
        return false;

    }

    public abstract boolean canMove(int x, int y, Board board);

    @SuppressWarnings("unlikely-arg-type")
    public boolean alive() {
        if (board.getXY(xCord, yCord) != valueInTheBoard || board.getXY(xCord, yCord) == 0 || board.getPiece(xCord, yCord) == null) {
            isAlive = false;
            Game.AllPieces.remove(getClass());
        }
        return isAlive;
    }

    public void intializeSide(int value) {
        if (isWhite) {
            pieceColor = PieceImages.WHITECOLOR;
        } else {
            pieceColor = PieceImages.BLACKCOLOR;
        }
        valueInTheBoard = value;
    }

    ;
	
	public Piece(int x, int y, boolean iswhite, Board board, int value) {
        this.xCord = x;
        this.yCord = y;
        this.isWhite = iswhite;
        isAlive = true;
        this.board = board;
        intializeSide(value);
        board.setPieceIntoBoard(x, y, this);
    }

    public void showMoves(Graphics g, JPanel panel) {
        Graphics2D g2 = (Graphics2D) g;

        boolean flipped = board != null && board.isFlipped();

        for (Move m : moves) {
            int fromX = m.getFromX();
            int fromY = m.getFromY();
            int toX = m.getToX();
            int toY = m.getToY();

            if (flipped) {
                fromX = 7 - fromX;
                fromY = 7 - fromY;
                toX = 7 - toX;
                toY = 7 - toY;
            }

            if (board.getPiece(m.getToX(), m.getToY()) != null
                    && board.getPiece(m.getToX(), m.getToY()).isWhite() != isWhite()) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.DARK_GRAY);
            }

            g.fillOval((toX * size) + size / 3, (toY * size) + size / 3, size / 3, size / 3);

            g2.setColor(Color.DARK_GRAY);
            if (Game.drag) {
                g2.fillRect(fromX * size, fromY * size, size, size);
            } else {
                g2.drawRect(fromX * size, fromY * size, size, size);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    public void draw(Graphics g, boolean drag, JPanel panel) {
        g.drawImage(image.getImage(), xCord * Piece.size, yCord * Piece.size, Piece.size, Piece.size, panel);
        panel.revalidate();
        panel.repaint();
    }

    public void drawAt(Graphics g, int boardX, int boardY, JPanel panel) {
        int pixelX = boardX * size;
        int pixelY = boardY * size;
        g.drawImage(image.getImage(), pixelX, pixelY, size, size, panel);
    }

    public void draw2(Graphics g, boolean player, int x, int y, JPanel panel) {
    boolean flipped = board != null && board.isFlipped();

    if (flipped) {
        x = panel.getWidth() - x;
        y = panel.getHeight() - y;
    }

    g.drawImage(image.getImage(), x - size / 2, y - size / 2, size, size, panel);
    panel.revalidate();
    panel.repaint();
}


    public void fillAllPseudoLegalMoves(Board b) {
        moves = new ArrayList<Move>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMove(i, j, b)) {
                    moves.add(new Move(xCord, yCord, i, j));
                }
            }
        }
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getXcord() {
        return xCord;
    }

    public void setXcord(int xcord) {
        this.xCord = xcord;
    }

    public int getYcord() {
        return yCord;
    }

    public void setYcord(int ycord) {
        this.yCord = ycord;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setValueInTheboard(int value) {
        this.valueInTheBoard = value;
    }

    public int getValueInTheboard() {
        return valueInTheBoard;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public Piece getClone() {
        try {
            return (Piece) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
