import bagel.util.Point;

/**
 * Represents a platform entity
 */
public class Platform extends Entity {

    /**
     * The constructor for a platform
     * @param imagePath path of the image of the invincible power-up
     * @param x x-coordinate of the invincible power-up
     * @param y y-coordinate of the invincible power-up
     */
    public Platform(String imagePath, double x, double y) {
        super(imagePath, x, y);
    }

    /**
     * Moves in the horizontal direction by the speed
     * @param speed speed of which the platform moves horizontally
     */
    public void move(double speed) {
        this.setPosition(new Point(this.getPosition().x + speed, this.getPosition().y));
    }
}
