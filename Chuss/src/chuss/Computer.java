package chuss;

//This class handles the computer interaction with the game,
//specifically the process of sorting and selecting moves.

import java.util.List;
import java.util.Random;

public class Computer implements Interactable {

    //FIELDS

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class
    private final Board board;
    //The board that the player is interacting with

    //CONSTRUCTORS

    public Computer(Board board) {
        //The constructor for the Computer object. Uses a move sorting
        //algorithm to generate a move that is passed back to
        //the main game.

        this.board = board;

    }

    //OTHER

    public Move getMove() {

        List<Move> legalMoves = board.getLegalMoves(board.getTurn());

        Random rand = new Random();

        return legalMoves.get(rand.nextInt(legalMoves.size() - 1));

    }

}