import bagel.util.Point;

import java.util.Properties;

/**
 * Represents a coin entity
 */
public class Coin extends Entity {

    private int verticalSpeed = 0;
    private boolean hasCollided = false;
    private final double radius;
    private static final int SPEED = -10;

    /**
     * The constructor for a coin object
     * @param imagePath path of the image of the coin
     * @param x x-coordinate of the coin
     * @param y y-coordinate of the coin
     * @param radius radius of the image
     */
    public Coin(String imagePath, double x, double y, double radius) {
        super(imagePath, x, y);
        this.radius = radius;
    }

    /**
     * Returns the radius of a coin
     * @return radius of coin
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns whether coin has collided with another entity
     * @return true if coin has collided, false if not
     */
    public boolean getHasCollided() {
        return hasCollided;
    }

    /**
     * Sets the collision status
     * @param hasCollided true if coin has collided, false if not
     */
    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    /**
     * Sets the vertical speed for a coin
     */
    public void moveUp() {
        verticalSpeed = SPEED;
    }

    /**
     * Moves in the horizontal direction by the speed
     * @param speed speed of which the coin moves horizontally
     */
    public void move(double speed) {
        this.setPosition(new Point(this.getPosition().x + speed, this.getPosition().y));
    }

    /**
     * Allows the coin to move up once it has collided with a player
     */
    public void coinUpdate() {
        this.setPosition(new Point(this.getPosition().x, this.getPosition().y + verticalSpeed));
    }
}
