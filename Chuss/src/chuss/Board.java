package chuss;

//TODO: Add tag key in README
//The Board object. Stores all necessary info about the current game.

import chuss.Piece.Color;
import java.awt.*;
import java.util.Random;

public class Board {

    //ENUMS

    public enum BoardState {CHECK, CHECKMATE, STALEMATE, NONE}

    //FIELDS

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class
    private final int size = 8;
    //The amount of tiles (width and height) on the board
    //TODO: Potential issues with board size > 10, must fix
    private final int tSize = size - 1;
    //Used when referencing the "true size," usually when iterating starting from 0
    private final Piece[][] board;
    //The Piece array that represents the actual chess board
    private Color turn;
    //The side that whose turn it is
    private Point wKingPos, bKingPos;
    //The current coordinates of the white and black Kings
    private ArrList<Piece> wPieces, bPieces, aPieces;
    //A list of the pieces for each side, and another list for all pieces combined
    private final ArrList<String> wCaptured, bCaptured;
    //A list of captured pieces for each side
    private Interactable wUser, bUser;
    //The Interactables that are controlling the white and black side
    private BoardState state;
    //What state the board is in (check, checkmate, etc)
    private int moves;
    //How many moves have occurred
    private int halfMove;
    //How many moves have occurred since a piece was captured or a pawn was moved

    //CONSTRUCTORS

    public Board() {
        //The default constructor for the Board object, creates a board
        //from the standard starting chess layout FEN string.

         this("rnbqkbnr/emppppme/////EMPPPPME/RNBQKBNR#00000000000000000000000000000000#W");
         //Call the FEN constructor with the FEN string for the default chess setup

    }

    public Board(String fen) {
        //The proper constructor for the Board object, creates a board
        //by interpreting a FEN string into a board layout.

        turn = Color.WHITE;
        board = readFen(fen);
        //Set the board field containing the piece array
        //to the array returned by the FEN interpreter

        wKingPos = findKing(Color.WHITE);
        if(DEBUG) System.out.println("White King is at [" + wKingPos.x + ", " + wKingPos.y + "]");
        bKingPos = findKing(Color.BLACK);
        if(DEBUG) System.out.println("Black King is at [" + bKingPos.x + ", " + bKingPos.y + "]");

        wCaptured = new ArrList<>();
        bCaptured = new ArrList<>();

        wUser = null;
        bUser = null;

        state = BoardState.NONE;

    }

    //ACCESSORS

    public int getSize() {

        return size;

    }

    public int getZeroSize() {

        return tSize;

    }

    public int getMoves() {

        return moves;

    }

    public Piece pieceAt(Point boardPos) {

        if(board[boardPos.x][boardPos.y] != null) return board[boardPos.x][boardPos.y];
        else return null;

    }

    public Piece pieceAt(int x, int y) {

        return pieceAt(new Point(x, y));

    }

    public Color getTurn() {

        return turn;

    }

    public ArrList<String> getCaptured(Color color) {

        if(color == Color.WHITE) return wCaptured;
        else return bCaptured;

    }

    public Interactable getCurrentUser() {

        if(turn == Color.WHITE) return wUser;
        else return bUser;

    }

    public BoardState getState() {

        return state;

    }

    public ArrList<Piece> getPieces(Color color) {

        if(color == Color.WHITE) return wPieces;
        else return bPieces;

    }

    private Interactable getUser(Color color) {

        if(color == Color.WHITE) return wUser;
        else return bUser;

    }

    public int pieceCount() {
        //Gets the total piece count.

        return aPieces.size();

    }

    public int pieceCount(Color color) {
        //Gets the amount of pieces for a given side.

        return getPieces(color).size();

    }

    private int pieceTypeCount(Class<? extends Piece> type) {
        //Counts the amount of a given Piece type in the board.

        int count = 0;

        for(Piece p : aPieces) if(p.getClass().equals(type)) count++;

        return count;

    }

    //MUTATORS

    public void setUser(Interactable user, Color color) {
        //Sets either user based on the Interactable passed in.

        if(color == Color.WHITE) wUser = user;
        else bUser = user;

    }

    public void setUsers(Interactable whiteUser, Interactable blackUser) {
        //Sets both users using a function chain.

        setUser(whiteUser, Color.WHITE);
        setUser(blackUser, Color.BLACK);

    }

    private void updateKingPos(Piece piece) {
        //Takes a piece and determine if it is a King. If it is,
        //then changes wKingPos or bKingPos accordingly.

        if(piece instanceof King) {

            if(piece.getColor() == Color.WHITE) wKingPos = piece.getPos();
            else bKingPos = piece.getPos();

        }

    }

    private void findEndGame() {
        //Checks various endgame requirements to see if there is a checkmate or a stalemate.

        int legalMoves = getLegalMoves(turn).size();

        if(legalMoves == 0) {

            if(state == BoardState.CHECK) state = BoardState.CHECKMATE;
            else state = BoardState.STALEMATE;

        }

        int pieceCount = aPieces.size();

        if(pieceCount == 2 ||
                (pieceCount == 3 && pieceTypeCount(Bishop.class) == 1) ||
                (pieceCount == 3 && pieceTypeCount(Knight.class) == 1)) state = BoardState.STALEMATE;
        //Catches "dead positions" where a checkmate cannot physically happen due to insufficient pieces
        //TODO: Add 2 Kings, 2 Bishops on same color tile

        if(halfMove >= 50) state = BoardState.STALEMATE;
        //If there has been 50 moves without any captures or pawn moves, creates a stalemate

    }

    private void updatePiece(Piece piece, boolean remove) {
        //Updates the piece ArrLists for the given color.
        //"remove" tells the function whether to add or remove the piece.

        ArrList<Piece> pieces = getPieces(piece.getColor());

        if(!remove) {

            pieces.add(piece);
            aPieces.add(piece);

        } else {

            pieces.remove(piece);
            aPieces.remove(piece);

        }

    }

    private void updateCaptured(Piece piece, boolean remove) {
        //Updates the captured pieces for the given color.
        //"remove" tells the function whether to add or remove the capture.

        ArrList<String> captured = getCaptured(piece.getColor());

        if(!remove) captured.add(piece.getString());
        else captured.remove(piece.getString());

    }

    //OTHER

    private Piece[][] readFen(String fen) {
        //Takes in a FEN string and turns it into a Piece array (board).
        //TODO: Add further FEN interpretation layers

        Piece[][] fenBoard = new Piece[size][size];

        String[] fens = fen.split("#");
        //Splits the FEN string into 3 layers - the basic board layout, the piece move counts, and the current turn
        char[] fen1Chars = fens[0].toCharArray();
        char[] fen2Chars = fens[1].toCharArray();
        char[] fen3Chars = fens[2].toCharArray();

        wPieces = new ArrList<>();
        bPieces = new ArrList<>();
        aPieces = new ArrList<>();
        //Either initializes or resets the piece lists for each side and both sides

        int x = 0;
        int y = tSize;
        //Starts at the top-left corner (0, tSize)

        int dataIndex = 0;

        if(DEBUG) System.out.println("Fen: " + fen);
        //[DEBUG TEXT] Print the FEN string

        for(char c : fen1Chars) {
            //For each char in the FEN string:

            Piece p = null;

            if(c >= '1' && c <= '9') x += (c - 49);
                //If c is between num 1 through 8, adds num - 1 to the x value
                //TODO: Remove hard-coded chars?
            else if(c == '/') {
                x = -1;
                //Goes back to the start of the rank
                y--;
                //Goes down a rank
            }
            else if(c == 'P') p = new Pawn(Color.WHITE, x, y);
            else if(c == 'R') p = new Rook(Color.WHITE, x, y);
            else if(c == 'N') p = new Knight(Color.WHITE, x, y);
            else if(c == 'B') p = new Bishop(Color.WHITE, x, y);
            else if(c == 'Q') p = new Queen(Color.WHITE, x, y);
            else if(c == 'K') p = new King(Color.WHITE, x, y);
            else if(c == 'E') p = new Earl(Color.WHITE, x, y);
            else if(c == 'M') p = new Monk(Color.WHITE, x, y);
            else if(c == 'p') p = new Pawn(Color.BLACK, x, y);
            else if(c == 'r') p = new Rook(Color.BLACK, x, y);
            else if(c == 'n') p = new Knight(Color.BLACK, x, y);
            else if(c == 'b') p = new Bishop(Color.BLACK, x, y);
            else if(c == 'q') p = new Queen(Color.BLACK, x, y);
            else if(c == 'k') p = new King(Color.BLACK, x, y);
            else if(c == 'e') p = new Earl(Color.BLACK, x, y);
            else if(c == 'm') p = new Monk(Color.BLACK, x, y);
            else throw new IllegalArgumentException("ERROR: Invalid FEN string");
            //If c is not a recognized character, throws an IllegalArgument

            if(p != null) {
                //If a new piece was created

                fenBoard[x][y] = p;
                //Sets the tile at the current position to the newly created piece
                aPieces.add(p);
                //Adds the piece to the ArrList of all pieces
                updatePiece(p, false);
                //Adds the piece to its respective color ArrList
                p.setMoveCount(fen2Chars[dataIndex] - '0');
                //Sets the piece's moveCount to the moveCount contained in the data string

            }

            x++;
            //Every time the current character has been checked, moves over a tile

        }

        if(fen3Chars[0] == 'W') turn = Color.WHITE;
        else if(fen3Chars[0] == 'B') turn = Color.BLACK;
        else throw new IllegalArgumentException("ERROR: Invalid FEN string");
        //Retrieves the current moving color

        return fenBoard;
        //Returns the new board with the pieces on it

    }

    private String generateFen() {
        //Uses the current board to generate a FEN String.

        StringBuilder fen = new StringBuilder();

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
                //If the tile contains a piece, add its respective char to the FEN string

            }

            if(y != 0) fen.append('/');
            //Add a / when going down a row, except on the first iteration

        }

        fen.append("#");
        //Add the delimiter to separate layer 1 and 2

        for(Piece p : aPieces) fen.append(p.getMoveCount());
        //For all pieces, add their moveCount to the second layer of the FEN string

        fen.append("#");
        //Add the delimiter to separate layer 2 and 3

        if(turn == Color.WHITE) fen.append("W");
        else fen.append("B");
        //Store the current moving color

        String fenStr = fen.toString();

        if(DEBUG) System.out.println("Generated FEN: " + fenStr);
        //[DEBUG TEXT] Prints the FEN string generated by the method

        return fenStr;

    }

    private Point findKing(Color color) {
        //Finds the King of a given color and returns its position on the board in terms of a Point.

        ArrList<Piece> pieces = getPieces(color);
        //Get the list of pieces for the passed color

        for(Piece p : pieces) {
            //Look through all pieces in the list

            if(p instanceof King) return p.getPos();
            //If the piece at the current tile is a King, return its coordinates

        }

        return null;
        //Should never happen as the King cannot be captured under standard game rules

    }

    public boolean findCheck(Move move) {
        //Look to see if the King of a given color will be in check at the end of a given move.

        forceMove(move);
        //Moves the piece to the end position

        ArrList<Piece> pieces;
        Point kingPos;

        if(turn == Color.WHITE) {
            pieces = bPieces;
            kingPos = wKingPos;
        } else {
            pieces = wPieces;
            kingPos = bKingPos;
        }
        //Stores the King position and piece array for the given color

        if(DEBUG) System.out.println("King is at [" + kingPos.x + ", " + kingPos.y + "]");
        //[DEBUG TEXT] Prints out where the King is

        for(Piece p : pieces) {

            if(p.isLegal(new Move(this, p.getPos(), kingPos, true))) {
                //If the color of this piece matches the color that was passed in and it is attacking the King

                if(DEBUG) {
                    //[DEBUG TEXT] Prints a statement indicating which piece is putting the King in check

                    if (turn == Color.WHITE) System.out.print("WHITE ");
                    else System.out.print("BLACK ");
                    System.out.print("King is currently being put IN CHECK by " + p.getString() + "\n");

                }

                state = BoardState.CHECK;
                //Sets the board state to CHECK if there is a check

                forceUndoMove(move);
                //Reverts the move

                return true;

            }

        }

        forceUndoMove(move);
        state = BoardState.NONE;
        //Sets the board state to NONE if there is no check

        return false;

    }

    public ArrList<Move> getLegalMoves(Color color) {
        //Creates a list of all legal moves on the current board for a given color.

        ArrList<Piece> checkedPieces = getPieces(color);
        //Creates a piece array representing either the white or black pieces,
        //depending on the color that is passed in

        ArrList<Move> legalMoves = new ArrList<>();

        for(Piece p : checkedPieces)

        for(int x = 0; x <= tSize; x++) {
            //Loop through ranks

            for(int y = 0; y <= tSize; y++) {
                //Loop through columns

                Move m = new Move(this, p.getPos(), x, y, true);

                if(p.isLegal(m) && !findCheck(m)) legalMoves.add(m);
                //If the move is legal for the piece and does not result in check
                //TODO: This is quite slow, need to optimize

            }

        }

        if(DEBUG) System.out.println("Legal moves: " + legalMoves.size());
        //[DEBUG TEXT] Prints the amount of legal moves available from this board

        return legalMoves;

    }

    public void doMove(Move move) {
        //Perform a move on the board when passed a move object.

        Piece p = move.getMovedPiece();
        Piece c = move.getCapturedPiece();

        if(p == null) throw new IllegalArgumentException("ERROR: No piece at starting position");
        //If there is no piece at the starting position, throws an Illegal Argument

        if(DEBUG) System.out.println("Moving piece: " + p.getString());
        //[DEBUG TEXT] Prints the string of the piece being moved

        if(!p.isLegal(move)) throw new IllegalArgumentException("ERROR: Illegal move");
        //If the move is illegal for this piece type, throws an IllegalArgument

        if(findCheck(move)) throw new IllegalArgumentException("ERROR: Move cannot result in self-check");
        //If the King is put in check by this move, throws an IllegalArgument
        //TODO: Move to isLegal or somewhere where it will be supported by a move sorting algorithm as a base legality check

        if(p instanceof Monk && c != null) {

            if(c instanceof King) throw new IllegalArgumentException("Error: Illegal move");

            Random rnd = new Random();

            if(rnd.nextInt(1) == 0) {

                updatePiece(c, true);
                c.switchColor();
                updatePiece(c, false);

            }

        } else forceMove(move);
        //Moves the piece to the end position

        if(p instanceof Pawn) {

            if((p.getColor() == Color.WHITE && move.getEndY() == 7) ||
                    (p.getColor() == Color.BLACK && move.getEndY() == 0)) {

                board[move.getEndX()][move.getEndY()] = getUser(turn).getPromotion(move);

                updatePiece(p, true);
                updatePiece(board[move.getEndX()][move.getEndY()], false);

            }

        }

        if(p instanceof Pawn || c != null) halfMove = 0;
        else halfMove++;
        //Increments halfMove by 1 if there is no piece capture or pawn move, otherwise set to 0

        moves++;
        //Increment the total number of moves

        if(turn == Color.WHITE) turn = Color.BLACK;
        else turn = Color.WHITE;
        //Change the current turn color

        findEndGame();
        //If the current side has no possible legal moves (is in checkmate)
        //TODO: Stalemate?

    }

    public void forceUndoMove(Move move) {
        //Undo the passed Move, disregarding rules.
        //TODO: Invert move and pass to forceMove

        Piece p = move.getMovedPiece();
        Piece c = move.getCapturedPiece();

        board[move.getStartX()][move.getStartY()] = p;
        //Set the tile at the start position to the moved piece
        board[move.getEndX()][move.getEndY()] = c;
        //Set the tile at the end position to the captured piece in the move

        p.setPos(move.getStartPos());
        //Reset the piece coordinates to its starting position
        p.incMoveCount(-1);
        //Subtract 1 from the moveCount of the moved piece
        updateKingPos(p);
        //Update the King's position on the board

        if(c != null) {
            //If there was a capture

            updatePiece(c, false);
            updateCaptured(c, true);

            //Add the captured piece to its respective piece lists and remove it from the capture list

        }

    }

    public void forceMove(Move move) {
        //Simply make the move in the passed Move, disregarding rules.

        Piece p = move.getMovedPiece();
        Piece c = move.getCapturedPiece();

        board[move.getEndX()][move.getEndY()] = p;
        //Set the tile at the end position to the moved piece
        board[move.getStartX()][move.getStartY()] = null;
        //Set the tile at the start position to null (empty tile)

        p.setPos(move.getEndPos());
        //Set the new coordinates for the piece

        p.incMoveCount(1);
        //Add 1 to the moveCount of the moved piece
        updateKingPos(p);
        //Update the King's position on the board

        if(c != null) {
            //If there is a capture

//            System.out.println(p.getString() + c.getString());

            updatePiece(c, true);
            updateCaptured(c, false);

            //Add the captured piece to its respective capture list and remove it from the piece lists

        }

    }

}