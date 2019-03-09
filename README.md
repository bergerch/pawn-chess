# Pawn Chess


## Getting Started

Just compile the sources and type in the CLI:

```java -jar PawnChess.jar```


## Use

All inputs can be applied using a command line interface:

*NEW: creates new game
*HEP: prints out help information
*QUIT: quits the game
*SWITCH: switches starting player
*LEVEL i: sets difficulty to level i
*MOVE fromX fromxY toX toY: moves a coordinate of a pawn
*PRINT: Prints the board to console

```javascript
pc> m 2 1 2 2
pc> p
B B B B B B

            B B


W
  W
    W W W W W W
```

## Development

The programm is written in Java. All sources are included. We use and recommend IntelliJ for development.

## Complexity

The artificial intellegence (computer player) is implemented using a min-max algorithm that traverses the entire search space and computes the best move according to an evaluation/gain function up to specific depth in the tree of all game board possibilites. It hence has a complexity that is exponential in the number of moves it has to look ahead.



