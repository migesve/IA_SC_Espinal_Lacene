import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class Simulation {
    private Grid grid;
    private List<Robot> robots;
    private int propagationRate;

    public Simulation(Grid grid, Robot[] robots, int propagationRate) {
        this.grid = grid;
        this.robots = Arrays.asList(robots);
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
                if (robot.isCharging()) {
                    // If the robot is charging, let it recharge
                    robot.rechargeEnergy();
                    System.out.println("Robot " + robot.getId() + " est en cours de recharge. Énergie actuelle : " + robot.getEnergyLevel());
                    continue;
                }

                if (robot.needsRecharge()) {
                    // If the robot needs to recharge, move it to the headquarters
                    System.out.println("Robot " + robot.getId() + " retourne au QG pour se recharger.");
                    moveRobotToHeadquarters(robot);
                    robot.startCharging();
                    continue;
                }

                if (robot.isExtinguishing()) {
                    // Robot continues extinguishing fires
                    robot.extinguishFires(grid);
                } else {
                    // Move the robot and scan the grid for fires
                    robot.move(grid); // Utilise la logique de priorisation pour se déplacer
                    robot.scan(grid);

                    // If a fire is detected within range, start extinguishing
                    if (robot.fireInExtinguishRange(grid)) {
                        robot.extinguishFires(grid);
                    }
                }

                // Communicate fire locations with other robots
                robot.communicate(robots);
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

    private void moveRobotToHeadquarters(Robot robot) {
        int hqX = grid.getHeadquartersX(); // Coordonnées du QG
        int hqY = grid.getHeadquartersY();

        // Calcul de l'étape pour se rapprocher du QG
        int dx = Integer.compare(hqX, robot.getX()); // Direction en X
        int dy = Integer.compare(hqY, robot.getY()); // Direction en Y

        int newX = robot.getX() + dx;
        int newY = robot.getY() + dy;

        if (isValidMove(newX, newY, robot)) {
            // Déplacement manuel avec une méthode spécifique dans la classe Robot
            robot.moveManually(newX, newY); // Utilisation de moveManually pour des déplacements directs
            System.out.println("Robot " + robot.getId() + " se déplace vers le QG : (" + newX + ", " + newY + ")");
        }
    }

    private boolean isValidMove(int x, int y, Robot movingRobot) {
        if (!grid.isValidCell(x, y)) {
            return false; // Hors des limites de la grille
        }

        for (Robot other : robots) {
            if (other != movingRobot && other.getX() == x && other.getY() == y) {
                return false; // La cellule est déjà occupée par un autre robot
            }
        }

        return true;
    }
}
