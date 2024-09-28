import bagel.util.Point;
import java.util.Properties;

/**
 * Represents an enemy boss entity
 */
public class EnemyBoss extends Enemy {

    private double verticalSpeed = 0;

    /**
     * The constructor for an enemy boss
     * @param imagePath path of the image of the enemy boss
     * @param x x-coordinate of the enemy boss
     * @param y y-coordinate of the enemy boss
     * @param radius radius of the image
     * @param game_props properties file with key values
     */
    public EnemyBoss(String imagePath, double x, double y, double radius, Properties game_props) {
        super(imagePath, x, y, radius, game_props);
    }

    /**
     * Sets the vertical speed of the enemy boss
     * @param speed vertical speed of enemy boss
     */
    public void setVerticalSpeed(double speed) {
        this.verticalSpeed = speed;
    }

    /**
     * Updates the position of enemy boss based on its current vertical speed.
     */
    public void updateEnemy() {
        this.setPosition(new Point(this.getPosition().x, this.getPosition().y + verticalSpeed));
    }
}
