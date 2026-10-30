public class Cell {
    public enum AttackResult {
        HIT,
        MISS,
        ALREADY_ATTACKED
    }

    private Ship ship;
    private boolean clicked;

    public boolean hasShip() {
        return ship != null;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isClicked() {
        return clicked;
    }

    void setShip(Ship ship) {
        if (this.ship != null) {
            throw new IllegalStateException("A ship already occupies this cell.");
        }
        this.ship = ship;
    }

    public AttackResult attack() {
        if (clicked) {
            return AttackResult.ALREADY_ATTACKED;
        }

        clicked = true;
        if (ship == null) {
            return AttackResult.MISS;
        }

        ship.registerHit();
        return AttackResult.HIT;
    }
}
