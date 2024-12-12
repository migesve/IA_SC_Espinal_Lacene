import java.io.IOException;

public class Simulation {
    private Grid grid;
    private Robot[] robots;
    private int propagationRate;

    public Simulation(Grid grid, Robot[] robots, int propagationRate) {
        this.grid = grid;
        this.robots = robots;
        this.propagationRate = propagationRate;
    }

    public void runInteractive() throws IOException {
        int step = 0;

        while (true) {
            System.out.println("Step " + (++step));
            grid.propagateFires(propagationRate);
            grid.printGrid();

            for (Robot robot : robots) {
                robot.scan(grid);

                // Example robot movement: move randomly
                int newX = (robot.getX() + 1) % grid.getSize();
                int newY = (robot.getY() + 1) % grid.getSize();
                robot.move(newX, newY);
            }

            System.out.println("Press SPACE and ENTER to continue or Q and ENTER to quit...");
            char input = (char) System.in.read();

            if (input == 'q' || input == 'Q') {
                System.out.println("Simulation ended by user.");
                break;
            }

            // Clear remaining input buffer
            while (System.in.read() != '\n') {
                // Consume remaining characters
            }
        }
    }
}
