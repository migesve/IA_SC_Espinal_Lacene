import java.util.Random;

public class Robot {
    private int id;
    private int x;
    private int y;

    public Robot(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void scan(Grid grid) {
        Cell currentCell = grid.getCell(x, y);
        if (currentCell.isOnFire()) {
            System.out.println("Robot " + id + " found fire at (" + x + ", " + y + ")");
            // random for the probabity to the robot can turn off the fire
            Random random = new Random();
            int probability = random.nextInt(100);
            if (probability < 90) {
                System.out.println("Robot " + id + " turned off fire at (" + x + ", " + y + ")");
                currentCell.turnOffFire();
            } else {
                System.out.println("Robot " + id + " failed to turn off fire at (" + x + ", " + y + ")");
            }
        }
        if (currentCell.hasSurvivor()) {
            System.out.println("Robot " + id + " found survivor at (" + x + ", " + y + ")");
        }
    }
}


// vision du robot? contraintes de terrain (cases d'eau, murs, etc.)?
// limitation de communication entre les robots? soit par un nombre de cases et limite par distance
