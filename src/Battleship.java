public class Battleship {
    public static final int TOTAL_SHIP_CELLS = 17;
    public static final int MAX_CONSECUTIVE_MISSES = 5;
    public static final int MAX_STRIKES = 3;
    public static final int TOTAL_BOARD_CELLS = GameBoard.SIZE * GameBoard.SIZE;

    private GameBoard board;
    private int consecutiveMisses;
    private int strikes;
    private int totalHits;
    private int totalMisses;
    private boolean gameOver;
    private boolean won;

    public Battleship() {
        restart();
    }

    public Cell.AttackResult attack(int row, int column) {
        if (gameOver) {
            return Cell.AttackResult.ALREADY_ATTACKED;
        }

        Cell.AttackResult result = board.getCell(row, column).attack();
        if (result == Cell.AttackResult.HIT) {
            totalHits++;
            consecutiveMisses = 0;
            if (totalHits == TOTAL_SHIP_CELLS) {
                gameOver = true;
                won = true;
            }
        } else if (result == Cell.AttackResult.MISS) {
            totalMisses++;
            consecutiveMisses++;
            if (consecutiveMisses == MAX_CONSECUTIVE_MISSES) {
                strikes++;
                consecutiveMisses = 0;
                if (strikes == MAX_STRIKES) {
                    gameOver = true;
                }
            }
        }
        return result;
    }

    public void restart() {
        board = new GameBoard();
        consecutiveMisses = 0;
        strikes = 0;
        totalHits = 0;
        totalMisses = 0;
        gameOver = false;
        won = false;
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getConsecutiveMisses() {
        return consecutiveMisses;
    }

    public int getStrikes() {
        return strikes;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public int getTotalMisses() {
        return totalMisses;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasWon() {
        return won;
    }
}
