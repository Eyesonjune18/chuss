package chuss;


import chuss.Board;
import chuss.GraphicInterface;
import chuss.Piece;

import java.awt.Button;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Command {
    Board chessBoard;		///< the current ChessBoard the game is being played on
    GraphicInterface view;					///< the View of our ChessBoard
    Point origin;				///< the origin of the moving ChessPiece
    Point destination;			///< the destination of the moving ChessPiece

    ImageIcon imageOrigin;		///< the image associated with the moving ChessPiece's original position
    ImageIcon imageDestination;	///< the image associated with the moving ChessPiece's destination position
    Piece capturedPiece;	///< the ChessPiece that may be captured. We save it in case a user wants to "undo" a move

    /**
     * A representation of a "Move" command
     * @param chessBoard	The ChessBoard we are moving the ChessPiece on
     * @param view			The View representation of current ChessBoard
     * @param origin		The origin of the moving ChessPiece
     * @param destination	The destination of the moving ChessPiece
     */
    public Command(Board chessBoard, GraphicInterface view, Point origin, Point destination){
        this.chessBoard  = chessBoard;
        this.view 		 = view;
        this.origin 	 = origin;
        this.destination = destination;

        Button originalButton    = view.getButton(origin);
        Button destinationButton = view.getButton(destination);
        imageOrigin      = (ImageIcon) originalButton.getIcon();
        imageDestination = (ImageIcon) destinationButton.getIcon();

        Piece possiblePiece = chessBoard.getPiece(destination);
        if (possiblePiece != null)
            capturedPiece = new Piece(possiblePiece);
    }

    /**
     * Simply execute the current Command
     */
    public void execute() {
        /* Update Model */
        Piece originPiece = chessBoard.getPiece(origin);
        chessBoard.movePiece(originPiece, destination);
        chessBoard.updateBoard();
        /* Update View */
        Button originButton = view.getButton(origin);
        java.awt.Button destinationButton = view.getButton(destination);
        view.setIcon(null, originButton);
        view.setIcon(imageOrigin, destinationButton);

        view.updateButtons();
    }

    /**
     * Undo the current Command
     */
    public void undo(){
        /* Update Model */
        Piece destinationPiece = chessBoard.getPiece(destination);
        chessBoard.movePiece(destinationPiece, origin);
        if (capturedPiece != null)
            chessBoard.tile[destination.y][destination.x] = capturedPiece;
        chessBoard.updateChessBoard();
        /* Update View */
        java.awt.Button originButton = view.getButton(origin);
        java.awt.Button destinationButton = view.getButton(destination);
        view.setIcon(imageOrigin, originButton);
        view.setIcon(imageDestination, destinationButton);

        view.updateButtons();
    }
}
