package chuss;

import java.util.Scanner;

public abstract class UserInterface {

    //FIELDS

    protected Board board;

    //CONSTRUCTORS

    public UserInterface(Board board) {

        this.board = board;

    }

    //OTHER

    public abstract Move getMove();

    public abstract void updateBoard();

} class CommandInterface extends UserInterface {

    //CONSTRUCTORS

    public CommandInterface(Board board) {

        super(board);

    }

    //OTHER

    public Move getMove() {

        Scanner input = new Scanner(System.in);
        //Scanner for the console input

        String move = null;
        //Initializing the move string

        boolean again;
        //Boolean to repeat do-while loop

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

        //[TEMPORARILY REMOVED] System.out.println();
        //[TEMPORARILY REMOVED] drawCaptured(whiteCaptured);
        //[TEMPORARILY REMOVED] System.out.println();

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

        //[TEMPORARILY REMOVED] drawCaptured(blackCaptured);
        System.out.println();
        System.out.println();

    }

}