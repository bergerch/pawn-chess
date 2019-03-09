package pawns_chess.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Arrays;

/**
 * The class Shell is responsible for handling the input and output and
 * therefore for the interaction with the user. Another task is to print out
 * error messages.
 */
public final class Shell {

    private static Board game;
    private static boolean gameinitialized;

    /**
     * not used
     */
    public Shell() {
    }

    /**
     * The main method initialises a new Buffered Reader and the list with the
     * instructions and invokes execute()
     * 
     * @param argv arguments for the start: not used
     * @throws IOException if something went wrong
     */
    public static void main(String[] argv) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(
                System.in));

        // The Set "initializedInstructions contains all instructions, which
        // can only be executed when the game is initialized
        Set<String> initializedInstructions = new LinkedHashSet<String>();
        String[] intialisedNecessaryCmd = {"LEVEL", "L", "SWITCH", "S",
                "PRINT", "P", "MOVE", "M", };
        initializedInstructions.addAll(Arrays.asList(intialisedNecessaryCmd));
        execute(stdin, initializedInstructions);
    }

    /**
     * This method handles the interaction with the user
     * 
     * @param stdin the created Buffered Reader
     * @param initializedInstructions a set with the insturctions that can only 
     *          be executed when the game is initialized
     * @throws IOException if something went wrong
     */
    private static void execute(BufferedReader stdin,
            Set<String> initializedInstructions) throws IOException {
        boolean quit = false;
        gameinitialized = false;
        while (!quit) {
            System.out.print("pc> ");
            // Read in line, check if input is empty?
            String input = stdin.readLine();
            if (input == null) {
                break;
            }
            // Split input by white spaces, command is the first input word
            String[] words = input.trim().split("\\s+");
            String command = words[0].toUpperCase();

            // At first check commands that do not need an initialized game
            switch (command) {
            case "":
                break;

            case "NEW":
            case "N":
                gameinitialized = newGame();
                break;

            case "HELP":
            case "H":
                help();
                break;

            case "QUIT":
            case "Q":
                quit = true;
                break;

            default:
                if (!initializedInstructions.contains(command)) {
                    errorMsg("Unknown command: " + command);
                    System.out.println("Type HELP for help");
                }
            }

            // game not initialized, but the command requires that
            if (!gameinitialized && initializedInstructions.contains(command)) {
                errorMsg("Game not initialized");
            }

            // game initialized and the command requires that
            if (gameinitialized && initializedInstructions.contains(command)) {
                switch (command) {
                case "LEVEL":
                case "L":
                    setLevel(words);
                    break;

                case "SWITCH":
                case "S":
                    switchPlayer();
                    break;

                case "MOVE":
                case "M":
                    move(words);
                    break;

                case "PRINT":
                case "P":
                    System.out.println(game);
                    break;

                default:
                    break;
                }
            }
        }
    }

    /**
     * Sets the level of the game
     * 
     * @param words
     *            command parameters
     */
    private static void setLevel(String[] words) {
        if (words.length == 2) {
            try {
                // String -> Int
                int level = Integer.parseInt(words[1]);
                if (level < 1 || level > 4) {
                    errorMsg("Invalid level. level must be 1-4");
                } else {
                    game.setLevel(level);
                }

            } catch (NumberFormatException e) {
                // Not an Int, Parsing failed
                errorMsg("At least one of the parameters"
                        + " is not of the type INT");
            }
        } else {
            errorMsg("Invalid number of arguments");
        }
    }

    /**
     * Initializes an new Game
     * 
     * @return true if game successfully initialized
     */
    private static boolean newGame() {
        if (gameinitialized) {
            game = new Game(game.getFirstPlayer());
        } else {
            game = new Game(Player.HUMAN);
        }
        System.out.println("New game started. You are "
                + Player.HUMAN.getColor().toLowerCase() + ".");
        return true;
    }

    /**
     * Prints out an error message
     * 
     * @param message
     *            error details
     */
    private static void errorMsg(String message) {

        System.out.println("Error! " + message);
    }

    /**
     * Moves a pawn on the game field
     * 
     * @param words
     *            command parameters
     */
    private static void move(String[] words) {
        if (game.gameOver()) {
            System.out.println("The game is over. You must start a new game.");
        } else {

            int[] coord = new int[4];
            if (5 == words.length) {
                // Watch out for a NumberFormatException
                try {
                    // String -> Int
                    for (int i = 0; i < 4; i++) {
                        coord[i] = Integer.parseInt(words[i + 1]);
                    }
                } catch (NumberFormatException e) {
                    // Not an Int, Parsing failed
                    errorMsg("At least one of the parameters"
                            + " is not of the type INT ");

                }
                for (int i = 0; i < 4; i++) {
                    // Number is not a coordinate on the game field
                    if (coord[i] <= 0 || coord[i] > Board.SIZE) {
                        errorMsg("Invalid Number (1,...," + Board.SIZE
                                + ") expected.");
                        return;
                    }
                        
                }
                // If move was successful, check who's next
                if (game.move(coord[0], coord[1], coord[2], coord[3])) {
                    if (game.next() == Player.MACHINE) {
                        machine();
                    } else {
                        System.out.println("Machine must miss a turn.");
                    }

                } else {
                    // Pawn cannot move to this place
                    if (game.getSlot(coord[0], coord[1]) == Player.HUMAN) {
                        errorMsg("Your Pawn (" + coord[0] + "," + coord[1]
                                + ") cannot" + " move to (" + coord[2] + ","
                                + coord[3] + ")");
                    } else {
                        // Specific pawn doesn't even exist
                        errorMsg("You don't have a Pawn at (" + coord[0] + ","
                                + coord[1] + ")");
                    }
                }
            } else {
                // Not the expected number of arguments
                errorMsg("Wrong number of parameters");

            }

            checkGameOver();
        }
    }

    /**
     * Executes a machine move as long as it is the machine's turn
     */
    private static void machine() {
        if (game.next() == Player.MACHINE && !game.gameOver()) {
            game.machineMove();
            while (game.next() == Player.MACHINE && !game.gameOver()) {
                game.machineMove();
                System.out.println("You must miss a turn.");
            }
        }
    }

    /**
     * Checks if the Game is over. If so prints out a appropriate message.
     */
    private static void checkGameOver() {
        if (game.gameOver()) {
            if (game.getWinner() == Player.HUMAN) {
                System.out.println("Congratulations! You won.");
            }
            if (game.getWinner() == Player.MACHINE) {
                System.out.println("Sorry! Machine wins.");
            }
            if (game.getWinner() == Player.NONE) {
                System.out.println("Nobody wins. Draw.");
            }
        }
    }

    /**
     * Switches the Player and start a new Game: The one that began the last
     * time is now the second player. Also switches the Color, because white
     * always starts.
     */
    private static void switchPlayer() {
        if (game.getFirstPlayer() == Player.HUMAN) {
            game = new Game(Player.MACHINE);
        } else {
            game = new Game(Player.HUMAN);
        }
        System.out.println("New game started. You are "
                + Player.HUMAN.getColor().toLowerCase() + ".");
    }

    /**
     * Prints out helpful information about the available commands
     */
    private static void help() {
        System.out.println("Welcome to Pawn Chess. Use these commands to play");
        System.out.println("NEW Generates a new Game");
        System.out.println("LEVEL x  Sets the level. x must be  1,2,3 or 4");
        System.out.println("MOVE sourceX sourceY aimX aimY Moves the player's"
                + " pawn from (sourceX,sourceY) to (aimX, aimY)");
        System.out
                .println("SWITCH Starts a new game and the player who started"
                        + " the last game is now the second");
        System.out.println("PRINT Prints out the current field");
        System.out.println("QUIT Exits the programm");
    }

}
