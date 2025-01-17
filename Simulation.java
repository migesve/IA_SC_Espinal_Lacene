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

            // Propagation des incendies
            grid.propagateFires(propagationRate);

            // Les robots agissent et se déplacent simultanément
            for (Robot robot : robots) {
                // Scanner l'environnement et agir (éteindre feu, sauver survivants)
                robot.scanAreaAndAct(grid, 3); // Niveau de visibilité 3

                // Traiter les messages échangés via le réseau
                robot.processMessages();

                // Déplacement du robot (logique simple, peut être améliorée)
                int newX = (robot.getX() + 1) % grid.getSize();
                int newY = (robot.getY() + 1) % grid.getSize();
                robot.move(newX, newY);
            }

            // Afficher la grille mise à jour
            grid.printGrid();

            // Demander à l'utilisateur d'avancer ou de quitter
            System.out.println("Press SPACE and ENTER to continue or Q and ENTER to quit...");
            char input = (char) System.in.read();

            if (input == 'q' || input == 'Q') {
                System.out.println("Simulation ended by user.");
                break;
            }

            // Consommer les caractères restants dans le buffer
            while (System.in.read() != '\n') {
                // Clear buffer
            }
        }
    }
}
