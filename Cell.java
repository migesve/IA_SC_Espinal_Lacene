public class Cell {
    private boolean onFire;
    private boolean hasSurvivor;

    public Cell(boolean onFire, boolean hasSurvivor) {
        this.onFire = onFire;
        this.hasSurvivor = hasSurvivor;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public boolean hasSurvivor() {
        return hasSurvivor;
    }

    public void setHasSurvivor(boolean hasSurvivor) {
        this.hasSurvivor = hasSurvivor;
    }
}
