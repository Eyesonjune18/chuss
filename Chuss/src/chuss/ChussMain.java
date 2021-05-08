package chuss;

//This class is tasked with handling the basic commands used to actually start the game.

import chuss.Piece.Color;
import java.util.Scanner;

public class ChussMain {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    public static void main(String[] args) {
        //Just the main method. Starts the program.

        //[TEST CODE]
        Board board = new Board();
        Interactable whiteUser = new Computer(board);
        Interactable blackUser = new Computer(board);
        board.setUser(whiteUser, Color.WHITE);
        board.setUser(blackUser, Color.BLACK);
        UserInterface ui = new CommandInterface(board);
        ui.updateBoard();
        Scanner input = new Scanner(System.in);

        boolean gameOver = true;

        while(gameOver) {

            boolean illegalMove;

            do {

                try {

//                    System.out.println("Press enter to move");
//                    String s = input.nextLine();

                    board.doMove(board.getCurrentUser().getMove());
                    illegalMove = false;

                } catch(IllegalArgumentException e) {

//                    System.out.println("Illegal move, try again");
                    illegalMove = true;

                }

            } while(illegalMove);

            ui.updateBoard();

        }
        //[END OF TEST CODE]

    }

}