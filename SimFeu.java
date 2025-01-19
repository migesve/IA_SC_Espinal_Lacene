import java.util.Random;
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

        // Set the cell of the headquarters explicitly
        grid.getCell(0, gridSize / 2).setHeadquarters(true);

        // Initialize robots
        Robot[] robots = new Robot[numberOfRobots];
        for (int i = 0; i < numberOfRobots; i++) {
            robots[i] = new Robot(i, 0, gridSize / 2, 2, 5, 10); // Robots start at the headquarters
            grid.addRobot(robots[i]);
        }

        // Print initial state of the grid
        System.out.println("État initial de la grille :");
        grid.printGrid();

        // Start the simulation interactively
        Simulation simulation = new Simulation(grid, robots, propagationRate);
        simulation.runInteractive();
    }
}
