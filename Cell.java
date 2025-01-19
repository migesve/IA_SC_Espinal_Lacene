public class Cell {
    private boolean onFire;
    private boolean hasSurvivor;
    private boolean isHeadquarters; // Indique si la cellule contient le QG

    public Cell(boolean onFire, boolean hasSurvivor) {
        this.onFire = onFire;
        this.hasSurvivor = hasSurvivor;
        this.isHeadquarters = false; // Par défaut, ce n'est pas le QG
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void turnOffFire() {
        this.onFire = false;
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

    public boolean isHeadquarters() {
        return isHeadquarters;
    }

    public void setHeadquarters(boolean isHeadquarters) {
        this.isHeadquarters = isHeadquarters;
        if (isHeadquarters) {
            this.hasSurvivor = false; // Assurez-vous qu'une cellule QG ne contient pas de survivant
        }
    }
}
