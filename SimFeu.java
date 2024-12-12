import java.util.Random;

public class SimFeu {

    public static void main(String[] args) {
        // Simulation parameters
        int gridSize = 10; // Size of the grid (NxN)
        int initialFires = 5; // Number of initial fires
        int propagationRate = 20; // Fire propagation rate (percentage chance)
        int numberOfRobots = 3; // Number of robots

        // Initialize the grid and headquarters
        Grid grid = new Grid(gridSize);
        grid.initializeFires(initialFires);

        // Initialize robots
        Robot[] robots = new Robot[numberOfRobots];
        for (int i = 0; i < numberOfRobots; i++) {
            robots[i] = new Robot(i, 0, 0); // Robots start at headquarters (0,0)
        }

        // Start the simulation
        Simulation simulation = new Simulation(grid, robots, propagationRate);
        simulation.run(10); // Run the simulation for 10 steps
    }
}