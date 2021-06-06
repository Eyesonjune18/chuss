package chuss;

//Abstract type for UIs, allows for two different types of interaction with the Board object
//using either a command line (console/ASCII) or a graphic interface (program window)

import chuss.Board.BoardState;
import chuss.Piece.Color;
import chuss.Piece.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

;

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

    private final GraphicInterface gui;

    //CONSTRUCTORS

    public CommandInterface(final String string, int width, int height, final Board board) throws IOException {
        //Main constructor for the CommandInterface

        super(board);
        gui = new GraphicInterface(string,width,height,board);

    }

    //OTHER

    public void playGame(Interactable whiteUser, Interactable blackUser) {
        //Starts a game using the UI and the board.
        //[TEST CODE] Probably will clean this up later

        board.setUsers(whiteUser, blackUser);
        updateBoard();
        Scanner input = new Scanner(System.in);

        boolean gameOver = false;

        while (!gameOver) {

            boolean illegalMove;

            do {

                try {

//                    System.out.println("Press enter to move");
//                    String s = input.nextLine();

                    board.doMove(board.getCurrentUser().getMove());
                    illegalMove = false;

                } catch (IllegalArgumentException e) {

                    System.out.println(e.getMessage());
                    illegalMove = true;

                }

                if (board.getState() == BoardState.CHECKMATE) {

                    System.out.println("\n" + board.getTurn().toString() + " is now checkmated. Game over!");

                    gameOver = true;
                    illegalMove = false;

                } else if (board.getState() == BoardState.CHECK) {

                    System.out.println("\n" + board.getTurn().toString() + " has been put in check!");
                    illegalMove = false;

                } else if (board.getState() == BoardState.STALEMATE) {

                    System.out.println("\n" + board.getTurn().toString() + " is now stalemated. Game over!");
                    gameOver = true;
                    illegalMove = false;

                }

            } while (illegalMove);

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

            if (move.equals("?")) {
                //If the user asks for help

                System.out.printf("%nHELP: To move, you must type a move in the following format:"
                        + "%n<STARTING TILE> <ENDING TILE>"
                        + "%nThe starting tile is the tile of the piece you wish to move."
                        + "%nThe ending tile is the tile you wish to move your piece to."
                        + "%nEach tile is notated with \"<COLUMN><RANK>\", example: \"e5\""
                        + "%n%nFull example move: \"a5 g5\"%n");

                again = true;

            } else if (move.equalsIgnoreCase("CASTLE")) {

                System.out.print("Enter the movement of the King: ");

                move = input.nextLine();

                return new Move(board, move, true);

            } else again = false;

        } while (again);
        //Reprompt if the user asks for help

        return new Move(board, move);
        //Returns a Move object made from the SMN string

    }

    public PieceType promptPromotion() {

        Scanner input = new Scanner(System.in);
        System.out.print("Pawn has promotion available.\nWhich piece to promote to? ");

        while (true) {

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

            if (promotion == PieceType.NONE) {

                System.out.print("\nInvalid type, please try again: ");
                continue;

            }

            return promotion;

        }

    }


    public void updateBoard() {
        //Prints the board in basic ASCII format.

        gui.updateButtons();

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
        //Prints captured pieces of either java.awt.Color.

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

    /**
     * Controls the visual and interactive aspect of the game.
     * @author Pashan Irani
     * @version 1.0
     */
    public class GraphicInterface extends JFrame {

        /**
         * serialVersion of GUI.
         */
        private static final long serialVersionUID = -1154461466047177754L;
        private Board gameBoard;
        private JFrame frame;
        private JButton[][] squares; //squares on board
        private final JPanel board = new JPanel(); //panel to display chess board on
        private final JButton[][] gridButtons; //used for squares to take input
        private final String defualtTitle;
        //         *Peices
        private final JLabel[][] chessPieceImages = new JLabel[2][6];
        //private final UserInterface ui;
        private JPanel gui = new JPanel(new BorderLayout(3, 3));

        /* the colors */
        private java.awt.Color color1 = java.awt.Color.decode("#8f0300"); //black
        private java.awt.Color color2 = java.awt.Color.decode("#fad481"); //white
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem saveButton;
        JMenuItem loadButton;
        JMenuItem resetButton;

        public Controller controller;                    ///< The view has access to the Controller
        ///< Will add 64 Buttons to this panel, and add the JPanel to the JFrame
        public java.awt.Button[][] button = new java.awt.Button[8][8];    ///< 64 Buttons on Panel

        private final int frameHeight = 739;            ///< Picked to make a square JFrame of ~600 pixels.
        private final int frameWidth = 917;                ///< Picked to make a square JFrame of ~600 pixels.
        private final int boardWidth = 600;            ///< Approximate number of Pixels on square board
        private final int squareIconLength = 75;        ///< length of square's edge for the icons we're using

        public java.awt.Button undoButton;                        ///< Allows for undoing chess moves
        public java.awt.Button redoButton;                        ///< Allows for redoing chess moves

        public JTextField statusBox;                    ///< Used to display information when WHITE or BLACK is in check

        public JTextField whiteName;                    ///< Used to display unique name for WHITE Player
        public JTextField blackName;                    ///< Used to display unique name for BLACK Player

        public JTextField whiteScore;                    ///< Used to display number of games WHITE has won
        public JTextField blackScore;                    ///< Used to display number of games BLACK has won

        public java.awt.Button whiteForfeit;                    ///< Used to give WHITE the option to forfeit a game
        public java.awt.Button blackForfeit;                    ///< Used to give BLACK the option to forfeit a game

        public java.awt.Button whiteRestart;                    ///< Used to give WHITE the option to restart a game (if opponent agrees)
        public java.awt.Button blackRestart;                    ///< Used to give BLACK the option to restart a game (if opponent agrees)

        public java.awt.Button classicMode;                        ///< Used to create a new game in "Classic" mode
        public java.awt.Button customMode;
        JFileChooser fileChose;
        private static final String COLS = "ABCDEFGH";
        public static final int QUEEN = 0, KING = 1,
                ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
        public static final int[] STARTING_ROW = {
                ROOK, KNIGHT, BISHOP, KING, QUEEN, BISHOP, KNIGHT, ROOK};


        /**
         * Constructor for GUI.
         *
         * @param string window title
         */
        public GraphicInterface(final String string, int width, int height, final Board board) {
            super(string);
            defualtTitle = string;
            gameBoard = board;
            //screen width in pixels
            //screen height in pixels
            setSize(width, height);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); //centers on relative to screen
            setLayout(new BorderLayout());
            this.board.setLayout(new GridLayout(8, 8));
            add(this.board, BorderLayout.CENTER);
            gridButtons = new JButton[squares.length][squares.length];

            constructMenu();
            createInterfaceButtons(gui);


        }



        /**
         * Constructs Menu.
         */
        private void constructMenu() {
            menuBar = new JMenuBar();
            menu = new JMenu("File");
            saveButton = new JMenuItem("Save");
            loadButton = new JMenuItem("Load");
            resetButton = new JMenuItem("Reset Board");
            saveButton.addActionListener(new ButtonClicked("save"));
            loadButton.addActionListener(new ButtonClicked("load"));
            resetButton.addActionListener(new ButtonClicked("reset"));
            menu.add(saveButton);
            menu.add(loadButton);
            menu.add(resetButton);
            menuBar.add(menu);
            setJMenuBar(menuBar);
            fileChose = new JFileChooser();

        }


        public final Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            Dimension prefSize = null;
            Component c = getParent();
            if (c == null) {
                prefSize = new Dimension(
                        (int) d.getWidth(), (int) d.getHeight());
            } else if (c != null &&
                    c.getWidth() > d.getWidth() &&
                    c.getHeight() > d.getHeight()) {
                prefSize = c.getSize();
            } else {
                prefSize = d;
            }
            int w = (int) prefSize.getWidth();
            int h = (int) prefSize.getHeight();
            // the smaller of the two sizes
            int s = (Math.min(w, h));
            return new Dimension(s, s);
        }

        public void initialize(Board chessBoard, Controller controller) {
            this.gameBoard = chessBoard;
            this.controller = controller;

            /* Initializes Swing Variables */
            gui = new JPanel(null);
            for (int row = 0; row < chessBoard.rows; row++) {
                for (int column = 0; column < chessBoard.columns; column++) {
                    button[row][column] = new Button(column, row);
                }
            }
            /* Set up JFrame */
            frame.setSize(frameWidth, frameHeight);
            frame.setLocationRelativeTo(null);                        //null indicates the window is placed in the center of the screen
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            /* Loop through board - Create 64 Buttons. Add them to the JPanel */
            for (int row = 0; row < chessBoard.rows; row++) {
                for (int column = 0; column < chessBoard.columns; column++) {

                    /* Create Buttons */
                    Piece currentPiece = chessBoard.tile[row][column];
                    java.awt.Button currentButton = button[row][column];
                    if (currentPiece != null)
                        currentButton.setIcon(currentPiece.image);

                    /* Set bounds for the button */
                    int xPositionGUI = getXPositionGUI(column);
                    int yPositionGUI = getYPositionGUI(row);
                    currentButton.setBounds(xPositionGUI, yPositionGUI, squareIconLength, squareIconLength);

                    setBackground(currentButton);

                    gui.add(currentButton);
                }
            }
            createInterfaceButtons(gui);
            updateButtons();

            frame.setContentPane(gui);    //add the JPanel to the JFrame
            frame.setVisible(true);
        }


        public void updateButtons() {
            /* Show which player's turn it is */
            if (chessBoard.Turn == Color.WHITE) {
                blackName.setBackground(null);
                whiteName.setBackground(java.awt.Color.GREEN);
                whiteForfeit.setEnabled(true);
                blackForfeit.setEnabled(false);
                whiteRestart.setEnabled(true);
                blackRestart.setEnabled(false);
            } else {
                blackName.setBackground(java.awt.Color.GREEN);
                whiteName.setBackground(null);
                whiteForfeit.setEnabled(false);
                blackForfeit.setEnabled(true);
                whiteRestart.setEnabled(false);
                blackRestart.setEnabled(true);
            }

            /* Determine if players are in check or checkmate */
            if (chessBoard.winner == Color.WHITE) {
                javax.swing.JOptionPane.showMessageDialog(null, "Checkmate. White Wins!");
                controller.whiteGamesWon++;
                controller.reset();
            } else if (chessBoard.winner == Color.BLACK) {
                javax.swing.JOptionPane.showMessageDialog(null, "Checkmate. Black Wins!");
                controller.blackGamesWon++;
                controller.reset();
            } else if (chessBoard.whiteInCheck) {
                statusBox.setText("White In Check");
                statusBox.setBackground(java.awt.Color.YELLOW);
            } else if (chessBoard.blackInCheck) {
                statusBox.setText("Black In Check");
                statusBox.setBackground(java.awt.Color.YELLOW);
            } else {
                statusBox.setText("Have Fun!!!");
                statusBox.setBackground(null);
            }
            gui.updateUI();
        }


        /**
         * Create the JButtons, Buttons, and JTextFields for the View
         *
         * @param panel The panel we are attaching the buttons to
         */
        public void createInterfaceButtons(JPanel panel) {
            createNames(panel);
            createScores(panel);
            createUndoButton(panel);
            createRedoButton(panel);
            createStatusBox(panel);
            createForfeitButtons(panel);
            createRestartButtons(panel);
            createModeButtons(panel);
        }

        /**
         * Creates unique names (JTextfields) for BLACK and WHITE
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createNames(JPanel panel) {
            blackName = new JTextField("Black Player: Player 2");
            blackName.setBounds(650, 20, 200, 40);
            blackName.setBackground(null);
            blackName.setBorder(null);
            blackName.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(blackName);
            whiteName = new JTextField("White Player: Player 1");
            whiteName.setBounds(650, 540, 200, 40);
            whiteName.setBackground(java.awt.Color.GREEN);
            whiteName.setBorder(null);
            whiteName.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(whiteName);
        }


        /**
         * Creates scores for BLACK and WHITE
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createScores(JPanel panel) {
            whiteScore = new JTextField("White Score: " + controller.whiteGamesWon);
            whiteScore.setBounds(610, 480, 100, 40);
            whiteScore.setEditable(false);
            whiteScore.setBorder(null);
            panel.add(whiteScore);
            blackScore = new JTextField("Black Score: " + controller.blackGamesWon);
            blackScore.setBounds(610, 80, 100, 40);
            blackScore.setBorder(null);
            blackScore.setEditable(false);
            panel.add(blackScore);
        }

        /**
         * Creates an "Undo" JButton that lets us undo our previous move
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createUndoButton(JPanel panel) {
            undoButton = new java.awt.Button("Undo");
            undoButton.setBounds(630, 250, 100, 40);
            panel.add(undoButton);
        }

        /**
         * Creates a "Redo" JButton that lets us redo our previous move
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createRedoButton(JPanel panel) {
            redoButton = new java.awt.Button("Redo");
            redoButton.setBounds(630, 310, 100, 40);
            panel.add(redoButton);
        }

        /**
         * Creates a status box as a JTextField that will tell us when a player is in check
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createStatusBox(JPanel panel) {
            statusBox = new JTextField("Have Fun!!!");
            statusBox.setBounds(250, 620, 110, 40);
            statusBox.setBorder(null);
            statusBox.setBackground(null);
            statusBox.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(statusBox);


        }

        /**
         * Creates a "Forfeit" JButton that lets us resign from a game and take a loss
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createForfeitButtons(JPanel panel) {
            whiteForfeit = new java.awt.Button("White Forfeit");
            whiteForfeit.setBounds(720, 500, 150, 30);
            panel.add(whiteForfeit);
            blackForfeit = new java.awt.Button("Black Forfeit");
            blackForfeit.setBounds(720, 70, 150, 30);
            panel.add(blackForfeit);
        }

        /**
         * Creates a "Restart" JButton that lets us restart a game (if opponent also wants to restart)
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createRestartButtons(JPanel panel) {
            whiteRestart = new java.awt.Button("White Restart");
            whiteRestart.setBounds(720, 460, 150, 30);
            panel.add(whiteRestart);
            blackRestart = new java.awt.Button("Black: Restart");
            blackRestart.setBounds(720, 110, 150, 30);
            panel.add(blackRestart);
        }

        /**
         * Creates JButtons that let us start new games in Classic or Custom mode
         *
         * @param panel The panel to attach the JTextFields to
         */
        public void createModeButtons(JPanel panel) {
            classicMode = new java.awt.Button("New Game - Classic Mode");
            classicMode.setBounds(450, 660, 200, 30);
            panel.add(classicMode);
            customMode = new java.awt.Button("New Game - Custom Mode");
            customMode.setBounds(670, 660, 200, 30);
            panel.add(customMode);
        }

        /**
         * Create a checkered background by painting every other Button a gray color
         *
         * @param currentButton The Button we want to set the background color for
         */
        public void setBackground(java.awt.Button button) {
            if ((button.xPos + button.yPos) % 2 == 0)
                button.setBackground(java.awt.Color.GRAY);
            else
                button.setBackground(null);
        }

        /**
         * Calculates "x" Position on GUI for ChessPiece
         *
         * @param x the "x" Position from 0-7
         * @return the "x" coordinate on the GUI from 0 - 525 (viewablePixels)
         */
        public int getXPositionGUI(int x) {
            return x * squareIconLength;
        }

        /**
         * Calculates "y" Position on GUI for ChessPiece
         *
         * @param y the "y" Position from 0-7
         * @return the "y" coordinate on the GUI from 0 - 525 (for boardWidth of 600)
         */
        public int getYPositionGUI(int y) {
            return boardWidth - (y * squareIconLength) - squareIconLength;
        }

        /**
         * Returns the Button at desired Point
         *
         * @param button Point of desired Button
         * @return ChessPiece corresponding to Point
         */
        public java.awt.Button getButton(Point point) {
            return button[point.y][point.x];
        }

        /**
         * Updates the ImageIcon for a Button
         *
         * @param image  Our new desired ImageIcon
         * @param button The Button to update with a new Image
         */
        public void setIcon(ImageIcon image, java.awt.Button button) {
            Piece.setIcon(image);
        }

        /**
         * Adds an MouseListener to a JButton
         *
         * @param mouseListener MouseListener to add (to a Button)
         * @param button        JButton to add a MouseListener to
         */
        public void addMouseListener(MouseListener mouseListener, java.awt.Button button) {
            button.addMouseListener(mouseListener);
        }


        /**
         * refreshes window.
         */
        public void refreshWindow() {
            invalidate();
            validate();
            repaint();
        }

        /**
         * Draws Squares.
         */
        public void drawSquares() {
            Color setWhite = null;
            board.removeAll();
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; true; j++) { //Draws grid
                    squares[i][j].setBackground(java.awt.Color.WHITE);
                    gridButtons[i][j] = new JButton();
                    assert false;
                    giveButtonAttributes(gridButtons[i][j], setWhite, (i + 1) + "" + (j + 1));
                    updateSquarePiece(squares[i][j], gridButtons[i][j]);
                    board.add(gridButtons[i][j]);
                }
            }
        }

        private void updateSquarePiece(JButton jButton, JButton jButton1) {
        }

        /**
         * Sets up buttons visual appearance.
         *
         * @param button the button to update
         * @param black  true if square is black; white if false
         * @param id     gives square an address
         */
        private void giveButtonAttributes(final JButton button, Color black, final String id) {
            button.setFocusable(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            //button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.addActionListener(new ButtonClicked(id));
            button.setName(id);

            if (true) {
                button.setBackground(java.awt.Color.WHITE);
            } else {
                button.setBackground(java.awt.Color.BLACK);
            }
        }


        /**
         * Change button's (square) color.
         *
         * @param color the color to change to
         * @param xaxis location on x axis
         * @param yaxis location on y axis
         */
        public void buttonColorChange(final String color, int xaxis, int yaxis) {
            JButton button = gridButtons[xaxis][yaxis];
            button.setBackground(java.awt.Color.decode(color));
            refreshWindow();
        }

        /**
         * updates the current square.
         *
         * @param square the square to update
         * @param button the square's visible representation.
         */
        public void updateSquarePiece(final JButton square, final JButton button, MouseEvent e) {
            Piece currentPiece = gameBoard.pieceAt(e.getX(), e.getY());

        }

        /**
         * Updates Title of window.
         *
         * @param string the string to update it to
         */
        public void updateTitle(String string) {
            setTitle(defualtTitle + string);
        }




            /**
             * Runs at every click, and sends clicked square id to doGameLogic method in board
             * which deals with logic.
             */
            public void actionPerformed(final ActionEvent event) {

                if (id.equalsIgnoreCase("save")) {
                    System.out.println("s: " + id);
                    try {
                        choseFile("save");
                        gameBoard.saveGame(addExtention());
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                } else if (id.equalsIgnoreCase("load")) {
                    choseFile("load");
                    try {
                        gameBoard.loadGame(fileChose.getSelectedFile() + "");
                    } catch (ClassNotFoundException exp) {
                        JOptionPane.showMessageDialog(board, "Corrupted File");
                    } catch (IOException exp) {
                        JOptionPane.showMessageDialog(board, "No File Found");
                    }

                } else if (id.equalsIgnoreCase("reset")) {
                    gameBoard.resetBoard();
                } else {
                    System.out.println(id);
                    gameBoard.doGameLogic(id);
                    // Game Logic == BoardState?
                }
            }


            /**
             * Opens chose file menu.
             *
             * @param message display message
             */
            private void choseFile(String message) {
                if (message.equalsIgnoreCase("save")) {
                    fileChose.showSaveDialog(this);
                } else {
                    fileChose.showOpenDialog(this);
                }
            }

            /**
             * adds extension of file name.
             *
             * @return new name
             */
            private String addExtention() {
                String fileName = fileChose.getSelectedFile() + "";
                int fileNameLength = fileName.length();
                int extentionLength = ".gam".length();
                boolean addExtention = false;
                int jfor = 0;
                for (int i = fileNameLength - extentionLength; i < fileNameLength; i++) {
                    System.out.println(fileName.charAt(i) + "-------" + ".gam".charAt(jfor));
                    if (fileName.charAt(i) != ".gam".charAt(jfor)) {
                        addExtention = true;
                        break;
                    }
                    jfor++;
                }

                if (addExtention) {
                    return fileName + ".gam";
                } else {
                    return fileName;
                }
            }
        }





