package chuss;

//This class handles the computer interaction with the game,
//specifically the process of sorting and selecting moves.

import chuss.Piece.Color;

import java.util.concurrent.TimeUnit;

public class Computer implements Interactable {

    //FIELDS

    private final boolean DEBUG = false;
    //Constant to determine whether [DEBUG TEXT] will be displayed for this class
    private final Board board;
    //The board that the computer is interacting with
    private final Color color;
    //What color this computer is playing
    private Move lastMove;
    private Move doubleLastMove;

    //CONSTRUCTORS

    public Computer(Board board, Color color) {
        //The constructor for the Computer object. Uses a move sorting
        //algorithm to generate a move that is passed back to
        //the main game.

        this.board = board;
        this.color = color;

    }

    //OTHER

    public Move getMove() {

        try {

            TimeUnit.SECONDS.sleep(1);

        } catch(InterruptedException e) {

            e.printStackTrace();

        }

        ArrList<Move> legalMoves = board.getLegalMoves(color);
        MinMaxer<Move> moveValues = new MinMaxer<>();

        for(Move m : legalMoves) moveValues.add(m, getMaterialValue(m) * 5 + getHeatmapValue(m) + getRepeatValue(m) * 10);

        Move chosenMove = moveValues.getMax();

        if(lastMove != null) doubleLastMove = lastMove;
        lastMove = chosenMove;

        return chosenMove;

    }

    public Piece getPromotion(Move move) {

        return new Queen(color, move.getEndX(), move.getEndY());

    }

    private int getMaterialValue(Move move) {
        //TODO: Convert to a field for quick access rather than recalculating value every time
        //TODO: Add comments

        Color col = move.getMovedPiece().getColor();

        board.forceMove(move);

        ArrList<Piece> friendlyPieces = board.getPieces(col);
        ArrList<Piece> enemyPieces = board.getPieces(col.getOther());

        int friendlyValue = 0;
        int enemyValue = 0;

        for(Piece p : friendlyPieces) friendlyValue += pieceValue(p);
        for(Piece p : enemyPieces) enemyValue += pieceValue(p);

        board.forceUndoMove(move);

        return friendlyValue - enemyValue;

    }

    private int getHeatmapValue(Move move) {

        Piece p = move.getMovedPiece();
        int[] tileHeatmap = new int[64];
        int tileValue = 0;

        if(p instanceof Pawn) {

            tileHeatmap = new int[] {

                     0,  0,  0,  0,  0,  0,  0,  0,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    10, 10, 20, 30, 30, 20, 10, 10,
                     5,  5, 10, 25, 25, 10,  5,  5,
                     0,  0,  0, 20, 20,  0,  0,  0,
                     5, -5,-10,  0,  0,-10, -5,  5,
                     5, 10, 10,-20,-20, 10, 10,  5,
                     0,  0,  0,  0,  0,  0,  0,  0

            };

        } else if(p instanceof Rook) {

            tileHeatmap = new int[] {

                     0,  0,  0,  0,  0,  0,  0,  0,
                     5, 10, 10, 10, 10, 10, 10,  5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                     0,  0,  0,  5,  5,  0,  0,  0

            };

        } else if(p instanceof Knight) {

            tileHeatmap = new int[] {
                    //TODO: Double-check the top row

                    -50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -30,  5, 15, 20, 20, 15,  5,-30,
                    -30,  0, 15, 20, 20, 15,  0,-30,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -50,-40,-30,-30,-30,-30,-40,-50

            };

        } else if(p instanceof Bishop) {

            tileHeatmap = new int[] {

                    -20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -10,  5,  5, 10, 10,  5,  5,-10,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -20,-10,-10,-10,-10,-10,-10,-20

            };

        } else if(p instanceof Queen) {

            tileHeatmap = new int[] {

                    -20,-10,-10, -5, -5,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5,  5,  5,  5,  0,-10,
                    -5,   0,  5,  5,  5,  5,  0, -5,
                     0,   0,  5,  5,  5,  5,  0, -5,
                    -10,  5,  5,  5,  5,  5,  0,-10,
                    -10,  0,  5,  0,  0,  0,  0,-10,
                    -20,-10,-10, -5, -5,-10,-10,-20

            };

        } else if(p instanceof King) {

            if (board.pieceCount(color) > 7) {

                tileHeatmap = new int[]{

                        -30, -40, -40, -50, -50, -40, -40, -30,
                        -30, -40, -40, -50, -50, -40, -40, -30,
                        -30, -40, -40, -50, -50, -40, -40, -30,
                        -30, -40, -40, -50, -50, -40, -40, -30,
                        -20, -30, -30, -40, -40, -30, -30, -20,
                        -10, -20, -20, -20, -20, -20, -20, -10,
                        20, 20, 0, 0, 0, 0, 20, 20,
                        20, 30, 10, 0, 0, 10, 30, 20

                };

            } else {

                tileHeatmap = new int[]{

                        -50, -40, -30, -20, -20, -30, -40, -50,
                        -30, -20, -10, 0, 0, -10, -20, -30,
                        -30, -10, 20, 30, 30, 20, -10, -30,
                        -30, -10, 30, 40, 40, 30, -10, -30,
                        -30, -10, 30, 40, 40, 30, -10, -30,
                        -30, -10, 20, 30, 30, 20, -10, -30,
                        -30, -30, 0, 0, 0, 0, -30, -30,
                        -50, -30, -30, -30, -30, -30, -30, -50

                };

            }

        } else if(p instanceof Earl) {

            tileHeatmap = new int[]{

                    -20, -10, -10, -5, -5, -10, -10, -20,
                    -10, 0, 0, 0, 0, 0, 0, -10,
                    -10, 0, 5, 5, 5, 5, 0, -10,
                    -5, 0, 5, 5, 5, 5, 0, -5,
                    0, 0, 5, 5, 5, 5, 0, -5,
                    -10, 5, 5, 5, 5, 5, 0, -10,
                    -10, 0, 5, 0, 0, 0, 0, -10,
                    -20, -10, -10, -5, -5, -10, -10, -20

            };

        }

        if(p.getColor() == Color.WHITE) {

            int i = 8 * (7 - move.getEndY()) + move.getEndX();
            tileValue = tileHeatmap[i];

        } else {

            int i = 8 * move.getEndY() + (7 - move.getEndX());
            tileValue = tileHeatmap[i];

        }

        return tileValue;

    }

    private int getRepeatValue(Move move) {

        int repeatValue = 0;

        try {

            Piece curP = move.getMovedPiece();
            Piece lastP = lastMove.getMovedPiece();
            Piece dLastP = doubleLastMove.getMovedPiece();

            if (curP == lastP) repeatValue -= 15;
            if (curP == dLastP && doubleLastMove.getStartPos() == move.getStartPos() &&
                    doubleLastMove.getEndPos() == move.getEndPos()) repeatValue -= 50;

        } catch(NullPointerException ignored) {}

        return repeatValue;

    }

    private int pieceValue(Piece piece) {
        //TODO: Add comments

        if(piece instanceof Pawn) return 1;
        else if(piece instanceof Rook) return 5;
        else if(piece instanceof Knight) return 3;
        else if(piece instanceof Bishop) return 3;
        else if(piece instanceof Queen) return 9;
        else return 0;

    }

}