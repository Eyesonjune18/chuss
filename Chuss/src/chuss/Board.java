package chuss;

//TODO: Add vocab key in README
//The Board object. Stores all necessary info about the current game.

import chuss.Piece.Color;
import java.awt.*;

public class Board {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    private final int size = 8;
    private final int tSize = size - 1;
    private Piece[][] board;
    //The Piece array that represents the actual chess board

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

    protected Piece[][] readFen(String fen) {

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
            else if(c == 'P') fenBoard[x][y] = new Pawn(Color.WHITE, new Point(x, y));
            else if(c == 'R') fenBoard[x][y] = new Rook(Color.WHITE, new Point(x, y));
            else if(c == 'N') fenBoard[x][y] = new Knight(Color.WHITE, new Point(x, y));
            else if(c == 'B') fenBoard[x][y] = new Bishop(Color.WHITE, new Point(x, y));
            else if(c == 'Q') fenBoard[x][y] = new Queen(Color.WHITE, new Point(x, y));
            else if(c == 'K') fenBoard[x][y] = new King(Color.WHITE, new Point(x, y));
            else if(c == 'E') fenBoard[x][y] = new Earl(Color.WHITE, new Point(x, y));
            else if(c == 'M') fenBoard[x][y] = new Monk(Color.WHITE, new Point(x, y));
            else if(c == 'p') fenBoard[x][y] = new Pawn(Color.BLACK, new Point(x, y));
            else if(c == 'r') fenBoard[x][y] = new Rook(Color.BLACK, new Point(x, y));
            else if(c == 'n') fenBoard[x][y] = new Knight(Color.BLACK, new Point(x, y));
            else if(c == 'b') fenBoard[x][y] = new Bishop(Color.BLACK, new Point(x, y));
            else if(c == 'q') fenBoard[x][y] = new Queen(Color.BLACK, new Point(x, y));
            else if(c == 'k') fenBoard[x][y] = new King(Color.BLACK, new Point(x, y));
            else if(c == 'e') fenBoard[x][y] = new Earl(Color.BLACK, new Point(x, y));
            else if(c == 'm') fenBoard[x][y] = new Monk(Color.BLACK, new Point(x, y));
            else throw new IllegalArgumentException("ERROR: invalid FEN string");
            //If c is not a recognized character, inform the user that the FEN string is invalid

            x++;
            //Every time the current character has been checked, move over a tile

        }

        return fenBoard;
        //Return the new board with the pieces on it

    }

    protected void printBoard() {
        //Prints the board in basic ASCII format.

        //[TEMPORARILY REMOVED] System.out.println();
        //[TEMPORARILY REMOVED] drawCaptured(whiteCaptured);
        //[TEMPORARILY REMOVED] System.out.println();

        int squaresPrinted = 0;
        //Keeps track of the total number of squares that has been printed

        for(int y = tSize; y >= 0; y--) {
            //Loop through y-values from the top to the bottom

            if(y != tSize) System.out.println();
            //If not on the first iteration of y-loop, go down a line (avoids leading line break)
            System.out.print((y + 1) + " ");
            //Print the rank number

            for(int x = 0; x <= tSize; x++) {

                if(board[x][y] != null) {

                    if(squaresPrinted % 2 == 0) System.out.print("[" + board[x][y].pString + "]");
                    else System.out.print("(" + board[x][y].pString + ")");

                } else {

                    if(squaresPrinted % 2 == 0) System.out.print("[ ]");
                    else System.out.print("( )");

                }

                squaresPrinted++;

            }

            squaresPrinted++;

        }

        System.out.println();
        System.out.print(" ");

        for(int i = 0; i < size; i++) {

            char colChar = (char) (i + 97);
            System.out.print("  " + colChar);

        }

        //[TEMPORARILY REMOVED] drawCaptured(blackCaptured);
        System.out.println();

    }

}