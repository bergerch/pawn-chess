package pawns_chess.model;

/**
 * Interface for a Pawns Chess game, a lite variant of chess. The only tiles are
 * pawns. Winner is who first reaches the opponent's ground line and gains a
 * queen, or in case of a draw who owns more remaining pawns.
 * 
 * <p>
 * There are some differences to traditional chess:
 * <ul>
 * <li>In case that one player has no option to make a valid move, he must miss
 * a turn. If both players subsequently must miss a turn, the game ends in draw.
 * <li>There is no en passant capture rule.
 * </ul>
 * 
 * <p>
 * A human plays against the machine. The human's ground line is always row 1,
 * whereas the ground line of the machine is row 7. The human plays from bottom
 * to top, the machine from top to bottom. The user with the white tiles opens
 * the game.
 */
public interface Board extends Cloneable {

    /**
     * The number of columns (files) and rows (ranks) of the game grid.
     * Originally 8. Here, at least 4.
     */
    int SIZE = 8;

    /**
     * Gets the player who should or already has opened the game. As an
     * invariant, this player has the white tiles.
     * 
     * @return The player who makes the initial move.
     */
    Player getFirstPlayer();

    /**
     * Gets the player who is allowed to execute the next move.
     * 
     * @return The player who shall make the next move.
     */
    Player next();

    /**
     * Executes a human move.
     * 
     * @param colFrom
     *            The slot's column number from which the tile of the human
     *            player should be moved.
     * @param rowFrom
     *            The slot's row number from which the tile of the human player
     *            should be moved.
     * @param colTo
     *            The slot's column number to which the tile of the human player
     *            should be moved.
     * @param rowTo
     *            The slot's row number to which the tile of the human player
     *            should be moved.
     * @return {@code true} if and only if the move was successful, e.g., if the
     *         move is a legal capture or a move forward to a free slot.
     * @throws IllegalMoveException
     *             If the game is already over, or it is not the human's turn.
     * @throws IllegalArgumentException
     *             If the provided parameters are invalid, e.g., one of the
     *             defined slots outside the grid.
     */
    boolean move(int colFrom, int rowFrom, int colTo, int rowTo);

    /**
     * Executes a machine move.
     * 
     * @throws IllegalMoveException
     *             If the game is already over, or it is not the machine's turn.
     */
    void machineMove();

    /**
     * Sets the skill level of the machine.
     * 
     * @param level
     *            The skill as a number, must be at least 1.
     */
    void setLevel(int level);

    /**
     * Checks if the game is over. Either one player has won or there is a draw,
     * i.e., no player can perform any further move.
     * 
     * @return {@code true} if and only if the game is over.
     */
    boolean gameOver();

    /**
     * Checks if the game state is won.
     * 
     * @return The winner or nobody in case of a draw.
     */
    Player getWinner();

    /**
     * Gets the content of the slot at the specified coordinates. Either it
     * contains a tile of one of the two players already or it is empty.
     * 
     * @param row
     *            The row of the slot in the game grid.
     * @param col
     *            The column of the slot in the game grid.
     * @return The slot's content.
     */
    Player getSlot(int col, int row);

    /**
     * Deep-copys the board.
     * 
     * @return A clone.
     */
    Board clone();

    /**
     * Gets the string representation of this board as row x column matrix. Each
     * slot is represented by one the three chars ' ', 'W', or 'B'. ' ' means
     * that the slot currently contains no tile. 'W' means that it contains a
     * white tile. 'B' means that it contains a black tile. In contrast to the
     * rows, the columns are whitespace separated.
     * 
     * @return The string representation of the current Pawns Chess game.
     */
    @Override
    String toString();

}