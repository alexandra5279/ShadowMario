import bagel.Image;
import bagel.util.Point;
/**
 * Represents a power-up entity
 */
public class Powerups extends Entity {

    private final double radius;
    private double speed;
    private boolean hasCollided = false;
    private double verticalSpeed = 0;

    /**
     * The constructor for power-ups
     * @param imagePath path of the image of the power-up
     * @param x x-coordinate of the power-up
     * @param y y-coordinate of the power-up
     * @param radius radius of the image
     */
    public Powerups(String imagePath, double x, double y, double radius) {
        super(imagePath, x, y);
        this.radius = radius;
    }

    /**
     * Returns the radius of a power-up
     * @return radius of power-up
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns whether power-up has collided with another entity
     * @return true if power-up has collided, false if not
     */
    public boolean getHasCollided() {
        return hasCollided;
    }

    /**
     * Sets the collision status
     * @param hasCollided true if power-up has collided, false if not
     */
    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    /**
     * Sets the speed for power-up
     *
     * @param speed speed for power-up
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Moves in the horizontal direction by the given speed
     * @param speedHorizontal horizontal speed of power-up
     */
    public void move(double speedHorizontal) {
        this.setPosition(new Point(this.getPosition().x + speedHorizontal, this.getPosition().y));
    }

    /**
     * Sets the vertical speed for a power-up
     */
    public void moveUp() {
        verticalSpeed = -1 * speed;
    }

    /**
     * Allows the power-up to move up once it has collided with a player
     */
    public void powerupsUpdate() {
        this.setPosition(new Point(this.getPosition().x, this.getPosition().y + verticalSpeed));
    }

}
