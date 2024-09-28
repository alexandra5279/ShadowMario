import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * Represents a generic entity
 */
public abstract class Entity {

    private final Image image;
    private Point position;

    /**
     * The constructor for a general entity
     * @param imagePath path of the image of the entity
     * @param x x-coordinate of the entity
     * @param y y-coordinate of the entity
     */
    public Entity(String imagePath, double x, double y) {
        image = new Image(imagePath);
        this.position = new Point(x, y);
    }

    /**
     * Returns the current position of the entity.
     * @return position of the entity
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the new position of the entity.
     * @param position new position of the entity
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Draws the image into window at its current position
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

}
