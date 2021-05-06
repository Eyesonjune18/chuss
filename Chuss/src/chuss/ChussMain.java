package chuss;

//This class is tasked with handling the basic commands used to actually start the game.

public class ChussMain {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    public static void main(String[] args) {
        //Just the main method. Starts the program.

        //[TEST CODE]
        Board board = new Board();
        UserInterface ui = new CommandInterface(board);
        ui.updateBoard();
        Player p1 = new Player(board);

        boolean gameOver = true;

        while(gameOver) {

            boolean illegalMove;

            do {

                try {

                    board.doMove(p1.getMove());
                    illegalMove = false;

                } catch (IllegalArgumentException e) {

                    System.out.println("Illegal move, try again");
                    illegalMove = true;

                }

            } while(illegalMove);

            ui.updateBoard();

        }
        //[END OF TEST CODE]

    }

}