package chuss.listeners;

import chuss.Board;
import chuss.Controller;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ForfeitListener extends MouseAdapter {
    public Controller controller;	///< The Controller in the MVC structure
    public Board chessBoard;	///< The Model in the MVC structure

    /**
     * Constructore. Saves the Controller and Model information
     * @param controller	The Controller in the MVC structure
     */
    public ForfeitListener(Controller controller){
        this.controller 	= controller;
        this.chessBoard 	= controller.chessBoard;
    }

    /**
     * Let's a player instantly forfeit a game
     * @param event		the event corresponding to the mouse click
     */
    public void mouseClicked(MouseEvent event){
        if (chessBoard.playerTurn == Color.BLACK){
            javax.swing.JOptionPane.showMessageDialog(null, "Black Forfeits. White Wins!");
            controller.whiteGamesWon++;
        }
        else{
            javax.swing.JOptionPane.showMessageDialog(null, "White Forfeits. Black Wins!");
            controller.blackGamesWon++;
        }
        controller.reset();
    }
}
