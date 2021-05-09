package chuss;

//Abstract type for UIs, allows for two different types of interaction with the Board object
//using either a command line (console/ASCII) or a graphic interface (program window)

import chuss.Piece.Color;
import chuss.Board.BoardState;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import chuss.Move.MoveType;


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

    public abstract void updateBoard();

    public abstract void playGame(Interactable whiteUser, Interactable blackUser);

} class CommandInterface extends UserInterface {

    //CONSTRUCTORS

    public CommandInterface(Board board) {
        //Main constructor for the CommandInterface

        super(board);

    }

    //OTHER

    public void playGame(Interactable whiteUser, Interactable blackUser) {
        //Starts a game using the UI and the board.
        //[TEST CODE] Probably will clean this up later

        board.setUsers(whiteUser, blackUser);
        updateBoard();
//        Scanner input = new Scanner(System.in);

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

                    System.out.println("Illegal move, try again");
                    illegalMove = true;

                }

                if(board.getState() == Board.BoardState.CHECKMATE) {

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

            } else again = false;

        } while(again);
        //Reprompt if the user asks for help

        return new Move(board, move);
        //Returns a Move object made from the SMN string

    }

    public void updateBoard() {
        //Prints the board in basic ASCII format.

        drawCaptured(Color.WHITE);

        System.out.println();
        //Adds a line before the board starts printing

        int squaresPrinted = 0;
        //Keeps track of the total number of squares that has been printed

        for(int y = board.getSize(); y >= 0; y--) {
            //Loop through y-values from top to bottom

            if(y != board.getSize()) System.out.println();
            //If not on the first iteration of y-loop, go down a line (avoids leading line break)
            System.out.print((y + 1) + " ");
            //Print the rank number

            for(int x = 0; x <= board.getSize(); x++) {
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

        for(int i = 0; i <= board.getSize(); i++) {
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

    public static class ChessBoardWithColumnsAndRows {

        private final JPanel gui = new JPanel(new BorderLayout(3, 3));
        private final JButton[][] chessBoardSquares = new JButton[8][8];
        private JPanel Board;
        private final JLabel message = new JLabel(
                "Chuss Champ is ready to play!");


        private static final String COLS = "ABCDEFGH";

        ChessBoardWithColumnsAndRows() {
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

            Board = new JPanel(new GridLayout(0, 9));
            Board.setBorder(new LineBorder(java.awt.Color.BLACK));
            gui.add(Board);

            // create the chess board squares
            Insets buttonMargin = new Insets(0,0,0,0);
            for (int ii = 0; ii < chessBoardSquares.length; ii++) {
                for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                    JButton b = new JButton();
                    b.setMargin(buttonMargin);
                    // our chess pieces are 64x64 px in size, so we'll
                    // 'fill this in' using a transparent icon..
                    ImageIcon icon = new ImageIcon(
                            new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                    b.setIcon(icon);
                    if ((jj % 2 == 1 && ii % 2 == 1)
                            //) {
                            || (jj % 2 == 0 && ii % 2 == 0)) {
                        b.setBackground(java.awt.Color.WHITE);
                    } else {
                        b.setBackground(java.awt.Color.BLACK);
                    }
                    chessBoardSquares[jj][ii] = b;
                }
            }

            //fill the chess board
            Board.add(new JLabel(""));
            // fill the top row
            for (int ii = 0; ii < 8; ii++) {
                Board.add(
                        new JLabel(COLS.substring(ii, ii + 1),
                                SwingConstants.CENTER));
            }
            // fill the black non-pawn piece row
            for (int ii = 0; ii < 8; ii++) {
                for (int jj = 0; jj < 8; jj++) {
                    if (jj == 0) {
                        Board.add(new JLabel("" + (ii + 1),
                                SwingConstants.CENTER));
                    }
                    Board.add(chessBoardSquares[jj][ii]);
                }
            }
        }

        public final JComponent getChessBoard() {
            return Board;
        }

        public final JComponent getGui() {
            return gui;
        }

        public static void main(String[] args) {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    ChessBoardWithColumnsAndRows cb =
                            new ChessBoardWithColumnsAndRows();

                    JFrame f = new JFrame("CHUSS");
                    f.add(cb.getGui());
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
    }


}