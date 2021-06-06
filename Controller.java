package chuss;

import chuss.listeners.ForfeitListener;
import chuss.listeners.RestartListener;
import chuss.listeners.MoveListener;
import chuss.listeners.NewGameListener;
import chuss.listeners.RedoListener;
import chuss.listeners.UndoListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.View;
import java.awt.*;
import java.awt.Button;
import java.util.ArrayList;

public class Controller {
    public Board chessBoard; 					///< The Model in MVC structure
    public CommandManager commandManager;			///< Saves Commands so we can undo and redo them
    public boolean inMovement;						///< true if a player is mid-move
    public ArrayList<Button> buttonsToHighlight; 	///< Buttons to highlight for the user to display valid moves for a ChessPiece
    public ArrayList<java.awt.Button> buttonsCanMoveTo;		///< Buttons that a current ChessPiece can move to
    public Point originPoint;						///< The origin of a moving ChessPiece

    public GraphicInterface view;								///< The "View" in MVC structure.
    public int whiteGamesWon = 0;					///< Number of games WHITE has won
    public int blackGamesWon = 0;					///< Number of games BLACK has won
    public boolean isClassicMode = true;			///< true for Classic Mode. false for Custom Mode.

    /**
     * Constructor: Initialize the Controller with a new ChessBoard and new View. Add mouseListeners to the View
     */
    public Controller() {
        initializeVariables();

        view = new View(chessBoard, this) {
            @Override
            public float getPreferredSpan(int axis) {
                return 0;
            }

            @Override
            public void paint(Graphics g, Shape allocation) {

            }

            @Override
            public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
                return null;
            }

            @Override
            public int viewToModel(float x, float y, Shape a, Position.Bias[] biasReturn) {
                return 0;
            }
        };
        addMouseListeners();
    }

    /**
     * Reset the Controller with the new ChessBoard and UPDATED View. Add mouseListeners to the View
     */
    public void reset(){
        initializeVariables();
        view.initialize(chessBoard, this);
        addMouseListeners();
    }

    /**
     * Create ChessBoard and CommandManager. Initialize local variables in Controller.
     */
    public void initializeVariables(){
        chessBoard = new Board(8, 8);
        inMovement = false;
        buttonsToHighlight = new ArrayList<java.awt.Button>();
        buttonsCanMoveTo = new ArrayList<java.awt.Button>();
        originPoint = null;
    }

    /**
     * Add mouseListeners to the View
     */
    public void addMouseListeners(){
        /* Add MoveListener to all tiles */
        for (int i = 0; i < chessBoard.rows; i++){
            for (int j = 0; j < chessBoard.columns; j++){
                view.addMouseListener(new MoveListener(this), view.button[i][j]);
            }
        }
        view.addMouseListener(new UndoListener(this), view.undoButton);
        view.addMouseListener(new RedoListener(this), view.redoButton);
        view.addMouseListener(new ForfeitListener(this), view.whiteForfeit);
        view.addMouseListener(new ForfeitListener(this), view.blackForfeit);
        view.addMouseListener(new RestartListener(this), view.whiteRestart);
        view.addMouseListener(new RestartListener(this), view.blackRestart);
        view.addMouseListener(new NewGameListener(this, true), view.classicMode);
        view.addMouseListener(new NewGameListener(this, false), view.customMode);
    }
}

