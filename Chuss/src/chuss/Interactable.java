package chuss;

//This interface provides a common reference point for the code to
//interact with both the player and the computer interchangeably.

public interface Interactable {

    public Move getMove();

    public Piece getPromotion(Move move);

}