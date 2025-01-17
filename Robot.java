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

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        addVisitedCell(newX, newY);
    }

    // Add cell to visited cells
    private void addVisitedCell(int x, int y) {
        visitedCells.add(x + "," + y);
    }

    // Check if a cell has been visited
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

    public void communicate(List<Robot> robots) {
        for (Robot other : robots) {
            if (this.id != other.getId() && inCommunicationRange(other)) {
                // Share visited cells
                other.receiveVisitedCells(this.visitedCells);

                // Share fire locations
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
}
