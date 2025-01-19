import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Robot {
    private int id;
    private int x;
    private int y;
    private int extinguishRange;
    private int visionRange;
    private int communicationRange;
    private List<int[]> fireLocations;
    private boolean isExtinguishing;
    private Set<String> visitedCells; // Track visited cells
    private int energyLevel; // Current energy level
    private int maxEnergy; // Maximum energy capacity
    private boolean isCharging; // Charging status

    public Robot(int id, int x, int y, int extinguishRange, int visionRange, int communicationRange) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.extinguishRange = extinguishRange;
        this.visionRange = visionRange;
        this.communicationRange = communicationRange;
        this.fireLocations = new ArrayList<>();
        this.isExtinguishing = false;
        this.visitedCells = new HashSet<>(); // Initialize visited cells
        this.maxEnergy = 100; // Set maximum energy
        this.energyLevel = maxEnergy; // Initialize energy to maximum
        this.isCharging = false; // Initially, not charging
        addVisitedCell(x, y); // Mark starting position as visited
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public boolean isExtinguishing() {
        return isExtinguishing;
    }

    public boolean needsRecharge() {
        return energyLevel < 20; // Threshold for recharge
    }

    public boolean isCharging() {
        return isCharging;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void move(Grid grid) {
        // Trouver la cellule prioritaire
        int[] priorityCell = findPriorityCell(grid);
        if (priorityCell != null) {
            int newX = priorityCell[0];
            int newY = priorityCell[1];
            grid.markAsVisited(newX, newY); // Marquer la cellule comme visitée
            this.x = newX;
            this.y = newY;
            System.out.println("Robot " + id + " se déplace vers la cellule prioritaire (" + newX + ", " + newY + ")");
        } else {
            System.out.println("Robot " + id + " n'a trouvé aucune cellule prioritaire.");
        }
        decreaseEnergy(1); // Moving consumes negligible energy
    }

    /**
     * Méthode pour déplacer manuellement le robot vers des coordonnées spécifiques.
     */
    public void moveManually(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        System.out.println("Robot " + id + " s'est déplacé manuellement à (" + newX + ", " + newY + ").");
    }

    private void addVisitedCell(int x, int y) {
        visitedCells.add(x + "," + y);
    }

    public boolean hasVisited(int x, int y) {
        return visitedCells.contains(x + "," + y);
    }

    public Set<String> getVisitedCells() {
        return visitedCells;
    }

    public void scan(Grid grid) {
        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (grid.isValidCell(nx, ny)) {
                    Cell cell = grid.getCell(nx, ny);
                    if (cell.isOnFire()) {
                        fireLocations.add(new int[]{nx, ny});
                        System.out.println("Robot " + id + " a détecté un feu à (" + nx + ", " + ny + ")");
                    }
                }
            }
        }

        if (fireInExtinguishRange(grid)) {
            isExtinguishing = true;
        }
    }

    public void extinguishFires(Grid grid) {
        for (int dx = -extinguishRange; dx <= extinguishRange; dx++) {
            for (int dy = -extinguishRange; dy <= extinguishRange; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (grid.isValidCell(nx, ny)) {
                    Cell cell = grid.getCell(nx, ny);
                    if (cell.isOnFire()) {
                        Random random = new Random();
                        if (random.nextInt(100) < 90) {
                            cell.turnOffFire();
                            decreaseEnergy(10); // Extinguishing consumes energy
                            System.out.println("Robot " + id + " a éteint un feu à (" + nx + ", " + ny + ")");
                        }
                    }
                }
            }
        }

        if (!fireInExtinguishRange(grid)) {
            isExtinguishing = false;
        }
    }

    public boolean fireInExtinguishRange(Grid grid) {
        for (int dx = -extinguishRange; dx <= extinguishRange; dx++) {
            for (int dy = -extinguishRange; dy <= extinguishRange; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (grid.isValidCell(nx, ny) && grid.getCell(nx, ny).isOnFire()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[] findPriorityCell(Grid grid) {
        int[] priorityCell = null;
        int maxPriority = Integer.MIN_VALUE;

        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int nx = x + dx;
                int ny = y + dy;

                if (grid.isValidCell(nx, ny) && !grid.isVisited(nx, ny)) {
                    int priority = grid.calculateCellPriority(nx, ny);
                    if (priority > maxPriority) {
                        maxPriority = priority;
                        priorityCell = new int[]{nx, ny};
                    }
                }
            }
        }
        return priorityCell; // Retourne la cellule la plus prioritaire
    }

    public void communicate(List<Robot> robots) {
        for (Robot other : robots) {
            if (this.id != other.getId() && inCommunicationRange(other)) {
                other.receiveVisitedCells(this.visitedCells);
                other.receiveFireLocations(this.fireLocations);
                System.out.println("Robot " + id + " a communiqué avec le robot " + other.getId());
            }
        }
    }

    public void receiveVisitedCells(Set<String> otherVisitedCells) {
        visitedCells.addAll(otherVisitedCells);
        System.out.println("Robot " + id + " a reçu des informations sur les cellules visitées.");
    }

    public void receiveFireLocations(List<int[]> newFireLocations) {
        for (int[] fire : newFireLocations) {
            if (!fireLocations.contains(fire)) {
                fireLocations.add(fire);
                System.out.println("Robot " + id + " a reçu des informations sur un feu à (" + fire[0] + ", " + fire[1] + ")");
            }
        }
    }

    private boolean inCommunicationRange(Robot other) {
        int distanceX = Math.abs(this.x - other.getX());
        int distanceY = Math.abs(this.y - other.getY());
        return distanceX <= communicationRange && distanceY <= communicationRange;
    }

    public void decreaseEnergy(int amount) {
        energyLevel -= amount;
        if (energyLevel < 0) {
            energyLevel = 0; // Prevent negative energy
        }
    }

    public void startCharging() {
        isCharging = true;
    }

    public void rechargeEnergy() {
        if (isCharging) {
            energyLevel += 10; // Recharge increment
            if (energyLevel >= maxEnergy) {
                energyLevel = maxEnergy;
                isCharging = false; // Fully charged
                System.out.println("Robot " + id + " est complètement rechargé.");
            }
        }
    }
}
