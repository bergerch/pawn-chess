package pawns_chess.model;

/**
 *The enum Player models a player a either  HUMAN or MACHINE. The third
possibility NONE is a dummy value. A Player has a color.
 *
 */
public enum Player {

    /**
     * The human player
     */
    HUMAN("White"), 
    
    /**
     * the bot
     */
    MACHINE("Black"), 
    
    /**
     * Dummy object
     */
    NONE("Nothing");

    private String color;

    /**
     * Each Player has a color
     * @param color white or black
     */
    private Player(String color) {
        this.color = color;
    }

    /**
     * Gets the color of a Player
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of a Player
     * @param color Must be "Black" or "White"
     */
    public void setColor(String color) {
        this.color = color;
    }

}
