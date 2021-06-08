package chuss;

//This class is tasked with handling the basic commands used to actually start the game.

import java.io.IOException;

public class ChussMain {

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class

    public static void main(String[] args) throws IOException {
        //Just the main method. Starts the program.

        Menu.mainMenu();

    }

}