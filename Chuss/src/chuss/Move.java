package chuss;

//The Move object. Stores necessary information about a move so
//that it can be passed around in a convenient package and can
//also be used as a return type.

import java.awt.*;

public class Move {

    private final Piece movedPiece;
    private final Point startPos;
    private final Point endPos;

    public Move(Piece piece, int x, int y, int x1, int y1) {

        movedPiece = piece;
        startPos = new Point(x, y);
        endPos = new Point(x1, y1);

    }

    public Move(Board board, String moveStr) {

        Point[] move = interpretMove(moveStr);

        movedPiece = board.pieceAt(move[0]);
        startPos = move[0];
        endPos = move[1];

    }

    private Point[] interpretMove(String moveStr) {

        Point[] move = new Point[2];

        moveStr = moveStr.replace(" ", "");
        char[] cMove = moveStr.toCharArray();

        return move;

    }

    public Piece getMovedPiece() {

        return movedPiece;

    }

    public Point getStartPos() {

        return startPos;

    }

    public int getStartX() {

        return startPos.x;

    }

    public int getStartY() {

        return startPos.y;

    }

    public Point getEndPos() {

        return endPos;

    }

    public int getEndX() {

        return endPos.x;

    }

    public int getEndY() {

        return endPos.y;

    }

}