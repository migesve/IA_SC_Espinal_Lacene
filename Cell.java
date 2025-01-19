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

    /**
     * Calcule la priorité de cette cellule pour les robots.
     * La priorité est basée sur la présence de feux, de survivants et sur le fait que ce soit ou non un QG.
     *
     * @param isVisited indique si la cellule a déjà été visitée
     * @return une valeur numérique représentant la priorité
     */
    public int calculatePriority(boolean isVisited) {
        int priority = 0;

        if (onFire) {
            priority += 10; // Les feux sont très prioritaires
        }

        if (hasSurvivor) {
            priority += 5; // Les survivants sont également prioritaires
        }

        if (isHeadquarters) {
            priority -= 100; // QG non pertinent pour exploration
        }

        if (isVisited) {
            priority -= 2; // Réduit légèrement la priorité si déjà visitée
        }

        return priority;
    }
}
