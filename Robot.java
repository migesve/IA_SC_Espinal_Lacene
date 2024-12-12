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
        }
        if (currentCell.hasSurvivor()) {
            System.out.println("Robot " + id + " found survivor at (" + x + ", " + y + ")");
        }
    }
}
