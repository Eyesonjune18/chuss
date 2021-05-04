package chuss;

//This class is tasked with handling the basic commands used to actually start the game.

public class ChussMain {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    public static void main(String[] args) {
        //Just the main method. Starts the program.

        //[TEST CODE]
        Board board = new Board();
        board.printBoard();
        new Player(board);
        //[END OF TEST CODE]

    }

}