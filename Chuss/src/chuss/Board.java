package chuss;

//TODO: Add tag key in README
//The Board object. Stores all necessary info about the current game.

import chuss.Piece.Color;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Board {

    //FIELDS

    private final boolean DEBUG = true;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class
    private final int size = 8;
    //The amount of tiles (width and height) on the board
    //TODO: Potential issues with board size > 10, must fix
    private final int tSize = size - 1;
    //Used when referencing the "true size," usually when iterating starting from 0
    private Piece[][] board;
    //The Piece array that represents the actual chess board
    private Color turn;
    //The side that whose turn it is
    private Point wKingPos;
    //The current coordinates of the white King
    private Point bKingPos;
    //The current coordinates of the black King
    private List<Piece> wPieces;
    //A list of all white pieces on the board
    private List<Piece> bPieces;
    //A list of all black pieces on the board
    private final UserInterface ui;
    //The UI for the board
    private Interactable whiteUser;
    //The Interactable that is controlling the white side
    private Interactable blackUser;
    //The Interactable that is controlling the black side

    //TODO: Remove hard-coded initializers

    //CONSTRUCTORS

    public Board() {
        //The default constructor for the Board object, creates a board
        //from the standard starting chess layout FEN string.

         this("rnbqkbnr/pppppppp/////PPPPPPPP/RNBQKBNR");
         //Call the FEN constructor with the FEN string for the default chess setup

    }

    public Board(String fen) {
        //The proper constructor for the Board object, creates a board
        //by interpreting a FEN string into a board layout.

        turn = Color.WHITE;
        //Sets the starting turn to white
        board = readFen(fen);
        //Set the board field containing the piece array
        //to the array returned by the FEN interpreter
        wKingPos = findKing(Color.WHITE);
        //Finds and stores the white King's coordinates
        if(DEBUG) System.out.println("White King is at [" + wKingPos.x + ", " + wKingPos.y + "]");
        //[DEBUG TEXT] Prints out where the White King is
        bKingPos = findKing(Color.BLACK);
        //Finds and stores the black King's coordinates
        ui = new CommandInterface(this);
        //Stores the UserInterface that the board is using
        whiteUser = null;
        //Initializes the white user
        blackUser = null;
        //Initializes the black user
        if(DEBUG) System.out.println("Black King is at [" + bKingPos.x + ", " + bKingPos.y + "]");
        //[DEBUG TEXT] Prints out where the black King is

    }

    //ACCESSORS

    public int getSize() {

        return tSize;

    }

    public Piece pieceAt(int x, int y) {

        return pieceAt(new Point(x, y));

    }

    public Piece pieceAt(Point boardPos) {

        if(board[boardPos.x][boardPos.y] != null) return board[boardPos.x][boardPos.y];
        else return null;

    }

    public Color getTurn() {

        return turn;

    }

    public UserInterface getUI() {

        return ui;

    }

    public Interactable getCurrentUser() {

        if(turn == Color.WHITE) return whiteUser;
        else return blackUser;

    }

    //MUTATORS

    public void doMove(Move move) {
        //Performs a move on the board when passed a move object.

        if(move.getMovedPiece() == null) throw new IllegalArgumentException("ERROR: No piece at starting position");
        //If there is no piece at the starting position, throw an Illegal Argument

        if(DEBUG) System.out.println("Moving piece: " + move.getMovedPiece().getString());
        //[DEBUG TEXT] Prints the string of the piece being moved

        if(!move.getMovedPiece().isLegal(move)) throw new IllegalArgumentException("ERROR: Illegal move");
        //If the move is illegal for this piece type, throw an IllegalArgument

        if(findCheck(move)) throw new IllegalArgumentException("ERROR: Move cannot result in self-check");
        //If the King is put in check by this move, throw an IllegalArgument
        //TODO: Move to isLegal or somewhere where it will be supported by a move sorting algorithm as a base legality check

        move.getMovedPiece().incMoveCount(1);
        //Add 1 to the moveCount of the moved piece

        board[move.getEndX()][move.getEndY()] = move.getMovedPiece();
        //Sets the tile at the end position to the moved piece
        board[move.getStartX()][move.getStartY()] = null;
        //Sets the tile at the start position to null (empty tile)

        if(move.getMovedPiece() instanceof King) {
            //If the moved piece is a King

            if(turn == Color.WHITE) wKingPos = move.getEndPos();
            //If the King is white, make the move's end position the new wKingPos
            else bKingPos = move.getEndPos();
            //If the King is black, make the move's end position the new bKingPos

        }

        if(turn == Color.WHITE) turn = Color.BLACK;
        else turn = Color.WHITE;
        //Change the current turn color

        generateFen();
        //[TEST CODE] Generates the FEN string of the board every time a move is made

    }

    public void setUser(Interactable user, Color color) {
        //Sets either user based on the Interactable passed in.
        //TODO: Make a setUser for both colors at once

        if(color == Color.WHITE) whiteUser = user;
        else blackUser = user;

    }

    public void setUsers(Interactable whiteUser, Interactable blackUser) {
        //Sets both users using a function chain.

        setUser(whiteUser, Color.WHITE);
        setUser(blackUser, Color.BLACK);

    }

    //OTHER

    private Piece[][] readFen(String fen) {
        //Takes in a FEN string and turns it into a Piece array (board).
        //TODO: Add full FEN interpretation, not just board layout

        Piece[][] fenBoard = new Piece[size][size];
        //Initialize a board to add pieces to and return
        char[] fenArr = fen.toCharArray();
        //Create an array of characters from the FEN string

        wPieces = new ArrayList<>();
        bPieces = new ArrayList<>();
        //Either initializes or resets the ArrayLists for each side's pieces


        int x = 0;
        int y = tSize;
        //Start at the top-left corner (0, 7)

        if(DEBUG) System.out.println("Fen: " + fen);
        //[DEBUG TEXT] Print the FEN string

        for(char c : fenArr) {
            //For each char in the FEN string:
            //TODO: Possibly convert to HashMap

            if(c >= '1' && c <= '9') x += (c - 49);
                //If c is between num 1 through 8, add num - 1 to the x value
                //TODO: Remove hard-coded chars?
            else if(c == '/') {
                x = -1;
                //Go back to the start of the rank
                y--;
                //Go down a rank
            }
            else if(c == 'P') {
                Piece p = new Pawn(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'R') {
                Piece p = new Rook(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'N') {
                Piece p = new Knight(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'B') {
                Piece p = new Bishop(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'Q') {
                Piece p = new Queen(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'K') {
                Piece p = new King(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'E') {
                Piece p = new Earl(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'M') {
                Piece p = new Monk(Color.WHITE, x, y);
                fenBoard[x][y] = p;
                wPieces.add(p);
            }
            else if(c == 'p') {
                Piece p = new Pawn(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'r') {
                Piece p = new Rook(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'n') {
                Piece p = new Knight(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'b') {
                Piece p = new Bishop(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'q') {
                Piece p = new Queen(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'k') {
                Piece p = new King(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'e') {
                Piece p = new Earl(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else if(c == 'm') {
                Piece p = new Monk(Color.BLACK, x, y);
                fenBoard[x][y] = p;
                bPieces.add(p);
            }
            else throw new IllegalArgumentException("ERROR: Invalid FEN string");
            //If c is not a recognized character, throw an IllegalArgument

            x++;
            //Every time the current character has been checked, move over a tile

        }

        return fenBoard;
        //Return the new board with the pieces on it

    }

    private String generateFen() {
        //Uses the current board to generate a FEN String.

        StringBuilder fen = new StringBuilder();
        //Create a StringBuilder to add to and return

        for(int y = tSize; y >= 0; y--) {
            //Starts at the top of the board and iterates down

            for(int x = 0; x <= tSize; x++) {
                //Starts at the left of the board and iterates right

                Piece t = board[x][y];
                //Creates a Piece object called "t" to represent the tile
                Color tC = null;
                if(t != null) tC = t.getColor();
                //Stores the color of the Piece rather than having to call .getColor() every time

                int lastI = fen.length() - 1;
                //Creates an int representing the last index of the StringBuilder

                char lastC = '#';
                if(fen.length() > 0) lastC = fen.charAt(lastI);
                //Creates a char representing the last char in the StringBuilder

                if(t == null) {
                    if(lastC >= '1' && lastC <= '9') fen.setCharAt(lastI, (char) (lastC + 1));
                    else fen.append('1');
                } else if(t instanceof Pawn) {
                    if(tC == Color.WHITE) fen.append("P");
                    else fen.append("p");
                } else if(t instanceof Rook) {
                    if(tC == Color.WHITE) fen.append("R");
                    else fen.append("r");
                } else if(t instanceof Knight) {
                    if(tC == Color.WHITE) fen.append("N");
                    else fen.append("n");
                } else if(t instanceof Bishop) {
                    if(tC == Color.WHITE) fen.append("B");
                    else fen.append("b");
                } else if(t instanceof Queen) {
                    if(tC == Color.WHITE) fen.append("Q");
                    else fen.append("q");
                } else if(t instanceof King) {
                    if(tC == Color.WHITE) fen.append("K");
                    else fen.append("k");
                } else if(t instanceof Earl) {
                    if(tC == Color.WHITE) fen.append("E");
                    else fen.append("e");
                } else if(t instanceof Monk) {
                    if(tC == Color.WHITE) fen.append("M");
                    else fen.append("m");
                }

            }

            if(y != 0) fen.append('/');

        }

        String fenStr = fen.toString();

        if(DEBUG) System.out.println("Generated FEN: " + fenStr);
        //[DEBUG TEXT] Prints the FEN string generated by the method

        return fenStr;

    }

    private Point findKing(Color color) {
        //Finds the King of a given color and returns its position on the board in terms of a Point.
        //TODO: Use ArrayList instead of whole board

        for(int x = 0; x <= tSize; x++) {
            //Loop through ranks

            for(int y = 0; y <= tSize; y++) {
                //Loop through columns

                if(board[x][y] == null) continue;
                //If the piece at the current tile is null, check the next tile
                if(board[x][y] instanceof King && board[x][y].getColor() == color) return new Point(x, y);
                //If the piece at the current tile is a King and of the correct color, return its coordinates

            }

        }

        return null;
        //Should never happen as the King cannot be captured under standard game rules

    }

    public boolean findCheck(Move move) {
        //Looks to see if the King of a given color will be in check at the end of a given move.
        //TODO: Restructure doMove and findCheck so you can pass a FEN instead of a move to findCheck

        String origFen = generateFen();
        //Stores the current board in a FEN string before modifying it

        Point kingPos;
        if(turn == Color.WHITE) kingPos = wKingPos;
        else kingPos = bKingPos;
        if(move.getMovedPiece() instanceof King) kingPos = move.getEndPos();
        //Stores the King position for the given color
        //TODO: Move "if King is moved" check to helper method forceMove

        if(DEBUG) System.out.println("King is at [" + kingPos.x + ", " + kingPos.y + "]");
        //[DEBUG TEXT] Prints out where the King is

        board[move.getEndX()][move.getEndY()] = move.getMovedPiece();
        //Sets the tile at the end position to the moved piece
        board[move.getStartX()][move.getStartY()] = null;
        //Sets the tile at the start position to null (empty tile)
        //TODO: Move this to helper method forceMove

        for(int x = 0; x <= tSize; x++) {
            //Loop through ranks

            for(int y = 0; y <= tSize; y++) {
                //Loop through columns

                //If the piece at the current tile is null, check the next tile
                if(board[x][y] != null && board[x][y].getColor() != turn && board[x][y].isLegal(new Move(this, x, y, kingPos, true))) {
                    //If the color of this piece matches the color that was passed in and it is attacking the King

                    if(DEBUG) {
                        //[DEBUG TEXT] Prints a statement indicating which piece is putting the King in check

                        if(turn == Color.WHITE) System.out.print("WHITE ");
                        else System.out.print("BLACK ");
                        System.out.print("King is currently being put IN CHECK by " + board[x][y].getString() + "\n");

                    }

                    board = readFen(origFen);
                    //Rebuild the board with the original FEN string

                    return true;
                    //TODO: Add checkmate test
                    //TODO: Add support for multiple attacks at once? (Not sure if this is necessary)

                }

            }

        }

        board = readFen(origFen);
        //Rebuild the board with the original FEN string

        return false;

    }

}