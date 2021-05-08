package chuss;

//The Piece objects. All child Piece objects extend the main Piece object,
//which stores common data and behavior that is required by all pieces.
//Individual pieces store any necessary data and behavior for their particular type.

import chuss.Move.MoveType;
import java.awt.*;

public abstract class Piece {
    /*
    GENERAL PIECE RULES:
    1. May not move to their current position
    2. May not capture a friendly piece
    3. May not be moved on an enemy turn
    4. May not put themselves in check
    5. Must make a move that takes themselves out of check if they are in check
    */

    //ENUMS

    public enum Color {WHITE, BLACK}
    //The enum for the piece color, either WHITE or BLACK

    //FIELDS

    protected int moveCount;
    //The amount of times the piece has moved
    protected String pString;
    //The string representing the type of piece
    protected Point pos;
    //The position of the piece on the board
    protected final Color color;
    //The color of the piece

    //CONSTRUCTORS

    public Piece(Color color, Point pos) {

        moveCount = 0;
        this.pos = pos;
        this.color = color;

    }

    //ACCESSORS

    public String getString() {

        return pString;

    }

    public Color getColor() {

        return color;

    }

    public Point getPos() {

        return pos;

    }

    //MUTATORS

    public void incMoveCount(int i) {

        moveCount += i;

    }

    //OTHER

    public boolean isLegal(Move move) {
        //Checks all universal move conditions to determine if a move is legal for any piece.

        boolean c1;
        //True if the piece is not moving into itself
        boolean c2 = true;
        //True if the piece is not trying to capture a friendly piece
        boolean c3 = true;
        //True if the piece is on the correct side for this turn
        boolean c4;
        //True if move resolves check state

        c1 = move.getStartPos() != move.getEndPos();
        //[ISSUE] Not detecting moves that have the same start and end position
        if(move.getCapturedPiece() != null) c2 = move.getCapturedPiece().getColor() != move.getMovedPiece().getColor();
        if(!move.getOverride()) c3 = color == move.getMoveBoard().getTurn();
        c4 = true;

        return c1 && c2 && c3 && c4;

    }

} class Pawn extends Piece {
    /*
    PAWN RULES:
    1. May move one tile vertically, except when a piece is in the way
        a. for black, -y
        b. for white, +y
    2. May move two spaces in their y-direction if their moveCount = 0
    3. May move diagonally in their y-direction if they are capturing
        a. capture may include an enemy piece or an en-passant tile
    4. May be promoted to any type of piece except a King if they make it to the end of the board
    */

    //CONSTRUCTORS

    public Pawn(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "P";
        if(color == Color.BLACK) pString = "p";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1 = false;
        //True if the pawn is moving forward one tile and no collisions occur
        boolean c2 = false;
        //True if the pawn is moving forward two tiles on its first move and no collisions occur
        boolean c3 = false;
        //True if the pawn is moving diagonally one tile and is capturing a piece
        boolean c4 = false;
        //True if the pawn is at the end of the board
        //TODO: Figure out logic/implementation for piece promotions

        int colorMod = 1;
        //Determines how the rules apply depending on the color of the pawn
        if(color == Color.BLACK) colorMod = -1;
        //If the pawn is black, sets colorMod to -1 to "invert" the rules

        int absX = move.getAbsX();
        int delY = colorMod * move.getDelY();

        if(absX == 0 && delY == 1 && move.getCapturedPiece() == null) c1 = true;
        if(absX == 0 && delY == 2 && move.getCapturedPiece() == null && moveCount == 0 &&
                move.getMoveBoard().pieceAt(move.getStartX(), move.getStartY() + colorMod) == null) c2 = true;
        //TODO: Absolutely disgusting conditional above, must fix (possibly keep the "next square" as a field for pawns?)
        if(absX == 1 && delY == 1 && move.getCapturedPiece() != null) {

            if(move.getCapturedPiece().getColor() != color) c3 = true;

        }
        //TODO: En passant captures

        return c1 ^ c2 ^ c3 ^ c4;

    }

} class Rook extends Piece {
    /*
    ROOK RULES:
    1. May move any distance vertically or horizontally, as long as no collisions occur
    3. May move to a castling tile if they and their King have moveCount = 0
    */

    //CONSTRUCTORS

    public Rook(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "R";
        if(color == Color.BLACK) pString = "r";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1;
        //If the move is horizontal or vertical and does not collide with any other pieces
        boolean c2 = false;
        //If the move is a valid castle

        c1 = move.getMoveType() == MoveType.HORIZONTAL ||
            move.getMoveType() == MoveType.VERTICAL;
        //Checks if the move is horizontal or vertical
        if(c1) c1 = move.checkPath();
        //Checks if the move is free of collisions

        return c1 ^ c2;

    }

} class Knight extends Piece {
    /*
    KNIGHT RULES:
    1. May move with ΔX = 2 ^ 1, ΔY = 1 ^ 2, disregarding all other pieces
    */

    //CONSTRUCTORS

    public Knight(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "N";
        if(color == Color.BLACK) pString = "n";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1;
        //If the move is a Knight move

        c1 = move.getMoveType() == MoveType.KNIGHT;
        //Checks if the move is a Knight move

        return c1;

    }

} class Bishop extends Piece {
    /*
    BISHOP RULES:
    1. May move any distance diagonally in any direction, as long as no collisions occur
    */

    //CONSTRUCTORS

    public Bishop(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "B";
        if(color == Color.BLACK) pString = "b";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1;
        //If the move is diagonal and does not collide with any other pieces

        c1 = move.getMoveType() == MoveType.DIAGONAL;
        //Checks if the move is diagonal
        if(c1) c1 = move.checkPath();
        //Checks if the move is free of collisions

        return c1;

    }

} class Queen extends Piece {
    /*
    QUEEN RULES:
    1. May move any distance horizontally, vertically, or diagonally in any direction, as long as no collisions occur
    */

    //CONSTRUCTORS

    public Queen(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "Q";
        if(color == Color.BLACK) pString = "q";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1;
        //If the move is horizontal or vertical and does not collide with any other pieces

        c1 = move.getMoveType() == MoveType.HORIZONTAL ||
                move.getMoveType() == MoveType.VERTICAL ||
                move.getMoveType() == MoveType.DIAGONAL;
        //Checks if the move is horizontal or vertical
        if(c1) c1 = move.checkPath();
        //Checks if the move is free of collisions

        return c1;

    }

} class King extends Piece {
    /*
    KING RULES:
    1. May move one tile horizontally, vertically, or diagonally in any direction, as long as no collisions occur
    */

    //CONSTRUCTORS

    public King(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "K";
        if(color == Color.BLACK) pString = "k";

    }

    //OTHER

    @Override
    public boolean isLegal(Move move) {

        if(!super.isLegal(move)) return false;
        //If the move is universally illegal, return false

        boolean c1;
        //If the move is vertical, horizontal or diagonal and only one tile away

        c1 = (move.getMoveType() == MoveType.HORIZONTAL ||
                move.getMoveType() == MoveType.VERTICAL ||
                move.getMoveType() == MoveType.DIAGONAL) &&
                (move.getAbsX() <= 1 && move.getAbsY() <= 1);
        //Checks if the move is valid for a King

        return c1;

    }

} class Earl extends Piece {
    /*
    EARL RULES:
    1. May move two tiles horizontally, vertically, or diagonally in any direction, as long as no collisions occur
    2. May only capture diagonally
    */

    //CONSTRUCTORS

    public Earl(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "E";
        if(color == Color.BLACK) pString = "e";

    }

} class Monk extends Piece {
    /*
    MONK RULES:
    1. May move one tile horizontally, vertically, or diagonally in any direction, as long as no collisions occur
    2. When moved adjacent to an enemy piece, there is a 10% chance for the enemy piece to become friendly (except for Kings)
    3. May not capture pieces
    */

    //CONSTRUCTORS

    public Monk(Color color, int x, int y) {

        super(color, new Point(x, y));
        if(color == Color.WHITE) pString = "M";
        if(color == Color.BLACK) pString = "m";

    }

}