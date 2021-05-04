package chuss;

//This class handles the player interaction with the game,
//specifically the selection of a move from user input.

import java.util.Scanner;

public class Player implements Interactable {

    private Move chosenMove;

    public Player() {
        //The constructor for the Player object. Gets a one-time user
        //input and turns it into a move object to be passed back to
        //the main game.

        getUserInput();

    }

    private Move getUserInput() {

        Scanner input = new Scanner(System.in);

        return null;

    }

    public Move getMove() {

        return chosenMove;

    }

}