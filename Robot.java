import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Robot {
    private int id;
    private int x;
    private int y;
    private RobotNetwork network; // Réseau pour la communication

    // Constructeur mis à jour pour inclure le réseau
    public Robot(int id, int x, int y, RobotNetwork network) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.network = network;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
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

    // Méthode pour envoyer un message au réseau
    public void communicate(String message) {
        network.sendMessage("Robot " + id + ": " + message);
    }

    // Méthode pour lire et traiter les messages du réseau
    public void processMessages() {
        List<String> messages = network.retrieveMessages();
        for (String message : messages) {
            System.out.println(message);
            // Ajoutez ici une logique pour réagir aux messages si nécessaire
        }
    }

    // Calculer les cellules visibles selon le niveau de visibilité
    public List<Cell> getVisibleCells(Grid grid, int level) {
        List<Cell> visibleCells = new ArrayList<>();
        int gridSize = grid.getSize();

        // Parcourir les cases autour du robot dans le rayon défini par "level"
        for (int dx = -level; dx <= level; dx++) {
            for (int dy = -level; dy <= level; dy++) {
                int nx = this.x + dx;
                int ny = this.y + dy;

                // Vérifier que les coordonnées sont valides (dans les limites de la grille)
                if (nx >= 0 && ny >= 0 && nx < gridSize && ny < gridSize) {
                    visibleCells.add(grid.getCell(nx, ny));
                }
            }
        }
        return visibleCells;
    }

    // Scanner les cellules visibles et agir en fonction de leur état
    public void scanAreaAndAct(Grid grid, int level) {
        List<Cell> visibleCells = getVisibleCells(grid, level);

        for (Cell cell : visibleCells) {
            // Si la cellule est en feu, le robot éteint le feu
            if (cell.isOnFire()) {
                System.out.println("Robot " + id + " sees fire and extinguishes it.");
                cell.turnOffFire();
                communicate("Fire extinguished in visible area.");
            }

            // Si la cellule contient un survivant, le robot le sauve
            if (cell.hasSurvivor()) {
                System.out.println("Robot " + id + " sees a survivor and rescues them.");
                cell.setHasSurvivor(false);
                communicate("Survivor rescued in visible area.");
            }
        }
    }

    // Méthode existante pour scanner une cellule (peut être maintenue si besoin pour des actions simples)
    public void scan(Grid grid) {
        Cell currentCell = grid.getCell(x, y);

        if (currentCell.isOnFire()) {
            System.out.println("Robot " + id + " found fire at (" + x + ", " + y + ")");
            communicate("Fire at (" + x + ", " + y + ")");

            Random random = new Random();
            int probability = random.nextInt(100);
            if (probability < 90) {
                System.out.println("Robot " + id + " turned off fire at (" + x + ", " + y + ")");
                currentCell.turnOffFire();
            } else {
                System.out.println("Robot " + id + " failed to turn off fire at (" + x + ", " + y + ")");
            }
        }

        if (currentCell.hasSurvivor()) {
            System.out.println("Robot " + id + " found survivor at (" + x + ", " + y + ")");
            communicate("Survivor at (" + x + ", " + y + ")");
        }
    }
}
