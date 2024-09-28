import bagel.util.Point;

/**
 * Represents a fireball entity
 */
public class Fireball extends Entity {
    private final double radius;
    private final double speed;

    /**
     * The constructor for a fireball
     * @param imagePath path of the image of the fireball
     * @param x x-coordinate of the fireball
     * @param y y-coordinate of the fireball
     * @param radius radius of the image
     * @param speed speed of fireball
     */
    public Fireball(String imagePath, double x, double y, double radius, double speed) {
        super(imagePath, x, y);
        this.radius = radius;
        this.speed = speed;
    }

    /**
     * Returns the radius of a fireball
     * @return radius of fireball
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Moves in the horizontal direction by the speed
     */
    public void update() {
        this.setPosition(new Point(this.getPosition().x + speed, this.getPosition().y));
    }
}
