package chuss;

//Abstract type for UIs, allows for two different types of interaction with the Board object
//using either a command line (console/ASCII) or a graphic interface (program window)

import chuss.Piece.Color;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class UserInterface {

    //FIELDS

    protected Board board;
    //The board that the UI will be used to interact with

    //CONSTRUCTORS

    public UserInterface(Board board) {
        //Constructor for UserInterface implementers

        this.board = board;

    }

    //OTHER

    public abstract Move promptMove();

    public abstract void updateBoard();

    public abstract void playGame(Interactable whiteUser, Interactable blackUser);

} class CommandInterface extends UserInterface {

    //CONSTRUCTORS

    public CommandInterface(Board board) {
        //Main constructor for the CommandInterface

        super(board);

    }

    //OTHER

    public void playGame(Interactable whiteUser, Interactable blackUser) {
        //Starts a game using the UI and the board.
        //[TEST CODE] Probably will clean this up later

        board.setUsers(whiteUser, blackUser);
        updateBoard();
//        Scanner input = new Scanner(System.in);

        boolean gameOver = false;

        while(!gameOver) {

            boolean illegalMove;

            do {

                try {

//                    System.out.println("Press enter to move");
//                    String s = input.nextLine();

                    board.doMove(board.getCurrentUser().getMove());
                    illegalMove = false;

                } catch(IllegalArgumentException e) {

                    System.out.println("Illegal move, try again");
                    illegalMove = true;

                }

                if(board.getState() == Board.BoardState.CHECKMATE) {

                    gameOver = true;
                    illegalMove = false;

                }

            } while(illegalMove);

            updateBoard();

        }

    }

    public Move promptMove() {
        //Takes user input for a move and returns the Move object

        Scanner input = new Scanner(System.in);

        String move;
        boolean again;

        do {

            System.out.print("Enter your move (? for help): ");
            move = input.nextLine();

            if(move.equals("?")) {
                //If the user asks for help

                System.out.printf("%nHELP: To move, you must type a move in the following format:"
                        + "%n<STARTING TILE> <ENDING TILE>"
                        + "%nThe starting tile is the tile of the piece you wish to move."
                        + "%nThe ending tile is the tile you wish to move your piece to."
                        + "%nEach tile is notated with \"<COLUMN><RANK>\", example: \"e5\""
                        + "%n%nFull example move: \"a5 g5\"%n");

                again = true;

            } else again = false;

        } while(again);
        //Reprompt if the user asks for help

        return new Move(board, move);
        //Returns a Move object made from the SMN string

    }

    public void updateBoard() {
        //Prints the board in basic ASCII format.

        drawCaptured(Color.WHITE);

        System.out.println();
        //Adds a line before the board starts printing

        int squaresPrinted = 0;
        //Keeps track of the total number of squares that has been printed

        for(int y = board.getSize(); y >= 0; y--) {
            //Loop through y-values from top to bottom

            if(y != board.getSize()) System.out.println();
            //If not on the first iteration of y-loop, go down a line (avoids leading line break)
            System.out.print((y + 1) + " ");
            //Print the rank number

            for(int x = 0; x <= board.getSize(); x++) {
                //Loop through x-values from left to right

                if(board.pieceAt(x, y) != null) {
                    //If there is a piece on the tile

                    if(squaresPrinted % 2 == 0) System.out.print("[" + board.pieceAt(x, y).getString() + "]");
                        //If squaresPrinted is even, print "[<pString>]"
                    else System.out.print("(" + board.pieceAt(x, y).getString() + ")");
                    //If squaresPrinted is odd, print "(<pString>)"

                } else {
                    //If there is no piece on the tile

                    if(squaresPrinted % 2 == 0) System.out.print("[ ]");
                        //If squaresPrinted is even, print "[ ]"
                    else System.out.print("( )");
                    //If squaresPrinted is odd, print "( )"

                }

                squaresPrinted++;
                //Increment squaresPrinted for each iteration of the x-loop

            }

            squaresPrinted++;
            //Increment squaresPrinted for each iteration of the y-loop

        }

        System.out.println();
        System.out.print(" ");
        //Print an extra line and the leading whitespace for the column identifiers

        for(int i = 0; i <= board.getSize(); i++) {
            //Repeat <size> times

            System.out.print("  " + (char) (i + 97));
            //Print the column identifier chars by casting from int

        }

        drawCaptured(Color.BLACK);
        //Prints all black pieces that have been captured
        System.out.println();

    }

    private void drawCaptured(Color color) {
        //Prints captured pieces of either color.

        System.out.println();
        //Prints a blank line

        ArrayList<String> capturedPieces = board.getCaptured(color);

        if(capturedPieces == null) return;

        for(String p : capturedPieces) {

            System.out.print(p + " ");
            //TODO: Remove trailing whitespace

        }

    }

} class GraphicInterface {



}