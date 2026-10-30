import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
    private static final Color BLANK_COLOR = new Color(100, 210, 230);
    private static final Color MISS_COLOR = Color.LIGHT_GRAY;
    private static final Color HIT_COLOR = Color.RED;
    private static final Dimension BOARD_DIMENSION = new Dimension(600, 600);

    private final JButton[][] buttons;
    private final JLabel consecutiveMissesLabel;
    private final JLabel strikesLabel;
    private final JLabel totalHitsLabel;
    private final JLabel totalMissesLabel;
    private final Battleship game;

    public GameWindow() {
        super("Battleship");
        game = new Battleship();
        buttons = new JButton[GameBoard.SIZE][GameBoard.SIZE];

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                confirmQuit();
            }
        });

        consecutiveMissesLabel = new JLabel();
        strikesLabel = new JLabel();
        totalHitsLabel = new JLabel();
        totalMissesLabel = new JLabel();
        setLayout(new BorderLayout(12, 12));
        add(createCountersPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);
        refreshCounters();

        setResizable(true);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JPanel createCountersPanel() {
        JPanel counters = new JPanel(new GridLayout(2, 2, 12, 6));
        counters.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        counters.add(consecutiveMissesLabel);
        counters.add(strikesLabel);
        counters.add(totalHitsLabel);
        counters.add(totalMissesLabel);
        return counters;
    }

    private JPanel createBoardPanel() {
        JPanel board = new JPanel(new GridLayout(GameBoard.SIZE, GameBoard.SIZE, 2, 2));
        board.setPreferredSize(BOARD_DIMENSION);
        board.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int column = 0; column < GameBoard.SIZE; column++) {
                final int cellRow = row;
                final int cellColumn = column;
                JButton button = new JButton();
                button.setBackground(BLANK_COLOR);
                button.setOpaque(true);
                button.setBorderPainted(true);
                button.setFocusPainted(false);
                button.setToolTipText("Row " + (row + 1) + ", Column " + (column + 1));
                button.addActionListener(event -> handleAttack(cellRow, cellColumn));
                buttons[row][column] = button;
                board.add(button);
            }
        }

        JPanel centeredBoard = new JPanel(new GridBagLayout());
        centeredBoard.add(board);
        return centeredBoard;
    }

    private JPanel createActionsPanel() {
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(event -> confirmRestart());
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(event -> confirmQuit());

        JPanel actions = new JPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        actions.add(playAgainButton);
        actions.add(quitButton);
        return actions;
    }

    private void handleAttack(int row, int column) {
        if (game.isGameOver()) {
            return;
        }

        Cell cell = game.getBoard().getCell(row, column);
        if (cell.isClicked()) {
            return;
        }

        Cell.AttackResult result = game.attack(row, column);
        if (result == Cell.AttackResult.ALREADY_ATTACKED) {
            return;
        }

        JButton button = buttons[row][column];
        button.setBackground(result == Cell.AttackResult.HIT ? HIT_COLOR : MISS_COLOR);
        button.setEnabled(false);
        refreshCounters();

        if (result == Cell.AttackResult.HIT) {
            Ship ship = cell.getShip();
            if (ship.isSunk()) {
                JOptionPane.showMessageDialog(this, "You sunk a ship of length " + ship.getLength() + "!",
                        "Ship Sunk", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (game.isGameOver()) {
            disableBoard();
            if (game.hasWon()) {
                JOptionPane.showMessageDialog(this, "Victory! You found all 17 ship cells.",
                        "You Win", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Game over! You received three strikes.",
                        "You Lose", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshCounters() {
        consecutiveMissesLabel.setText("Consecutive Misses: " + game.getConsecutiveMisses() + "/"
                + Battleship.MAX_CONSECUTIVE_MISSES);
        strikesLabel.setText("Strikes: " + game.getStrikes() + "/" + Battleship.MAX_STRIKES);
        totalHitsLabel.setText("Total Hits: " + game.getTotalHits() + "/" + Battleship.TOTAL_SHIP_CELLS);
        totalMissesLabel.setText("Total Misses: " + game.getTotalMisses() + "/"
                + (Battleship.TOTAL_BOARD_CELLS - Battleship.TOTAL_SHIP_CELLS));
    }

    private void disableBoard() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setEnabled(false);
            }
        }
    }

    private void confirmRestart() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?",
                "Play Again", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            game.restart();
            for (JButton[] row : buttons) {
                for (JButton button : row) {
                    button.setBackground(BLANK_COLOR);
                    button.setEnabled(true);
                }
            }
            refreshCounters();
        }
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?",
                "Quit Battleship", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
