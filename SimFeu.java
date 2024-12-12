import java.util.Random;

import java.io.IOException;

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

        // Initialize robots
        Robot[] robots = new Robot[numberOfRobots];
        for (int i = 0; i < numberOfRobots; i++) {
            robots[i] = new Robot(i, 0, 0); // Robots start at headquarters (0,0)
            grid.addRobot(robots[i]);
        }

        // Start the simulation interactively
        Simulation simulation = new Simulation(grid, robots, propagationRate);
        simulation.runInteractive();
    }
}