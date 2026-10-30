import javax.swing.SwingUtilities;

public class BattleshipApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow().setVisible(true));
    }
}
