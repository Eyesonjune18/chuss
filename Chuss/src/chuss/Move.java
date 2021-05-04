package chuss;

//The Move object. Stores necessary information about a move so
//that it can be passed around in a convenient package and can
//also be used as a return type.

import java.awt.*;

public class Move {

    private final Piece movedPiece;
    //The piece being moved
    private final Point startPos;
    //The starting position of the piece
    private final Point endPos;
    //The ending position of the piece

    public Move(Piece piece, int x, int y, int x1, int y1) {
        //The manual constructor for the Move object.

        movedPiece = piece;
        startPos = new Point(x, y);
        endPos = new Point(x1, y1);

    }

    public Move(Board board, String moveStr) {
        //The automatic constructor for the Move object,
        //takes in a move in SMN and turns it into a Move object.

        Point[] move = interpretMove(moveStr);

        System.out.println(move[0]);

        System.out.println(board.pieceAt(move[0]).getString());

        movedPiece = board.pieceAt(move[0]);
        startPos = move[0];
        endPos = move[1];

    }

    private Point[] interpretMove(String moveStr) {

        Point[] move = new Point[2];

        moveStr = moveStr.replace(" ", "");
        char[] cMove = moveStr.toCharArray();

        move[0] = new Point(cMove[0] - 'a', cMove[1] - '1');
        move[1] = new Point(cMove[2] - 'a', cMove[3] - '1');

        return move;

    }

    //ACCESSORS

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