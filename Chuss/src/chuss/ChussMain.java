package chuss;

//This class is tasked with handling the basic commands used to actually start the game.

public class ChussMain {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    public static void main(String[] args) {
        //Just the main method. Starts the program.

        Board board = new Board();
        //Creates a Board object to play a game with
        UserInterface ui = new CommandInterface(board);
        //Creates a UserInterface to interact with the board
        Interactable whiteUser = new Player(ui);
        //Creates a user for the white side
        Interactable blackUser = new Player(ui);
        //Creates a user for the black side
        ui.playGame(whiteUser, blackUser);
        //Starts the game by passing in the desired users for each side

    }

}