import bagel.util.Point;

import java.util.Properties;

/**
 * Represents an end flag entity
 */
public class EndFlag extends Entity {

    private final double radius;

    /**
     * The constructor for an end-flag
     * @param imagePath path of the image of the end flag
     * @param x x-coordinate of the end flag
     * @param y y-coordinate of the end flag
     * @param radius radius of the image
     */
    public EndFlag(String imagePath, double x, double y, double radius) {
        super(imagePath, x, y);
        this.radius = radius;
    }

    /**
     * Returns the radius of an end flag
     * @return radius of end flag
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Moves in the horizontal direction by the speed
     * @param speed speed of which the end flag moves horizontally
     */
    public void move(double speed) {
        this.setPosition(new Point(this.getPosition().x + speed, this.getPosition().y));
    }
}
