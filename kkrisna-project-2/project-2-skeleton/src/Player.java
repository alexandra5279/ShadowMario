import bagel.*;
import bagel.util.Point;

import java.util.Properties;

/**
 * Represents a player entity
 */
public class Player extends Entity {

    private final Image imageRight;
    private final double RADIUS;
    private double verticalSpeed = 0;
    private final double INITIAL_Y;
    private final static int LOSE_SPEED = 2;
    private final static int INITIAL_JUMP_SPEED = -20;
    private final static int VERTICAL_SPEED_CHANGE = 1;
    private final double HEIGHT;
    private boolean onFlyingPlatform = false;
    private double health;

    /**
     * The constructor for a player
     * @param imagePathRight path of the image of the player facing right
     * @param x x-coordinate of the player
     * @param y y-coordinate of the player
     * @param radius radius of the image
     * @param initial_y initial y coordinate of the player
     * @param health initial health of player
     */
    public Player(String imagePathRight, double x, double y, double radius, double initial_y, double health) {
        super(imagePathRight, x, y);
        imageRight = new Image(imagePathRight);
        this.RADIUS = radius;
        this.INITIAL_Y = initial_y;
        HEIGHT = imageRight.getHeight();
        this.health = health;
    }

    /**
     * Draws out the image of player facing left
     * @param leftImage the image of the player facing left
     */
    public void drawLeft(Image leftImage) {
        leftImage.draw(this.getPosition().x, this.getPosition().y);
    }

    /**
     * Returns the radius of a player
     * @return radius of player
     */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * Sets the vertical speed of the player
     * @param verticalSpeed vertical speed of player
     */
    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    /**
     * Returns whether the player is on a flying platform.
     * @return true if player is on a flying platform, false if not
     */
    public boolean getOnFlyingPlatform() {
        return onFlyingPlatform;
    }

    /**
     * Sets whether a player is on a flying platform
     * @param onFlyingPlatform true if the player is on a flying platform, false if not
     */
    public void setOnFlyingPlatform(boolean onFlyingPlatform) {
        this.onFlyingPlatform = onFlyingPlatform;
    }

    /**
     * Returns the height of the player
     * @return height of player
     */
    public double getHeight() {
        return HEIGHT;
    }

    /**
     * Returns the health of the player
     * @return health of player
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the player's health
     * @param health health to be set
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Updates the player depending on the input
     * Allows the player to jump
     * @param input input received from the user
     */
    public void updatePlayer(Input input) {

        // Check whether player should start jumping
        if (input.wasPressed(Keys.UP)) {
            verticalSpeed = INITIAL_JUMP_SPEED;
        }

        // Mid jump
        if (this.getPosition().y < INITIAL_Y) {
            verticalSpeed += VERTICAL_SPEED_CHANGE;
        }

        // Finishing jump
        if (verticalSpeed > 0 && this.getPosition().y >= INITIAL_Y && !isDead()) {
            verticalSpeed = 0;
            this.setPosition(new Point(this.getPosition().x, INITIAL_Y));
        }

        this.setPosition(new Point(this.getPosition().x, this.getPosition().y + verticalSpeed));
    }

    /**
     * Players moves down and exits the screen
     */
    public void playerDown() {
        verticalSpeed = LOSE_SPEED;
        if (!offScreen()) {
            this.setPosition(new Point(this.getPosition().x, this.getPosition().y + verticalSpeed));
        }
    }

    /**
     * Checks whether player is already off-screen
     * @return true if the player is off-screen, false if not
     */
    public boolean offScreen() {
        double height = imageRight.getHeight();

        // Height is divided by 2 because y coordinate indicates the centre coordinate of player
        return (this.getPosition().y - (height / 2)) > Window.getHeight();
    }

    /**
     * Sets the player's status to dead, affecting the vertical speed
     */
    public void dead() {
        this.verticalSpeed = LOSE_SPEED;
    }

    /**
     * Checks whether the player is dead
     * @return true if player's health is less than or equal to zero, false if not
     */
    public boolean isDead() {
        return health <= 0;
    }
}
