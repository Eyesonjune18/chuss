package chuss;

//This class handles the computer interaction with the game,
//specifically the process of sorting and selecting moves.

public class Computer implements Interactable {

    //FIELDS

    private Move chosenMove;

    //CONSTRUCTORS

    public Computer() {
        //The constructor for the Player object. Gets a one-time user
        //input and turns it into a move object to be passed back to
        //the main game.

        chooseMove();

    }

    private Move chooseMove() {
        //Starts the move evaluation process.

        return null;

    }

    //ACCESSORS

    public Move getMove() {

        return chosenMove;

    }

}