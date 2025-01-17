import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class Simulation {
    private Grid grid;
    private List<Robot> robots; // Change to List<Robot>
    private int propagationRate;

    public Simulation(Grid grid, Robot[] robots, int propagationRate) {
        this.grid = grid;
        this.robots = Arrays.asList(robots); // Convert array to list
        this.propagationRate = propagationRate;
    }

    public void runInteractive() throws IOException {
        int step = 0;

        while (true) {
            System.out.println("Step " + (++step));

            // Propagate fires
            grid.propagateFires(propagationRate);
            grid.printGrid();

            // Handle each robot's actions
            for (Robot robot : robots) {
                if (robot.isExtinguishing()) {
                    // Robot continues extinguishing fires
                    robot.extinguishFires(grid);
                } else {
                    // Move the robot and scan the grid for fires
                    moveRobot(robot);
                    robot.scan(grid);

                    // If a fire is detected within range, start extinguishing
                    if (robot.fireInExtinguishRange(grid)) {
                        robot.extinguishFires(grid);
                    }
                }

                // Communicate fire locations with other robots
                robot.communicate(robots); // robots is now a List<Robot>
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

    private void moveRobot(Robot robot) {
        if (robot.isExtinguishing()) {
            return; // If extinguishing, don't move
        }

        int[] directions = {-1, 0, 1};
        int newX, newY;

        do {
            int dx = directions[(int) (Math.random() * directions.length)];
            int dy = directions[(int) (Math.random() * directions.length)];
            newX = robot.getX() + dx;
            newY = robot.getY() + dy;
        } while (!isValidMove(newX, newY, robot));

        robot.move(newX, newY);
        System.out.println("Robot " + robot.getId() + " moved to (" + newX + ", " + newY + ")");
    }

    private boolean isValidMove(int x, int y, Robot movingRobot) {
        if (!grid.isValidCell(x, y)) {
            return false; // Outside grid boundaries
        }

        for (Robot other : robots) {
            if (other != movingRobot && other.getX() == x && other.getY() == y) {
                return false; // Cell is occupied by another robot
            }
        }

        return true;
    }

}
