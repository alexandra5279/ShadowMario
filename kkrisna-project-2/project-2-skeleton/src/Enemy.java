import bagel.util.Point;

import java.util.Properties;

/**
 * Represents an enemy entity
 */
public class Enemy extends Entity implements RandomMovement {

    private final double radius;
    private boolean hasDamaged = false;
    private final double MAX_DISP;
    private final double RANDOM_SPEED;
    private int direction;
    private double currentDisplacement = 0;

    /**
     * The constructor for an enemy
     * @param imagePath path of the image of the enemy
     * @param x x-coordinate of the enemy
     * @param y y-coordinate of the enemy
     * @param radius radius of the image
     * @param game_props properties file with key values
     */
    public Enemy(String imagePath, double x, double y, double radius, Properties game_props) {
        super(imagePath, x, y);
        this.radius = radius;

        // Choose the random direction
        this.direction = chooseRandom();

        this.MAX_DISP = Double.parseDouble(game_props.getProperty("gameObjects.enemy.maxRandomDisplacementX"));;
        this.RANDOM_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.enemy.randomSpeed"));
    }

    /**
     * Returns whether enemy has damaged a player
     * @return true if enemy has damaged, false if not
     */
    public boolean getHasDamaged() {
        return hasDamaged;
    }

    /**
     * Sets the damaged status
     * @param hasDamaged true if enemy has damaged the player, false if not
     */
    public void setHasDamaged(boolean hasDamaged) {
        this.hasDamaged = hasDamaged;
    }

    /**
     * Returns the radius of an enemy
     * @return radius of enemy
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Moves in the horizontal direction by the speed
     * @param speed speed of which the enemy moves horizontally
     */
    public void move(double speed) {
        this.setPosition(new Point(this.getPosition().x + speed, this.getPosition().y));
    }


    /**
     * Updates enemy position in a random direction
     * Enemy moves within a specified range.
     * Once it exceeds the range, direction is reversed.
     */
    public void updateRandom() {

        // Checks whether the current displacement is greater than the maximum displacement
        if (Math.abs(currentDisplacement) >= MAX_DISP) {
            // Reverse the direction once maximum displacement is reached
            direction *= -1;
        }

        // Move the enemy
        double step = direction * RANDOM_SPEED;
        this.setPosition(new Point(this.getPosition().x + step, this.getPosition().y));
        currentDisplacement += step;
    }
}
