package chuss;

//TODO: Add vocab key in README
//The Board object. Stores all necessary info about the current game.

import chuss.Piece.Color;
import java.awt.*;

public class Board {

    //FIELDS

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class
    private final int size = 8;
    //The amount of tiles (width and height) on the board
    private final int tSize = size - 1;
    //Used when referencing the "true size," usually when iterating starting from 0
    private final Piece[][] board;
    //The Piece array that represents the actual chess board
    private final UserInterface ui = new CommandInterface(this);

    //CONSTRUCTORS

    public Board() {
        //The default constructor for the Board object, creates a board
        //from the standard starting chess layout FEN string.

         this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

    }

    public Board(String fen) {
        //The proper constructor for the Board object, creates a board
        //by interpreting a FEN string into a board layout.

        board = readFen(fen);
        //Set the board field containing the piece array
        //to the array returned by the FEN interpreter

    }

    //ACCESSORS

    public Piece pieceAt(int x, int y) {

        return pieceAt(new Point(x, y));

    }

    public Piece pieceAt(Point boardPos) {

        if(board[boardPos.x][boardPos.y] != null) return board[boardPos.x][boardPos.y];
        else return null;

    }

    public int getSize() {

        return tSize;

    }

    public UserInterface getUI() {

        return ui;

    }

    //MUTATORS

    public void doMove(Move move) {

        if(DEBUG) System.out.println("Moving piece: " + move.getMovedPiece().getString());
        //[DEBUG TEXT] Prints the string of the piece being moved

        if(!move.getMovedPiece().isLegal(move)) throw new IllegalArgumentException("ERROR: Illegal move");
        //If the move is illegal, throw an IllegalArgument

        board[move.getEndX()][move.getEndY()] = move.getMovedPiece();
        //Sets the tile at the end position to the moved piece
        board[move.getStartX()][move.getStartY()] = null;
        //Sets the tile at the start position to null (empty tile)

    }

    //OTHER

    private Piece[][] readFen(String fen) {
        //Takes in a FEN string and turns it into a Piece array (board).
        //TODO: Add full FEN interpretation, not just board layout

        Piece[][] fenBoard = new Piece[size][size];
        //Initialize a board to add pieces to and return
        char[] fenArr = fen.toCharArray();
        //Create an array of characters from the FEN string

        int x = 0;
        int y = tSize;
        //Start at the top-left corner (0, 7)

        if(DEBUG) System.out.println("Fen: " + fen);
        //[DEBUG TEXT] Print the FEN string

        for(char c : fenArr) {
            //For each char in the FEN string:
            //TODO: Possibly convert to HashMap

            if(c >= '1' && c <= '8') x += (c - 49);
                //If c is between num 1 through 8, add num - 1 to the x value
            else if(c == '/') {
                x = -1;
                //Go back to the start of the rank
                y--;
                //Go down a rank
            }
            else if(c == 'P') fenBoard[x][y] = new Pawn(Color.WHITE, x, y);
            else if(c == 'R') fenBoard[x][y] = new Rook(Color.WHITE, x, y);
            else if(c == 'N') fenBoard[x][y] = new Knight(Color.WHITE, x, y);
            else if(c == 'B') fenBoard[x][y] = new Bishop(Color.WHITE, x, y);
            else if(c == 'Q') fenBoard[x][y] = new Queen(Color.WHITE, x, y);
            else if(c == 'K') fenBoard[x][y] = new King(Color.WHITE, x, y);
            else if(c == 'E') fenBoard[x][y] = new Earl(Color.WHITE, x, y);
            else if(c == 'M') fenBoard[x][y] = new Monk(Color.WHITE, x, y);
            else if(c == 'p') fenBoard[x][y] = new Pawn(Color.BLACK, x, y);
            else if(c == 'r') fenBoard[x][y] = new Rook(Color.BLACK, x, y);
            else if(c == 'n') fenBoard[x][y] = new Knight(Color.BLACK, x, y);
            else if(c == 'b') fenBoard[x][y] = new Bishop(Color.BLACK, x, y);
            else if(c == 'q') fenBoard[x][y] = new Queen(Color.BLACK, x, y);
            else if(c == 'k') fenBoard[x][y] = new King(Color.BLACK, x, y);
            else if(c == 'e') fenBoard[x][y] = new Earl(Color.BLACK, x, y);
            else if(c == 'm') fenBoard[x][y] = new Monk(Color.BLACK, x, y);
            else throw new IllegalArgumentException("ERROR: Invalid FEN string");
            //If c is not a recognized character, throw an IllegalArgument

            x++;
            //Every time the current character has been checked, move over a tile

        }

        return fenBoard;
        //Return the new board with the pieces on it

    }

//    public boolean checkCollision(Move move) {
//
//
//
//    }

}