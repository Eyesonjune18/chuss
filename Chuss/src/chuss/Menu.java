package chuss;

import chuss.Piece.Color;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    public static void mainMenu() throws IOException {

        Board board = new Board();
        UserInterface ui = new CommandInterface(board);
        Interactable whiteUser = null;
        Interactable blackUser = null;
        Scanner input = new Scanner(System.in);

        boolean again = false;

        System.out.println("Welcome to Chuss!");

        do {

            if(again) System.out.println("ERROR: Invalid input, try again");
            System.out.print("Would you like to play singleplayer or multiplayer (S/M)? ");
            String mode = input.nextLine();
            System.out.print("Would you like to play as white or black (W/B)? ");
            String color = input.nextLine();


            if (color.equalsIgnoreCase("W")) {

                whiteUser = new Player(ui);

                if (mode.equalsIgnoreCase("S")) blackUser = new Computer(board, Color.BLACK);
                else if (mode.equalsIgnoreCase("M")) {

                    Client.setupClient();
                    blackUser = new OtherPlayer(board);

                }

            } else if (color.equalsIgnoreCase("B")) {

                blackUser = new Player(ui);

                if (mode.equalsIgnoreCase("S")) whiteUser = new Computer(board, Color.WHITE);
                else if (mode.equalsIgnoreCase("M")) {

                    Client.setupClient();
                    whiteUser = new OtherPlayer(board);

                }

            }

            try {

                again = false;
                ui.playGame(whiteUser, blackUser);

            } catch(NullPointerException e) {

                again = true;

            }

        } while(again);

    }

}
