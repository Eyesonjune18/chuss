package chuss;

//This class handles the computer interaction with the game,
//specifically the process of sorting and selecting moves.

import java.util.ArrayList;
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

        List<Move> legalMoves = new ArrayList<Move>();

        for(int x = 0; x <= board.getSize(); x++) {
            //Loop through ranks

            for(int y = 0; y <= board.getSize(); y++) {
                //Loop through columns

                Piece p = board.pieceAt(x, y);

                if(p != null) {
                    if(DEBUG) System.out.println("Checking piece: " + p.getString());
                    //[DEBUG TEXT] Prints the string of the moved piece

                    if(p.getColor() == board.getTurn()) {

                        for(int x1 = 0; x1 <= board.getSize(); x1++) {
                            //Loop through ranks

                            for(int y1 = 0; y1 <= board.getSize(); y1++) {
                                //Loop through columns

                                if(DEBUG) System.out.println("  to [" + x1 + ", " + y1 + "]");
                                //[DEBUG TEXT] Prints the ending position of the move to be checked

                                Move m = new Move(board, x, y, x1, y1);

                                if(p.isLegal(m)) {

                                    if(DEBUG) System.out.println("    Move is legal!");
                                    //[DEBUG TEXT] Prints if the move is legal
                                    legalMoves.add(m);

                                }

                            }

                        }

                    }

                }

            }

        }

        Random rand = new Random();

        return legalMoves.get(rand.nextInt(legalMoves.size() - 1));

    }

}