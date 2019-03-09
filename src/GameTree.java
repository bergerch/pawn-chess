package pawns_chess.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The class GameTree represents the following tree structure: Each GameTree has
 * a game, which represents the actual game state depending on the move that was
 * executed. It also has a List of GameTree with as its children in it (all
 * possible Moves the current Player can make) and the level and the difficulty
 * level.
 */
public class GameTree {

    /**
     * all possible Moves the current Player can make
     */
    private LinkedList<GameTree> children;
    
    /**
     * encapsulates the game state
     */
    private Game game;
    
    /**
     * the level of this children element in the actual gametree of the
     * original game. NOT the difficulty level
     */
    private int level;
    
    /**
     * the difficulty level
     */
    private static int maxlevel;

    /**
     * Second constructor. Used for Initialization.
     * @param game current game state
     * @param max difficulty level
     */
    public GameTree(Game game, int max) {
        this.game = game;
        maxlevel = max;
        children = new LinkedList<GameTree>();
        this.possibleMoves();
    }

    /**
     * First Constructor. Used for recursion.
     * @param game current game
     * @param level level of this gametree  (NOT the difficulty level)
     * @param max the difficulty level
     */
    private GameTree(Game game, int level, int max) {
        this.game = game;
        this.level = level;
        maxlevel = max;
        children = new LinkedList<GameTree>();
        if (level < maxlevel) {
            this.possibleMoves();
        }
    }
    
    /**
     * Gets the current game of this level
     * @return the current game state
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the children of the game: all possible moves of the current player
     * @return children of
     */
    public LinkedList<GameTree> getChilrden() {
        return children;
    }

    /**
     * Selects the best Strategy the machine can make.
     * @return the best move
     */
    public Game selectBestStrategy() {
        if (!children.isEmpty()) {
    
            GameTree res = children.get(0);
            for (GameTree gt : children) {
                if (gt.getScore() > res.getScore()) {
                    res = gt;
                }
            }
            return res.getGame();
        } else {
            System.out.println("empty");
            return game;
        }
    }

    /**
     * Gets the Score of any gametree element
     * @return score
     */
    public double getScore() {
        if (children.isEmpty() || game.gameOver()) {
            return game.getScore(level);
        } else {
            Player next = game.next();
            return (next == Player.MACHINE) ? max() : min();
        }
        
    }

    /**
     * Gets the maximal Score of the children in the gametree
     * @return max score
     */
    private double max() {
        double x = Double.NEGATIVE_INFINITY;
        for (GameTree g : children) {
            double gamescore = g.getScore();
            if (gamescore > x) {
                x = gamescore;
            }
        }
        return game.getScore(level) + x;
    }

    /**
     * Gets the minimal Score of the children in the gametree
     * @return min score
     */
    private double min() {
        double x = Double.POSITIVE_INFINITY;
        for (GameTree g : children) {
            double gamescore = g.getScore();
            if (gamescore < x) {
                x = gamescore;
            }
        }
        return game.getScore(level) + x;
    }

    /**
     * Creates all possible moves a player can make and adds them as children
     * to the gametree. This is used to initialize the children using recursion
     */
    private void possibleMoves() {
        List<Game> allPossibilities = new LinkedList<Game>();
        for (Pawn p : game.getPawns(game.next())) {
            allPossibilities.addAll(possibleMoves(p, game.next()));
        }
        for (Game b : allPossibilities) {
            GameTree gt = new GameTree(b, this.level + 1, maxlevel);
            children.add(gt);
        }
    }

    /**
     * Creates all (max 4) possible moves a player can make with one pawn and
     * adds  them as children to the gametree.
     * This is used to initialize the children using recursion
     * @param p the specific pawn
     * @param current the current player
     * @return all possible moves
     */
    private List<Game> possibleMoves(Pawn p, Player current) {
        int x = p.getColumn();
        int y = p.getRow();
        // To save all the possible moves / game states
        List<Game> moves = new LinkedList<Game>();
        Player other = current == Player.HUMAN ? Player.MACHINE : Player.HUMAN;
        assert (game.next() == current) : "game.next() unequal to current";
        int step = current == Player.HUMAN ? 1 : -1;
        int border = current == Player.HUMAN ? 1 : Board.SIZE;

        // Strike Left
        if (game.getSlot(x - step, y + step).equals(other)) {
            Game g = game.clone();
            g.setNext();
            for (Pawn h : g.getPawns(current)) {
                if (p.equals(h)) {
                    h.setColumn(x - step);
                    h.setRow(y + step);
                    g.getPawns(other).remove(h);
                    moves.add(g);
                }
            }
        } // Step forward
        if (y + step <= Board.SIZE && y + step >= 1
                && game.getSlot(x, y + step).equals(Player.NONE)) {
            Game g = game.clone();
            g.setNext();
            for (Pawn h : g.getPawns(current)) {
                if (p.equals(h)) {
                    h.setRow(y + step);
                    moves.add(g);
                }
            }
        } // 2 Steps forward
        if (y == border && game.getSlot(x, y + 2 * step).equals(Player.NONE)) {
            Game g = game.clone();
            g.setNext();
            for (Pawn h : g.getPawns(current)) {
                if (p.equals(h)) {
                    h.setRow(y + 2 * step);
                    moves.add(g);
                }
            }
        } // Strike right
        if (game.getSlot(x + step, y + step).equals(other)) {
            Game g = game.clone();
            g.setNext();
            for (Pawn h : g.getPawns(current)) {
                if (p.equals(h)) {
                    h.setColumn(x + step);
                    h.setRow(y + step);
                    g.getPawns(other).remove(h);
                    moves.add(g);
                }
            }
        }
        return moves;
    }

}
