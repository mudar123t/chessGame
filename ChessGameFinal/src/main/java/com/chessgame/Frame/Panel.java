package com.chessgame.Frame;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.chessgame.Network.Client;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chessgame.Game.*;
import com.chessgame.Pieces.Piece;
import java.awt.Dimension;

public class Panel extends JPanel {

    private static final long serialVersionUID = 1L;
    Game game;
    Client client;
    int ti, tj;
    public static int xx, yy;
    JPanel panel = this;

    public Panel(Client client) {
        this.client = client;
        this.game = new Game(client);
        if (client != null && !client.isWhite()) {
            game.setFlipped(true);
        }
        this.setFocusable(true);
        this.addMouseListener(new Listener());
        this.addMouseMotionListener(new Listener());

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 37) {
                    Game.board.undoMove();
                }

            }
        }
        );
        this.setPreferredSize(new Dimension(640, 640));
        this.repaint();
        this.revalidate();
    }

    public Panel() {
        this(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g, xx, yy, this);
    }

    class Listener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (client != null && !client.isMyTurn()) {
                    return;
                }

                int x = e.getX() / Piece.size;
                int y = e.getY() / Piece.size;

                if (game.isFlipped()) {
                    x = 7 - x;
                    y = 7 - y;
                }

                Game.drag = false;
                game.active = null;
                game.selectPiece(x, y);
                revalidate();
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int x = e.getX() / Piece.size;
            int y = e.getY() / Piece.size;

            if (game.isFlipped()) {
                x = 7 - x;
                y = 7 - y;
            }

            ti = x;
            tj = y;

            if (Game.board.getPiece(ti, tj) != null) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            revalidate();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (client != null && !client.isMyTurn()) {
                return;
            }

            int x = e.getX() / Piece.size;
            int y = e.getY() / Piece.size;

            if (game.isFlipped()) {
                x = 7 - x;
                y = 7 - y;
            }

            if (!Game.drag && game.active != null) {
                game.active = null;
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                game.selectPiece(x, y);
                Game.drag = true;
                xx = e.getX();
                yy = e.getY();
            }

            revalidate();
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (client != null && !client.isMyTurn()) {
                return;
            }

            int x = e.getX() / Piece.size;
            int y = e.getY() / Piece.size;

            if (game.isFlipped()) {
                x = 7 - x;
                y = 7 - y;
            }

            game.move(x, y);
            revalidate();
            repaint();
        }

    }

}
