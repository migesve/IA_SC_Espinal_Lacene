public class Simulation {
    private Grid grid;
    private Robot[] robots;
    private int propagationRate;

    public Simulation(Grid grid, Robot[] robots, int propagationRate) {
        this.grid = grid;
        this.robots = robots;
        this.propagationRate = propagationRate;
    }

    public void run(int steps) {
        for (int step = 0; step < steps; step++) {
            System.out.println("Step " + (step + 1));
            grid.propagateFires(propagationRate);
            grid.printGrid();

            for (Robot robot : robots) {
                robot.scan(grid);
                // Simple move logic: move randomly for now
                int newX = (robot.getX() + 1) % grid.getSize();
                int newY = (robot.getY() + 1) % grid.getSize();
                robot.move(newX, newY);
            }

            System.out.println();
        }
    }
}
