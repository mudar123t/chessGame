package com.chessgame.Pieces;

import java.awt.Color;
import java.nio.file.Path;

import javax.swing.ImageIcon;

public class PieceImages {

    static Color WHITECOLOR = Color.WHITE;
    static Color BLACKCOLOR = Color.BLACK;
    static String PAWN = "♟";
    static String ROOK = "♜";
    static String KNIGHT = "♞";
    static String BISHOP = "♝";
    static String QUEEN = "♛";
    static String KING = "♚";

    static ImageIcon wk;
    static ImageIcon bk;
    static ImageIcon wr;
    static ImageIcon br;
    static ImageIcon wq;
    static ImageIcon bq;
    static ImageIcon wb;
    static ImageIcon bb;
    static ImageIcon wn;
    static ImageIcon bn;
    static ImageIcon wp;
    static ImageIcon bp;

    public PieceImages() {
        wk = new ImageIcon("src/main/java/com/chessgame/Resources/images/wk.png");
        bk = new ImageIcon("src/main/java/com/chessgame/Resources/images/bk.png");
        wr = new ImageIcon("src/main/java/com/chessgame/Resources/images/wr.png");
        br = new ImageIcon("src/main/java/com/chessgame/Resources/images/br.png");
        wq = new ImageIcon("src/main/java/com/chessgame/Resources/images/wq.png");
        bq = new ImageIcon("src/main/java/com/chessgame/Resources/images/bq.png");
        wb = new ImageIcon("src/main/java/com/chessgame/Resources/images/wb.png");
        bb = new ImageIcon("src/main/java/com/chessgame/Resources/images/bb.png");
        wn = new ImageIcon("src/main/java/com/chessgame/Resources/images/wn.png");
        bn = new ImageIcon("src/main/java/com/chessgame/Resources/images/bn.png");
        wp = new ImageIcon("src/main/java/com/chessgame/Resources/images/wp.png");
        bp = new ImageIcon("src/main/java/com/chessgame/Resources/images/bp.png");
    }
}
