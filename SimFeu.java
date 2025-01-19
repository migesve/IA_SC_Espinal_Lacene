import java.io.IOException;

public class SimFeu {

    public static void main(String[] args) throws IOException {
        // Simulation parameters
        int gridSize = 20; // Size of the grid (NxN)
        int initialFires = 5; // Number of initial fires
        int propagationRate = 5; // Fire propagation rate (percentage chance)
        int numberOfRobots = 3; // Number of robots
        int initialSurvivors = 5; // Number of initial survivors

        // Initialize the grid and headquarters
        Grid grid = new Grid(gridSize);
        grid.initializeFires(initialFires);
        grid.initializeSurvivors(initialSurvivors);
        grid.setHeadquarters(0, gridSize / 2);

        // Mark the headquarters cell explicitly
        grid.getCell(0, gridSize / 2).setHeadquarters(true);

        // Initialize robots
        Robot[] robots = new Robot[numberOfRobots];
        for (int i = 0; i < numberOfRobots; i++) {
            robots[i] = new Robot(i, 0, gridSize / 2, 2, 5, 10); // Robots start at the headquarters
            grid.addRobot(robots[i]);
        }

        // Display initial configuration
        System.out.println("=== État initial de la simulation ===");
        System.out.println("Taille de la grille : " + gridSize + "x" + gridSize);
        System.out.println("Nombre de robots : " + numberOfRobots);
        System.out.println("Incendies initiaux : " + initialFires);
        System.out.println("Survivants initiaux : " + initialSurvivors);
        System.out.println("=== Grille initiale ===");
        grid.printGrid();

        // Start the simulation interactively
        Simulation simulation = new Simulation(grid, robots, propagationRate);
        simulation.runInteractive();

        // Final report after simulation
        System.out.println("\n=== Rapport final de simulation ===");
        System.out.println("Nombre de feux restants : " + countRemainingFires(grid));
        System.out.println("Survivants restants : " + countRemainingSurvivors(grid));
    }

    /**
     * Compte le nombre de cellules en feu restantes dans la grille.
     *
     * @param grid La grille de simulation.
     * @return Le nombre total de feux restants.
     */
    private static int countRemainingFires(Grid grid) {
        int remainingFires = 0;
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                if (grid.getCell(x, y).isOnFire()) {
                    remainingFires++;
                }
            }
        }
        return remainingFires;
    }

    /**
     * Compte le nombre de survivants restants dans la grille.
     *
     * @param grid La grille de simulation.
     * @return Le nombre total de survivants restants.
     */
    private static int countRemainingSurvivors(Grid grid) {
        int remainingSurvivors = 0;
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                if (grid.getCell(x, y).hasSurvivor()) {
                    remainingSurvivors++;
                }
            }
        }
        return remainingSurvivors;
    }
}
