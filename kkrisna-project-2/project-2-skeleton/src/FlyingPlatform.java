import bagel.util.Point;
import bagel.Image;
import java.util.Properties;

/**
 * Represents a flying platform entity
 */
public class FlyingPlatform extends Platform implements RandomMovement {

    private final double MAX_DISP, RANDOM_SPEED;
    private int direction;
    private double currentDisplacement = 0;
    private final double HEIGHT;

    /**
     * The constructor for a flying platform
     * @param imagePath path of the image of the flying platform
     * @param x x-coordinate of the flying platform
     * @param y y-coordinate of the flying platform
     * @param game_props properties file with key values
     */
    public FlyingPlatform(String imagePath, double x, double y, Properties game_props) {
        super(imagePath, x, y);
        this.MAX_DISP = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));;
        this.RANDOM_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        this.HEIGHT = new Image(imagePath).getHeight();
        this.direction = chooseRandom();
    }

    /**
     * Returns the height of the flying platform
     * @return height of flying platform
     */
    public double getHeight() {
        return HEIGHT;
    }

    /**
     * Updates flying platform position in a random direction
     * Flying platform moves within a specified range.
     * Once it exceeds the range, direction is reversed.
     */
    @Override
    public void updateRandom() {

        // Checks whether the current displacement is greater than the maximum displacement
        if (Math.abs(currentDisplacement) >= MAX_DISP) {
            direction *= -1;  // Reverse direction at bounds
        }

        // Move the platform
        double step = direction * RANDOM_SPEED;
        this.setPosition(new Point(this.getPosition().x + step, this.getPosition().y));
        currentDisplacement += step;
    }
}
