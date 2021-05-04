package chuss;

//This class handles the computer interaction with the game,
//specifically the process of sorting and selecting moves.

import java.util.Scanner;

public class Computer implements Interactable {

    private Move chosenMove;

    public Computer() {
        //The constructor for the Player object. Gets a one-time user
        //input and turns it into a move object to be passed back to
        //the main game.

        chooseMove();

    }

    private Move chooseMove() {

        return null;

    }

    public Move getMove() {

        return chosenMove;

    }

}