import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int size;
    private Cell[][] grid;
    private int headquartersX = 0;
    private int headquartersY = 0;
    private List<Robot> robots = new ArrayList<>();

    public Grid(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(false, false); // No fire, no survivor by default
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

    public void setHeadquarters(int x, int y) {
        this.headquartersX = x;
        this.headquartersY = y;
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

    private boolean isValidCell(int x, int y) {
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
}
