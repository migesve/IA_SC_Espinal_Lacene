import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int size;
    private Cell[][] grid;
    private boolean[][] visitedCells; // Map of visited cells
    private int headquartersX = 0;
    private int headquartersY = 0;
    private List<Robot> robots = new ArrayList<>();
    private int survivorsRescued = 0; // Counter for rescued survivors

    public Grid(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        this.visitedCells = new boolean[size][size];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(false, false); // Default: no fire, no survivor
                visitedCells[i][j] = false; // Initially, all cells are unvisited
            }
        }
    }

    public void initializeFires(int initialFires) {
        Random random = new Random();
        for (int i = 0; i < initialFires; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid[x][y].setOnFire(true);
        }
    }

    public void initializeSurvivors(int initialSurvivors) {
        Random random = new Random();
        for (int i = 0; i < initialSurvivors; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid[x][y].setHasSurvivor(true);
        }
    }

    public void setHeadquarters(int x, int y) {
        this.headquartersX = x;
        this.headquartersY = y;
        grid[x][y].setHasSurvivor(false); // Ensure no survivor is at HQ
    }

    public int getHeadquartersX() {
        return headquartersX;
    }

    public int getHeadquartersY() {
        return headquartersY;
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public int getSize() {
        return size;
    }

    public void propagateFires(int propagationRate) {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j].isOnFire()) {
                    spreadFire(random, propagationRate, i, j);
                }
            }
        }
    }

    private void spreadFire(Random random, int propagationRate, int x, int y) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (isValidCell(nx, ny) && !grid[nx][ny].isOnFire()) {
                if (random.nextInt(100) < propagationRate) {
                    grid[nx][ny].setOnFire(true);
                }
            }
        }
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void printGrid() {
        System.out.println("Legend: H = Headquarters, R = Robot, F = Fire, S = Survivor, V = Visited, . = Empty");
        System.out.println();

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < size; col++) {
            System.out.printf("%2d ", col);
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.printf("%2d ", i); // Row number
            for (int j = 0; j < size; j++) {
                if (i == headquartersX && j == headquartersY) {
                    System.out.print(" H ");
                } else if (isRobotHere(i, j)) {
                    System.out.print(" R ");
                } else if (grid[i][j].isOnFire()) {
                    System.out.print(" F ");
                } else if (grid[i][j].hasSurvivor()) {
                    System.out.print(" S ");
                } else if (isVisited(i, j)) {
                    System.out.print(" V ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isRobotHere(int x, int y) {
        for (Robot robot : robots) {
            if (robot.getX() == x && robot.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public boolean isAtHeadquarters(int x, int y) {
        return x == headquartersX && y == headquartersY;
    }

    // Handle visited cells
    public boolean isVisited(int x, int y) {
        return visitedCells[x][y];
    }

    public void markAsVisited(int x, int y) {
        if (isValidCell(x, y)) {
            visitedCells[x][y] = true;
        }
    }

    // Calculate cell priority based on fire, survivors, and visited status
    public int calculateCellPriority(int x, int y) {
        if (!isValidCell(x, y)) {
            return Integer.MIN_VALUE; // Minimum priority for invalid cells
        }

        Cell cell = grid[x][y];
        int priority = 0;

        if (cell.isOnFire()) {
            priority += 50; // Fires have high priority
        }

        if (cell.hasSurvivor()) {
            priority += 100; // Survivors have the highest priority
        }

        if (isVisited(x, y)) {
            priority -= 10; // Reduce priority for visited cells
        }

        return priority;
    }

    // Survivor management
    public int getSurvivorsRescued() {
        return survivorsRescued;
    }

    public void incrementSurvivorsRescued() {
        survivorsRescued++;
    }
}
