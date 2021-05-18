package chuss;

//Abstract type for UIs, allows for two different types of interaction with the Board object
//using either a command line (console/ASCII) or a graphic interface (program window)

import chuss.Piece.Color;
import chuss.Board.BoardState;
import chuss.Piece.PieceType;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public abstract class UserInterface {

    //FIELDS

    protected Board board;
    //The board that the UI will be used to interact with

    //CONSTRUCTORS

    public UserInterface(Board board) {
        //Constructor for UserInterface implementers

        this.board = board;

    }

    //OTHER

    public abstract Move promptMove();

    public abstract PieceType promptPromotion();

    public abstract void updateBoard();

    public abstract void playGame(Interactable whiteUser, Interactable blackUser);

}

class CommandInterface extends UserInterface {

    private GraphicInterface gui;

    //CONSTRUCTORS

    public CommandInterface(Board board) {
        //Main constructor for the CommandInterface

        super(board);
        gui = new GraphicInterface(board);

    }

    //OTHER

    public void playGame(Interactable whiteUser, Interactable blackUser) {
        //Starts a game using the UI and the board.
        //[TEST CODE] Probably will clean this up later

        board.setUsers(whiteUser, blackUser);
        updateBoard();
        Scanner input = new Scanner(System.in);

        boolean gameOver = false;

        while(!gameOver) {

            boolean illegalMove;

            do {

                try {

//                    System.out.println("Press enter to move");
//                    String s = input.nextLine();

                    board.doMove(board.getCurrentUser().getMove());
                    illegalMove = false;

                } catch(IllegalArgumentException e) {

                    System.out.println(e.getMessage());
                    illegalMove = true;

                }

                if(board.getState() == BoardState.CHECKMATE) {

                    System.out.println("\n" + board.getTurn().toString() + " is now checkmated. Game over!");

                    gameOver = true;
                    illegalMove = false;

                } else if(board.getState() == BoardState.CHECK) {

                    System.out.println("\n" + board.getTurn().toString() + " has been put in check!");
                    illegalMove = false;

                } else if(board.getState() == BoardState.STALEMATE) {

                    System.out.println("\n" + board.getTurn().toString() + " is now stalemated. Game over!");
                    gameOver = true;
                    illegalMove = false;

                }

            } while(illegalMove);

            updateBoard();

        }

    }

    public Move promptMove() {
        //Takes user input for a move and returns the Move object

        Scanner input = new Scanner(System.in);

        String move;
        boolean again;

        do {

            System.out.print("Enter your move (? for help): ");
            move = input.nextLine();

            if(move.equals("?")) {
                //If the user asks for help

                System.out.printf("%nHELP: To move, you must type a move in the following format:"
                        + "%n<STARTING TILE> <ENDING TILE>"
                        + "%nThe starting tile is the tile of the piece you wish to move."
                        + "%nThe ending tile is the tile you wish to move your piece to."
                        + "%nEach tile is notated with \"<COLUMN><RANK>\", example: \"e5\""
                        + "%n%nFull example move: \"a5 g5\"%n");

                again = true;

            } else if(move.equalsIgnoreCase("CASTLE")) {

                System.out.print("Enter the movement of the King: ");

                move = input.nextLine();

                return new Move(board, move, true);

            } else again = false;

        } while(again);
        //Reprompt if the user asks for help

        return new Move(board, move);
        //Returns a Move object made from the SMN string

    }

    public PieceType promptPromotion() {

        Scanner input = new Scanner(System.in);
        System.out.print("Pawn has promotion available.\nWhich piece to promote to? ");

        while(true) {

            String type = input.nextLine();

            PieceType promotion = PieceType.NONE;

            if (type.equalsIgnoreCase("PAWN")) promotion = PieceType.PAWN;
            else if (type.equalsIgnoreCase("ROOK")) promotion = PieceType.ROOK;
            else if (type.equalsIgnoreCase("KNIGHT")) promotion = PieceType.KNIGHT;
            else if (type.equalsIgnoreCase("BISHOP")) promotion = PieceType.BISHOP;
            else if (type.equalsIgnoreCase("QUEEN")) promotion = PieceType.QUEEN;
            else if (type.equalsIgnoreCase("KING")) promotion = PieceType.KING;
            else if (type.equalsIgnoreCase("EARL")) promotion = PieceType.EARL;
            else if (type.equalsIgnoreCase("MONK")) promotion = PieceType.MONK;

            if(promotion == PieceType.NONE) {

                System.out.print("\nInvalid type, please try again: ");
                continue;

            }

            return promotion;

        }

    }

    public void updateBoard() {
        //Prints the board in basic ASCII format.

        gui.updateBoard();

        drawCaptured(Color.WHITE);

        System.out.println();
        //Adds a line before the board starts printing

        int squaresPrinted = 0;
        //Keeps track of the total number of squares that has been printed

        for(int y = board.getZeroSize(); y >= 0; y--) {
            //Loop through y-values from top to bottom

            if(y != board.getZeroSize()) System.out.println();
            //If not on the first iteration of y-loop, go down a line (avoids leading line break)
            System.out.print((y + 1) + " ");
            //Print the rank number

            for(int x = 0; x <= board.getZeroSize(); x++) {
                //Loop through x-values from left to right

                if(board.pieceAt(x, y) != null) {
                    //If there is a piece on the tile

                    if(squaresPrinted % 2 == 0) System.out.print("[" + board.pieceAt(x, y).getString() + "]");
                        //If squaresPrinted is even, print "[<pString>]"
                    else System.out.print("(" + board.pieceAt(x, y).getString() + ")");
                    //If squaresPrinted is odd, print "(<pString>)"

                } else {
                    //If there is no piece on the tile

                    if(squaresPrinted % 2 == 0) System.out.print("[ ]");
                        //If squaresPrinted is even, print "[ ]"
                    else System.out.print("( )");
                    //If squaresPrinted is odd, print "( )"

                }

                squaresPrinted++;
                //Increment squaresPrinted for each iteration of the x-loop

            }

            squaresPrinted++;
            //Increment squaresPrinted for each iteration of the y-loop

        }

        System.out.println();
        System.out.print(" ");
        //Print an extra line and the leading whitespace for the column identifiers

        for(int i = 0; i <= board.getZeroSize(); i++) {
            //Repeat <size> times

            System.out.print("  " + (char) (i + 97));
            //Print the column identifier chars by casting from int

        }

        drawCaptured(Color.BLACK);
        //Prints all black pieces that have been captured
        System.out.println();

    }

    private void drawCaptured(Color color) {
        //Prints captured pieces of either color.

        System.out.println();
        //Prints a blank line

        ArrayList<String> capturedPieces = board.getCaptured(color);

        if(capturedPieces == null) return;

        for(String p : capturedPieces) {

            System.out.print(p + " ");
            //TODO: Remove trailing whitespace

        }

    }

}

class GraphicInterface {

    private final JPanel gui;
    private final JButton[][] chessBoardSquares;
    private JPanel buttonsAndLabels;
    private final JLabel message;
    private final String columnNames;
    private final Board board;
    //remove later
    private final String path = "src/chuss/icons/";
    private ImageIcon BK;
    private ImageIcon WP;
    private ImageIcon WR;
    private ImageIcon WN;
    private ImageIcon WB;
    private ImageIcon WQ;
    private ImageIcon WK;
    private ImageIcon BP;
    private ImageIcon BR;
    private ImageIcon BN;
    private ImageIcon BB;
    private ImageIcon BQ;
    private ImageIcon blank;

    public GraphicInterface(Board board) {

//        super(board);

        this.board = board;

        try {

            BK = new ImageIcon(ImageIO.read(new File(path + "BK.gif")));
            WP = new ImageIcon(ImageIO.read(new File(path + "WP.gif")));
            WR = new ImageIcon(ImageIO.read(new File(path + "WR.gif")));
            WN = new ImageIcon(ImageIO.read(new File(path + "WN.gif")));
            WB = new ImageIcon(ImageIO.read(new File(path + "WB.gif")));
            WQ = new ImageIcon(ImageIO.read(new File(path + "WQ.gif")));
            WK = new ImageIcon(ImageIO.read(new File(path + "WK.gif")));
            BP = new ImageIcon(ImageIO.read(new File(path + "BP.gif")));
            BR = new ImageIcon(ImageIO.read(new File(path + "BR.gif")));
            BN = new ImageIcon(ImageIO.read(new File(path + "BN.gif")));
            BB = new ImageIcon(ImageIO.read(new File(path + "BB.gif")));
            BQ = new ImageIcon(ImageIO.read(new File(path + "BQ.gif")));
            blank = new ImageIcon(ImageIO.read(new File(path + "blank.png")));

        } catch(IOException e) {

            e.printStackTrace();

        }

        gui = new JPanel(new BorderLayout(3, 3));
        chessBoardSquares = new JButton[8][8];
        message = new JLabel("Chuss Champ is ready to play!");
        columnNames = "ABCDEFGH";

        initializeGui();

    }

    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("New")); // TODO - add functionality!
        tools.add(new JButton("Save")); // TODO - add functionality!
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);
        // create the chess board squares

        buttonsAndLabels = new JPanel(new GridLayout(0, 9));
        buttonsAndLabels.setBorder(new LineBorder(java.awt.Color.BLACK));
        gui.add(buttonsAndLabels);

        updateBoard();

        Runnable r = new Runnable() {

            @Override
            public void run() {

                JFrame f = new JFrame("CHUSS");
                f.add(getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public void updateBoard() {
        //Prints the board in graphical format.

        buttonsAndLabels.removeAll();

        Insets buttonMargin = new Insets(0,0,0,0);

        for (int ii = 7; ii >= 0; ii--) {

            for (int jj = 0; jj < chessBoardSquares.length; jj++) {

                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..

                Piece p = board.pieceAt(jj, ii);

                ImageIcon icon = null;

                if(p instanceof Pawn && p.getColor() == Color.WHITE) icon = WP;
                else if(p instanceof Rook && p.getColor() == Color.WHITE) icon = WR;
                else if(p instanceof Knight && p.getColor() == Color.WHITE) icon = WN;
                else if(p instanceof Bishop && p.getColor() == Color.WHITE) icon = WB;
                else if(p instanceof Queen && p.getColor() == Color.WHITE) icon = WQ;
                else if(p instanceof King && p.getColor() == Color.WHITE) icon = WK;
                else if(p instanceof Pawn && p.getColor() == Color.BLACK) icon = BP;
                else if(p instanceof Rook && p.getColor() == Color.BLACK) icon = BR;
                else if(p instanceof Knight && p.getColor() == Color.BLACK) icon = BN;
                else if(p instanceof Bishop && p.getColor() == Color.BLACK) icon = BB;
                else if(p instanceof Queen && p.getColor() == Color.BLACK) icon = BQ;
                else if(p instanceof King && p.getColor() == Color.BLACK) icon = BK;
                else if(p == null) icon = blank;

                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(java.awt.Color.WHITE);
                } else {
                    b.setBackground(java.awt.Color.GRAY);
                }
                chessBoardSquares[jj][ii] = b;
                buttonsAndLabels.add(chessBoardSquares[jj][ii]);

            }
        }

        //fill the chess board
        buttonsAndLabels.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii <= 7; ii++) {
            buttonsAndLabels.add(
                    new JLabel(columnNames.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 7; ii >= 0; ii--) {
            for (int jj = 0; jj < 8; jj++) {
                if (jj == 0) {
                    buttonsAndLabels.add(new JLabel("" + (ii + 1),
                            SwingConstants.CENTER));
                }
                buttonsAndLabels.add(chessBoardSquares[jj][ii]);
            }
        }

        gui.updateUI();

    }

    public JComponent getChessBoard() {
        return buttonsAndLabels;
    }

    public JComponent getGui() {
        return gui;
    }

}