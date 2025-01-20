import java.io.IOException;
import java.util.Scanner;

public class SimFeu {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Demander les paramètres à l'utilisateur ou utiliser des valeurs par défaut
        System.out.println("Bienvenue dans la simulation d'intervention des robots !");
        System.out.println("Appuyez sur Entrée pour utiliser les valeurs par défaut ou saisissez vos propres paramètres.");
        
        System.out.print("Taille de la grille (par défaut : 40) : ");
        String gridSizeInput = scanner.nextLine();
        int gridSize = gridSizeInput.isEmpty() ? 40 : Integer.parseInt(gridSizeInput);

        System.out.print("Nombre d'incendies initiaux (par défaut : 5) : ");
        String initialFiresInput = scanner.nextLine();
        int initialFires = initialFiresInput.isEmpty() ? 5 : Integer.parseInt(initialFiresInput);

        System.out.print("Taux de propagation des incendies (par défaut : 10) : ");
        String propagationRateInput = scanner.nextLine();
        int propagationRate = propagationRateInput.isEmpty() ? 10 : Integer.parseInt(propagationRateInput);

        System.out.print("Nombre de robots (par défaut : 7) : ");
        String numberOfRobotsInput = scanner.nextLine();
        int numberOfRobots = numberOfRobotsInput.isEmpty() ? 7 : Integer.parseInt(numberOfRobotsInput);

        System.out.print("Nombre de survivants initiaux (par défaut : 5) : ");
        String initialSurvivorsInput = scanner.nextLine();
        int initialSurvivors = initialSurvivorsInput.isEmpty() ? 5 : Integer.parseInt(initialSurvivorsInput);

        // Initialiser la grille et les robots
        Grid grid = new Grid(gridSize);
        grid.initializeFires(initialFires);
        grid.initializeSurvivors(initialSurvivors);
        grid.setHeadquarters(0, gridSize / 2);
        grid.getCell(0, gridSize / 2).setHeadquarters(true);

        Robot[] robots = new Robot[numberOfRobots];
        for (int i = 0; i < numberOfRobots; i++) {
            robots[i] = new Robot(i, 0, gridSize / 2, 2, 5, 10); // Robots démarrent au QG avec, les 3 premieres variables et extinguishRange, visionRange et communicationRange
            grid.addRobot(robots[i]);
        }

        // Afficher la configuration initiale
        System.out.println("\n=== Configuration initiale ===");
        System.out.println("Taille de la grille : " + gridSize + "x" + gridSize);
        System.out.println("Nombre de robots : " + numberOfRobots);
        System.out.println("Incendies initiaux : " + initialFires);
        System.out.println("Survivants initiaux : " + initialSurvivors);
        grid.printGrid();

        // Demander le mode d'exécution
        System.out.println("\nChoisissez le mode d'exécution :");
        System.out.println("1 - Étape par étape (manuellement avec Entrée)");
        System.out.println("2 - Automatique (jusqu'à ce qu'il n'y ait plus d'incendies ou de survivants)");
        System.out.print("Votre choix : ");
        int mode = scanner.nextInt();

        Simulation simulation = new Simulation(grid, robots, propagationRate);

        if (mode == 1) {
            // Mode interactif étape par étape
            simulation.runInteractive();
        } else if (mode == 2) {
            // Mode automatique
            simulation.runUntilFinalState();
        } else {
            System.out.println("Choix invalide. Fin du programme.");
        }

        // Afficher le rapport final
        System.out.println("\n=== Rapport final de simulation ===");
        System.out.println("Nombre de feux restants : " + simulation.countRemainingFires(grid));
        System.out.println("Nombre de survivants restants : " + simulation.countRemainingSurvivors(grid));
        System.out.println("Survivants sauvés : " + grid.getSurvivorsRescued());
        System.out.println("Survivants pas sauvés : " + grid.getSurvivorsNotRescued());
        System.out.println("Feux restants : " + simulation.countRemainingFires(grid));
    }
}
