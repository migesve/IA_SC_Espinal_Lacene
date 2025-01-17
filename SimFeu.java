import java.io.IOException;
import java.util.Random;

public class SimFeu {

    public static void main(String[] args) throws IOException {
        // Simulation parameters
        int gridSize = 50; // Size of the grid (NxN)
        int initialFires = 5; // Number of initial fires
        int propagationRate = 20; // Fire propagation rate (percentage chance)
        int numberOfRobots = 3; // Number of robots
        int initialSurvivors = 5; // Number of initial survivors

        // Initialize the grid and headquarters
        Grid grid = new Grid(gridSize);
        grid.initializeFires(initialFires);
        grid.initializeSurvivors(initialSurvivors);
        grid.setHeadquarters(0, 0);

        // Initialize the communication network
        RobotNetwork network = new RobotNetwork();

        // Initialize robots and link them to the network
        Robot[] robots = new Robot[numberOfRobots];
        Random random = new Random();

        for (int i = 0; i < numberOfRobots; i++) {
            int x, y;
            // Assure que les robots sont placés sur des cases distinctes
            do {
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            } while (grid.isRobotHere(x, y)); // Rechercher une cellule libre

            robots[i] = new Robot(i, x, y, network); // Place le robot sur une position unique
            grid.addRobot(robots[i]);
        }

        // Afficher le nombre de robots sur la grille
        System.out.println("Number of robots on the grid: " + grid.getRobotCount());
        grid.printRobotInfo(); // Affiche les informations des robots (id, position)

        // Start the simulation interactively
        Simulation simulation = new Simulation(grid, robots, propagationRate);
        simulation.runInteractive();
    }
}
