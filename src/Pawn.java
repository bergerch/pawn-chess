package pawns_chess.model;

/**
 * The class Pawn models a chess pawn as pair of coordinates.
 */
public class Pawn implements Cloneable {

    private int column;
    private int row;

    /**
     * Generates a new pawn
     * 
     * @param column
     *            x-coodinate
     * @param row
     *            y-coordinate
     */
    public Pawn(int column, int row) {
        super();
        this.column = column;
        this.row = row;
    }

    /**
     * Gets the colum og the pawn
     * 
     * @return x-coordinnate
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column of the pawn
     * 
     * @param column
     *            x-coordinate
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Gets the row of the pawn
     * 
     * @return y-coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of the pawn
     * 
     * @param row
     *            y-coordinate
     */
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pawn other = (Pawn) obj;
        if (column != other.column) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }

    @Override
    public Pawn clone() {
        return new Pawn(this.column, this.row);
    }

    /**
     * Is this Pawn next to another Pawn q ?
     * 
     * @param q
     *            the other pawn
     * @return if the pawn this has x and y coordinate distances of <= 1 to q
     */
    public boolean isNextTo(Pawn q) {
        if (this.equals(q)) {
            return false;
        } else {
            return 
            (Math.abs(column - q.column) <= 1 && Math.abs(row - q.row) <= 1);
        }
    }

}
