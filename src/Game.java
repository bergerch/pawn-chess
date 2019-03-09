package pawns_chess.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The class Game models the chess field: There are two players, a difficulty 
 * level and the pawns of each players are saved in a list. This class provides
 * all methods of the Board interface and some others e.g. the score methods 
 * to evaluate the game.
 */
public class Game implements Board {

    private Player first;
    private Player current;
    private int level;
    private List<Pawn> human;
    private List<Pawn> machine;

    /**
     * Initialises the Game
     * 
     * @param first
     *            Which player shall start the game?
     */
    public Game(Player first) {
        super();
        level = 3;
        human = new LinkedList<Pawn>();
        machine = new LinkedList<Pawn>();
        for (int i = 1; i <= SIZE; i++) {
            human.add(new Pawn(i, 1));
            machine.add(new Pawn(9 - i, SIZE));
        }
        this.first = first;
        current = first;
        if (first == Player.MACHINE) {
            Player.MACHINE.setColor("White");
            Player.HUMAN.setColor("Black");
            machineMove();
        }
        if (first == Player.HUMAN) {
            Player.MACHINE.setColor("Black");
            Player.HUMAN.setColor("White");
        }

    }

    /**
     * Clone Constructor
     * 
     * @param first
     *            copy of the first player
     * @param current
     *            copy of the current player
     * @param level
     *            must be 1-4
     * @param human
     *            List of human pawns
     * @param machine
     *            List of machine pawns
     */
    private Game(Player first, Player current, int level, List<Pawn> human,
            List<Pawn> machine) {
        super();
        this.first = first;
        this.current = current;
        this.level = level;
        this.human = human;
        this.machine = machine;
    }

    @Override
    public Player getFirstPlayer() {
        return first;
    }

    @Override
    public Player next() {

        return current;
    }

    /**
     * Sets the next Player.
     */
    public void setNext() {
        current = checkNext();
    }

    /**
     * Decides which player's turn it is
     * 
     * @return the next player if he can move
     */
    private Player checkNext() {
        Player next = current.equals(Player.HUMAN) ? Player.MACHINE
                : Player.HUMAN;

        if (!canMove(next)) {
            next = next.equals(Player.HUMAN) ? Player.MACHINE : Player.HUMAN;
        }
        if (!canMove(next)) {
            next = Player.NONE;
        }

        return next;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Allows access to the List with the pawns, depending on the player
     * 
     * @param player
     *            Who posses the pawns
     * @return machine pawns if player is the machine, humans pawns if the
     *         player is human
     */
    public List<Pawn> getPawns(Player player) {
        if (player == Player.HUMAN) {
            return human;
        }
        if (player == Player.MACHINE) {
            return machine;
        }
        return new LinkedList<Pawn>();
    }

    @Override
    public boolean move(int colFrom, int rowFrom, int colTo, int rowTo) {
        
        if (gameOver() || current != Player.HUMAN) {
            throw new IllegalMoveException();
        }
        
        if (colFrom < 1 || colFrom > SIZE || rowFrom < 1 || rowFrom > SIZE 
            || colTo < 1 || colTo > SIZE || rowTo < 1 || rowTo > SIZE) {
                throw new IllegalArgumentException();
        }
        
        Pawn target = new Pawn(colTo, rowTo);
        Pawn source = new Pawn(colFrom, rowFrom);
        boolean legalMove = false;

        // There is no Pawn with the respective coordinates
        if (!human.contains(source)) {
            return legalMove;
        }
        // One Step forward
        if (colFrom == colTo && rowFrom + 1 == rowTo && !human.contains(target)
                && !machine.contains(target)) {
            legalMove = true;
        }
        // Two Steps forward
        if (colFrom == colTo && rowFrom + 2 == rowTo && rowFrom == 1
                && !human.contains(target) && !machine.contains(target)) {
            legalMove = true;
        }
        // Strike right | left
        if ((colFrom + 1 == colTo || colFrom - 1 == colTo)
                && rowFrom + 1 == rowTo && !human.contains(target)
                && machine.contains(target)) {
            machine.remove(target);
            legalMove = true;
        }
        // if the move was okay, alter the coords of the Pawn in the list
        if (legalMove) {
            for (Pawn q : human) {
                if (q.equals(source)) {
                    q.setColumn(colTo);
                    q.setRow(rowTo);
                }
            }

            current = checkNext();
        }

        return legalMove;
    }

    @Override
    public void machineMove() {
        
        if (gameOver() || current != Player.MACHINE) {
            throw new IllegalMoveException();
        }
        
        // Initialize the gametree
        GameTree gt = new GameTree(this.clone(), level);
        Game draw = gt.selectBestStrategy();
        machine = draw.machine;
        human = draw.human;
        current = checkNext();
    }

    @Override
    public Player getSlot(int col, int row) {
        Pawn p = new Pawn(col, row);
        if (human.contains(p)) {
            return Player.HUMAN;
        }
        if (machine.contains(p)) {
            return Player.MACHINE;
        }
        return Player.NONE;
    }

    @Override
    public Game clone() {
        List<Pawn> human2 = new LinkedList<Pawn>();
        List<Pawn> machine2 = new LinkedList<Pawn>();
        for (Pawn p : human) {

            human2.add(p.clone());
        }
        for (Pawn p : machine) {

            machine2.add(p.clone());
        }
        Game copy = new Game(this.first, this.current, this.level, human2,
                machine2);
        return copy;
    }

    @Override
    public String toString() {
        String res = "";
        char humanSymbol = Player.HUMAN.getColor().charAt(0);
        char machineSymbol = Player.MACHINE.getColor().charAt(0);
        for (int y = SIZE; y >= 1; y--) {
            for (int x = 1; x <= SIZE; x++) {
                if (human.contains(new Pawn(x, y))) {
                    res += humanSymbol;
                } else {
                    if (machine.contains(new Pawn(x, y))) {
                        res += machineSymbol;
                    } else {
                        res += " ";
                    }
                }
                if (x < SIZE) {
                    res += " ";
                }
            }
            if (y > 1) {
                res += "\n";
            }
        }
        return res;
    }

    @Override
    public boolean gameOver() {
        Player next = current.equals(Player.HUMAN) ? Player.MACHINE
                : Player.HUMAN;
        if (!canMove(current) && !canMove(next)) {
            return true;
        }
        if (human.isEmpty() || machine.isEmpty()) {
            return true;
        }
        for (Pawn p : human) {
            if (p.getRow() == SIZE) {
                return true;
            }
        }
        for (Pawn p : machine) {
            if (p.getRow() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Player getWinner() {
        if (human.isEmpty()) {
            return Player.MACHINE;
        }
        if (machine.isEmpty()) {
            return Player.HUMAN;
        }
        for (Pawn h : human) {
            if (h.getRow() == SIZE) {
                return Player.HUMAN;
            }
        }
        for (Pawn m : machine) {
            if (m.getRow() == 1) {
                return Player.MACHINE;
            }
        }
        return Player.NONE;

    }

    /**
     * @param i
     *            depth in the gametree
     * 
     * @return score of this game
     */
    public double getScore(int i) {
        return scoreN() + scoreD() + scoreC() + scoreI() + scoreV(i);
    }

    /**
     * Computes the number score: A high number of living machine pawns is good,
     * whereas a high number of living human pawns is bad.
     * 
     * @return number score
     */
    private double scoreN() {
        return machine.size() - (double) human.size() * 1.5;
    }

    /**
     * Computes the distance score: A high Distance to the start line is 
     * considered good.
     * 
     * @return distance score
     */
    private double scoreD() {
        double scoreDH = 0;
        double scoreDM = 0;
        for (Pawn h : human) {
            scoreDH = scoreDH + h.getRow() - 1;
        }
        for (Pawn m : machine) {
            scoreDM = scoreDM + SIZE - m.getRow();
        }
        return scoreDM - scoreDH * 1.5;
    }

    /**
     * Computes the scoreC: pawns that are threatened by the opponent and are
     * not protected are considered bad.
     * 
     * @return score of threatened pawns
     */
    private double scoreC() {
        double scoreCH = 0;
        double scoreCM = 0;
        for (Pawn h : human) {
            boolean inDanger = false;
            for (Pawn m : machine) {
                if (canBeat(m, h)) {
                    inDanger = true;
                }
            }
            for (Pawn p : human) {
                if (canBeat(p, h)) {
                    inDanger = false;
                }
            }
            if (inDanger) {
                scoreCH++;
            }
        }
        for (Pawn m : machine) {
            boolean inDanger = false;
            for (Pawn h : human) {
                if (canBeat(h, m)) {
                    inDanger = true;
                }
            }
            for (Pawn p : machine) {
                if (canBeat(p, m)) {
                    inDanger = false;
                }
            }
            if (inDanger) {
                scoreCM++;
            }
        }
        return scoreCH - 3 * scoreCM / 2;
    }

    /**
     * Computes the isolation score: isolated pawns are considered bad
     * 
     * @return isolation score
     */
    private double scoreI() {
        double scoreIH = 0;
        double scoreIM = 0;

        for (Pawn h : human) {
            boolean isIsolated = true;
            for (Pawn p : human) {
                if (h.isNextTo(p)) {
                    isIsolated = false;
                }
            }
            if (isIsolated) {
                scoreIH++;
            }
        }

        for (Pawn m : machine) {
            boolean isIsolated = true;
            for (Pawn p : machine) {
                if (m.isNextTo(p)) {
                    isIsolated = false;
                }
            }
            if (isIsolated) {
                scoreIM++;
            }
        }
        return scoreIH - 3 * scoreIM / 2;
    }

    /**
     * Computes the victory score: if a move leads to victoy it is a very good
     * move
     * 
     * @param i
     *            depth in the gametree
     * @return victory score
     */
    private double scoreV(int i) {
        double scoreVH = 0;
        double scoreVM = 0;

        if (getWinner() == Player.HUMAN) {
            scoreVH = 5000 / i;
        }
        if (getWinner() == Player.MACHINE) {
            scoreVM = 5000 / i;
        }
        return scoreVM - 3 * scoreVH / 2;

    }

    /**
     * Returns if Pawn p can beat Pawn q or Not. Or: if Pawn q is protected by
     * Pawn p
     * 
     * @param p 
     * @param q
     * @return
     */
    private boolean canBeat(Pawn p, Pawn q) {

        int one = human.contains(p) ? 1 : -1;
        return (p.getRow() + one == q.getRow()
                && (p.getColumn() + 1 == q.getColumn() || p.getColumn() - 1 == q
                        .getColumn()));
    }

    /**
     * Can a certain player make a move?
     * @param player the specific player
     * @return true if player can move
     */
    private boolean canMove(Player player) {
        boolean res = false;
        if (player.equals(Player.MACHINE)) {
            for (Pawn p : machine) {
                if (canMove(p, player)) {
                    res = true;
                }
            }
        }
        if (player.equals(Player.HUMAN)) {
            for (Pawn p : human) {
                if (canMove(p, player)) {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * Can a certain player make a move with a specific pawn?
     * @param source the specific pawn
     * @param player the specific player
     * @return true if the player can move the pawn
     */
    private boolean canMove(Pawn source, Player player) {
        boolean legalMove = false;
        int step = human.contains(source) ? 1 : -1;
        int border = human.contains(source) ? 1 : Board.SIZE;
        Player other = player == Player.HUMAN ? Player.MACHINE : Player.HUMAN;

        Pawn target1 = new Pawn(source.getColumn(), source.getRow() + step);
        Pawn target2 = new Pawn(source.getColumn(), source.getRow() + 2 * step);
        Pawn target3 = new Pawn(source.getColumn() - step, source.getRow()
                + step);
        Pawn target4 = new Pawn(source.getColumn() + step, source.getRow()
                + step);

        // There is no Pawn with the respective coordinates
        if (!getPawns(getSlot(source.getColumn(), source.getRow())).contains(
                source)) {
            return legalMove;
        }
        // One Step forward
        if (source.getRow() + step <= SIZE && source.getRow() + step >= 1
                && !machine.contains(target1) && !human.contains(target1)) {
            legalMove = true;
        }
        // Two Steps forward
        if (source.getRow() == border && !machine.contains(target2)
                && !human.contains(target2)) {
            legalMove = true;
        }
        // Strike right
        if (!getPawns(player).contains(target3)
                && getPawns(other).contains(target3)) {
            legalMove = true;
        }
        // Strike left
        if (!getPawns(player).contains(target4)
                && getPawns(other).contains(target4)) {
            legalMove = true;
        }
        return legalMove;
    }
}
