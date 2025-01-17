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

    // Initialisation de la grille avec des cellules sans feu ni survivant
    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(false, false); // Pas de feu ni de survivant par défaut
            }
        }
    }

    // Initialisation des incendies aléatoires
    public void initializeFires(int initialFires) {
        Random random = new Random();
        for (int i = 0; i < initialFires; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid[x][y].setOnFire(true);
        }
    }

    // Initialisation des survivants aléatoires
    public void initializeSurvivors(int initialSurvivors) {
        Random random = new Random();
        for (int i = 0; i < initialSurvivors; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid[x][y].setHasSurvivor(true);
        }
    }

    // Définir les coordonnées du quartier général
    public void setHeadquarters(int x, int y) {
        this.headquartersX = x;
        this.headquartersY = y;
    }

    // Ajouter un robot à la grille
    public void addRobot(Robot robot) {
        robots.add(robot);
    }

    // Récupérer une cellule de la grille
    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    // Récupérer la taille de la grille
    public int getSize() {
        return size;
    }

    // Propagation des incendies
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

    // Propager un feu à une cellule adjacente
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

    // Vérifier si une cellule est valide dans la grille
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // Vérifier si un robot est présent dans une cellule
    public boolean isRobotHere(int x, int y) {
        for (Robot robot : robots) {
            if (robot.getX() == x && robot.getY() == y) {
                return true;
            }
        }
        return false;
    }

    // Afficher l'état de la grille
    public void printGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == headquartersX && j == headquartersY) {
                    System.out.print("H ");
                } else if (isRobotHere(i, j)) {
                    System.out.print(getRobotsSymbol(i, j) + " "); // Afficher les robots présents
                } else if (grid[i][j].isOnFire()) {
                    System.out.print("F ");
                } else if (grid[i][j].hasSurvivor()) {
                    System.out.print("S ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    // Obtenir les symboles des robots présents dans une cellule
    private String getRobotsSymbol(int x, int y) {
        StringBuilder robotsSymbol = new StringBuilder();
        for (Robot robot : robots) {
            if (robot.getX() == x && robot.getY() == y) {
                robotsSymbol.append("R").append(robot.getId());
            }
        }
        return robotsSymbol.toString();
    }

    // Déplacer un robot vers une nouvelle position s'il n'y a pas de chevauchement
    public boolean moveRobot(Robot robot, int newX, int newY) {
        if (isValidCell(newX, newY) && !isRobotHere(newX, newY)) {
            robot.move(newX, newY);
            return true;
        }
        return false; // Si la cellule est occupée ou invalide
    }

    // Récupérer le nombre total de robots sur la grille
    public int getRobotCount() {
        return robots.size();
    }

    // Afficher les informations sur les robots (id et position)
    public void printRobotInfo() {
        System.out.println("Number of robots on the grid: " + getRobotCount());
        for (Robot robot : robots) {
            System.out.println("Robot ID: " + robot.getId() + ", Position: (" + robot.getX() + ", " + robot.getY() + ")");
        }
    }
}
