import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int size;
    private Cell[][] grid;
    private boolean[][] visitedCells; // Carte des cellules visitées
    private int headquartersX = 0;
    private int headquartersY = 0;
    private List<Robot> robots = new ArrayList<>();
    private int survivorsRescued = 0; // Compteur des survivants sauvés

    public Grid(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        this.visitedCells = new boolean[size][size];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(false, false); // Par défaut, pas de feu ni de survivant
                visitedCells[i][j] = false; // Cellules marquées comme non visitées au départ
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
        grid[x][y].setHasSurvivor(false); // S'assure qu'il n'y a pas de survivant au QG
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
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == headquartersX && j == headquartersY) {
                    System.out.print("H ");
                } else if (isRobotHere(i, j)) {
                    System.out.print("R ");
                } else if (grid[i][j].isOnFire()) {
                    System.out.print("F ");
                } else if (grid[i][j].hasSurvivor()) {
                    System.out.print("S ");
                } else if (isVisited(i, j)) {
                    System.out.print("V "); // Cellule visitée
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
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

    // Gestion des cellules visitées
    public boolean isVisited(int x, int y) {
        return visitedCells[x][y];
    }

    public void markAsVisited(int x, int y) {
        if (isValidCell(x, y)) {
            visitedCells[x][y] = true;
        }
    }

    // Calcul de la priorité d'une cellule (incendie, survivants, etc.)
    public int calculateCellPriority(int x, int y) {
        if (!isValidCell(x, y)) {
            return Integer.MIN_VALUE; // Priorité minimale pour une cellule invalide
        }

        Cell cell = grid[x][y];
        int priority = 0;

        if (cell.isOnFire()) {
            priority += 10; // Les incendies sont très prioritaires
        }

        if (cell.hasSurvivor()) {
            priority += 20; // Les survivants sont hautement prioritaires
        }

        if (isVisited(x, y)) {
            priority -= 2; // Réduction de priorité pour éviter les doublons
        }

        return priority;
    }

    // Gestion des survivants sauvés
    public int getSurvivorsRescued() {
        return survivorsRescued;
    }

    public void incrementSurvivorsRescued() {
        survivorsRescued++;
    }
}
