import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    public static final int SIZE = 10;
    private static final int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};

    private final Cell[][] cells;
    private final List<Ship> ships;

    public GameBoard() {
        cells = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                cells[row][column] = new Cell();
            }
        }

        ships = new ArrayList<>();
        placeShips(new Random());
    }

    private void placeShips(Random random) {
        for (int length : SHIP_LENGTHS) {
            Ship ship = new Ship(length);
            boolean placed = false;

            while (!placed) {
                boolean horizontal = random.nextBoolean();
                int row = random.nextInt(SIZE);
                int column = random.nextInt(SIZE);
                int rowStep = horizontal ? 0 : 1;
                int columnStep = horizontal ? 1 : 0;

                if (fits(row, column, rowStep, columnStep, length)) {
                    for (int segment = 0; segment < length; segment++) {
                        cells[row + segment * rowStep][column + segment * columnStep].setShip(ship);
                    }
                    placed = true;
                }
            }

            ships.add(ship);
        }
    }

    private boolean fits(int row, int column, int rowStep, int columnStep, int length) {
        int endRow = row + (length - 1) * rowStep;
        int endColumn = column + (length - 1) * columnStep;
        if (endRow >= SIZE || endColumn >= SIZE) {
            return false;
        }

        for (int segment = 0; segment < length; segment++) {
            if (cells[row + segment * rowStep][column + segment * columnStep].hasShip()) {
                return false;
            }
        }
        return true;
    }

    public Cell getCell(int row, int column) {
        if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
            throw new IndexOutOfBoundsException("Cell coordinates are outside the board.");
        }
        return cells[row][column];
    }

    public List<Ship> getShips() {
        return List.copyOf(ships);
    }
}
