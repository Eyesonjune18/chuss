package chuss;

//The Move object. Stores necessary information about a move so
//that it can be passed around in a convenient package and can
//also be used as a return type.

import java.awt.*;

public class Move {

    private final Board moveBoard;
    //The board that the move occurs on
    private final Piece movedPiece;
    //The piece being moved
    private final Piece capturedPiece;
    //The piece being captured
    private final Point startPos;
    //The starting position of the piece
    private final Point endPos;
    //The ending position of the piece
    private final boolean overrideLegal;
    //Whether to override the move legality check

    public Move(Board board, Piece movedPiece, Piece capturedPiece, int x, int y, int x1, int y1) {
        //The manual constructor for the Move object.

        moveBoard = board;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        startPos = new Point(x, y);
        endPos = new Point(x1, y1);
        overrideLegal = false;

    }

    public Move(Board board, String moveStr) {
        //The automatic constructor for the Move object,
        //takes in a move in SMN and turns it into a Move object.

        //TODO: Check if the string is actually in SMN

        Point[] move = interpretMove(moveStr);

        moveBoard = board;
        movedPiece = board.pieceAt(move[0]);
        capturedPiece = board.pieceAt(move[1]);
        startPos = move[0];
        endPos = move[1];
        overrideLegal = false;

    }

    //ACCESSORS

    public Board getMoveBoard() {

        return moveBoard;

    }

    public Piece getMovedPiece() {

        return movedPiece;

    }

    public Piece getCapturedPiece() {

        return capturedPiece;

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

    public boolean getOverride() {

        return overrideLegal;

    }

    //OTHER

    private Point[] interpretMove(String moveStr) {
        //Turns a SMN string into two Points representing the start and end positions of the move.

        Point[] move = new Point[2];

        moveStr = moveStr.replace(" ", "");
        char[] cMove = moveStr.toCharArray();

        move[0] = new Point(cMove[0] - 'a', cMove[1] - '1');
        move[1] = new Point(cMove[2] - 'a', cMove[3] - '1');

        return move;

    }

}