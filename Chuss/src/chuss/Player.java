package chuss;

//This class handles the player interaction with the game,
//specifically the selection of a move from user input.

import java.util.Scanner;

public class Player implements Interactable {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    private Move chosenMove;
    private final Board board;

    public Player(Board board) {
        //The constructor for the Player object. Gets a one-time user
        //input and turns it into a move object to be passed back to
        //the main game.

        this.board = board;

        chosenMove = getUserInput();

    }

    private Move getUserInput() {

        Scanner input = new Scanner(System.in);

        String move = null;
        boolean again = false;

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

        return new Move(board, move);
        //Returns a Move object made from the SMN string

    }

    //ACCESSORS

    public Move getMove() {

        return chosenMove;

    }

}