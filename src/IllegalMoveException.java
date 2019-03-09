package pawns_chess.model;

/**
 * This kind of runtime exception occurs, if a move is illegal (e.g. the game
 * is already over, the player who moves is not the current player) and can
 * not be treaten reasonable
 */
public class IllegalMoveException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * default constructor
     */
    public IllegalMoveException() {
        super();
    }

    /**
     * constructor with message
     * 
     * @param arg0 message
     */
    public IllegalMoveException(String arg0) {
        super(arg0);
    }

}
