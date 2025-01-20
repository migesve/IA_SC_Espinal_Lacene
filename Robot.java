import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Collections;


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




    public void moveManually(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        //System.out.println("Robot " + id + " manually moved to (" + newX + ", " + newY + ").");
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
                        //System.out.println("Robot " + id + " detected a fire at (" + nx + ", " + ny + ")");
                    }
                }
            }
        }

        if (fireInExtinguishRange(grid)) {
            isExtinguishing = true;
        }
    }
    
    public void move(Grid grid, List<Robot> robots) {
        // verifier les priorités
        int[] priorityCell = findPriorityCell(grid, robots);

        if (priorityCell != null) {
            // calcul d'un pas vers la cellule prioritaire
            int dx = Integer.compare(priorityCell[0], x);
            int dy = Integer.compare(priorityCell[1], y);
            int newX = x + dx;
            int newY = y + dy;

            if (grid.isValidCell(newX, newY)) {
                // Move to prochaine celulle et marquer comme visitée
                this.x = newX;
                this.y = newY;
                grid.markAsVisited(newX, newY);
                decreaseEnergy(1); // bouger consomme de l'énergie
                System.out.println("Robot " + id + " moved to (" + newX + ", " + newY + ")");
            } else {
                // si la celulle prioritaire n'est pas valide, bouger aléatoirement
                fallbackRandomMove(grid, robots);
            }
        } else {
            // si la celulle prioritaire n'est pas trouvée, bouger aléatoirement
            fallbackRandomMove(grid, robots);
        }
    }

    private void fallbackRandomMove(Grid grid, List<Robot> robots) {
        int[] directions = {-1, 0, 1};
        int newX, newY;

        do {
            int dx = directions[new Random().nextInt(directions.length)];
            int dy = directions[new Random().nextInt(directions.length)];
            newX = x + dx;
            newY = y + dy;
        } while (!grid.isValidCell(newX, newY) || grid.isVisited(newX, newY) || isRobotHere(newX, newY, robots));

        this.x = newX;
        this.y = newY;
        grid.markAsVisited(newX, newY);
        decreaseEnergy(1); // Bouger consomme de l'énergie
        System.out.println("Robot " + id + " moved randomly to (" + newX + ", " + newY + ")");
    }

    private int[] findPriorityCell(Grid grid, List<Robot> robots) {
        int[] priorityCell = null;
        int maxPriority = Integer.MIN_VALUE;

        List<int[]> candidates = new ArrayList<>();
        for (int dx = -visionRange; dx <= visionRange; dx++) {
            for (int dy = -visionRange; dy <= visionRange; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (grid.isValidCell(nx, ny) && !isRobotHere(nx, ny, robots)) {
                    candidates.add(new int[]{nx, ny});
                }
            }
        }

        Collections.shuffle(candidates); // aleatoire pour eviter les tourner en rond

        for (int[] cell : candidates) {
            int nx = cell[0];
            int ny = cell[1];
            int priority = grid.calculateCellPriority(nx, ny);
            if (priority > maxPriority) {
                maxPriority = priority;
                priorityCell = cell;
            }
        }

        return priorityCell;
    }

    private boolean isRobotHere(int x, int y, List<Robot> robots) {
        for (Robot robot : robots) {
            if (robot.getX() == x && robot.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void communicate(Grid grid, List<Robot> robots) {
        for (Robot other : robots) {
            if (this.id != other.getId() && inCommunicationRange(other)) {
                other.receiveVisitedCells(this.visitedCells);
                other.receiveFireLocations(this.fireLocations);

                // Share requests for help
                if (fireInExtinguishRange(grid)) {
                    other.fireLocations.addAll(this.fireLocations);
                }
            }
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
                            //System.out.println("Robot " + id + " extinguished a fire at (" + nx + ", " + ny + ")");
                        }
                    }
                }
            }
        }

        if (!fireInExtinguishRange(grid)) {
            isExtinguishing = false;
        }
    }

    public boolean rescueSurvivor(Grid grid) {
        // Check if the robot is on a cell with a survivor
        Cell cell = grid.getCell(x, y);
        if (cell.hasSurvivor()) {
            cell.setHasSurvivor(false); // Save the survivor
            grid.incrementSurvivorsRescued(); // Update the global counter
            //System.out.println("Robot " + id + " rescued a survivor at (" + x + ", " + y + ")");
            decreaseEnergy(5); // Saving a survivor consumes energy
            return true;
        }
        return false; // No survivor to rescue
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

    public void receiveVisitedCells(Set<String> otherVisitedCells) {
        visitedCells.addAll(otherVisitedCells);
        //System.out.println("Robot " + id + " received visited cell information.");
    }

    public void receiveFireLocations(List<int[]> newFireLocations) {
        for (int[] fire : newFireLocations) {
            if (!fireLocations.contains(fire)) {
                fireLocations.add(fire);
                //System.out.println("Robot " + id + " received information about a fire at (" + fire[0] + ", " + fire[1] + ")");
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
                //System.out.println("Robot " + id + " is fully charged.");
            }
        }
    }
}
