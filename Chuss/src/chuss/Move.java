package chuss;

//The Move object. Stores necessary information about a move so
//that it can be passed around in a convenient package and can
//also be used as a return type.

import java.awt.*;

public class Move {

    public enum MoveType {HORIZONTAL, VERTICAL, DIAGONAL, KNIGHT, NONE}

    //FIELDS

    private final Board board;
    //The board that the move occurs on
    private final Piece movedPiece;
    //The piece being moved
    private final Piece capturedPiece;
    //The piece being captured
    private final Point startPos;
    //The starting position of the piece
    private final Point endPos;
    //The ending position of the piece
    private final int delX;
    //The change in X without absolute value
    private final int delY;
    //The change in Y without absolute value
    private final int absX;
    //The change in X with absolute value
    private final int absY;
    //The change in Y with absolute value
    private final boolean overrideLegal;
    //Whether to override the move legality check
    private final MoveType moveType;
    //Whether the move is horizontal, vertical, diagonal, or none

    //CONSTRUCTORS

    public Move(Board board, Piece movedPiece, Piece capturedPiece, int x, int y, int x1, int y1) {
        //The manual constructor for the Move object.

        this.board = board;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        startPos = new Point(x, y);
        endPos = new Point(x1, y1);
        delX = endPos.x - startPos.x;
        delY = endPos.y - startPos.y;
        absX = Math.abs(delX);
        absY = Math.abs(delY);
        overrideLegal = false;
        moveType = findMoveType();

    }

    public Move(Board board, String moveStr) {
        //The automatic constructor for the Move object,
        //takes in a move in SMN and turns it into a Move object.

        //TODO: Check if the string is actually in SMN

        Point[] move = interpretMove(moveStr);

        this.board = board;
        movedPiece = board.pieceAt(move[0]);
        capturedPiece = board.pieceAt(move[1]);
        startPos = move[0];
        endPos = move[1];
        delX = endPos.x - startPos.x;
        delY = endPos.y - startPos.y;
        absX = Math.abs(delX);
        absY = Math.abs(delY);
        overrideLegal = false;
        moveType = findMoveType();

    }

    //ACCESSORS

    public Board getMoveBoard() {

        return board;

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

    public int getDelX() {

        return delX;

    }

    public int getDelY() {

        return delY;

    }

    public int getAbsX() {

        return absX;

    }

    public int getAbsY() {

        return absY;

    }

    public boolean getOverride() {

        return overrideLegal;

    }

    public MoveType getMoveType() {

        return moveType;

    }

    //OTHER

    private Point[] interpretMove(String moveStr) {
        //Turns a SMN string into two Points representing the start and end positions of the move.

        Point[] move = new Point[2];
        //Create a move array to return

        moveStr = moveStr.replace(" ", "");
        //Remove spaces from the string
        char[] cMove = moveStr.toCharArray();
        //Turn the string into a char array

        move[0] = new Point(cMove[0] - 'a', cMove[1] - '1');
        //Turns the start tile name into a Point
        move[1] = new Point(cMove[2] - 'a', cMove[3] - '1');
        //Turns the end tile name into a Point

        return move;
        //Return the Point array

    }

    private MoveType findMoveType() {
        //Figures out what direction the move goes in

        if(getAbsX() != 0 && getAbsY() == 0) return MoveType.HORIZONTAL;
        else if(absX == 0 && getAbsY() != 0) return MoveType.VERTICAL;
        else if(absX == 0 && getAbsY() == 0) return MoveType.NONE;
        else if(absX == getAbsY()) return MoveType.DIAGONAL;
        else if((absX == 1 && absY == 2) || (absX == 2 && absY == 1)) return MoveType.KNIGHT;
        else return MoveType.NONE;

    }

    public boolean checkPath() {

        int modX = 1;
        int modY = 1;

        if(getDelX() < 0) modX = -1;
        if(getDelY() < 0) modY = -1;

        //TODO: Possibly add something to make sure only horizontal, vertical, and diagonal moves get cleared

        if(getAbsX() == getAbsY()) {
            //If the move is diagonal

            int y = getStartY() + modY;
            //Start at the square in front of the piece, to avoid detecting the piece itself

            for(int x = getStartX() + modX; x != getEndX(); x += modX) {
                //Loop through board diagonally from StartPos to EndPos

                if(board.pieceAt(x, y) != null) return false;
                //If there is a piece, return false

                y += modY;

            }

        } else if(getDelX() != 0 && getDelY() == 0) {
            //If the move is horizontal

            for(int x = getStartX() + modX; x != getEndX(); x += modX) {
                //Loop through board from StartX to EndX

                if(board.pieceAt(x, getStartY()) != null) return false;
                //If there is a piece, return false

            }

        } else if(getDelX() == 0 && getDelY() != 0) {
            //If the move is vertical

            for(int y = getStartY() + modY; y != getEndY(); y += modY) {
                //Loop through board from StartY to EndY

                if(board.pieceAt(getStartX(), y) != null) return false;
                //If there is a piece, return false

            }

        }

        return true;
        //Otherwise, return true

    }

}