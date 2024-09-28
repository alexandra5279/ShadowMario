/**
 * Represents an invincible power-ups entity
 */
public class Invincible extends Powerups {

    private boolean isActive = false;

    /**
     * The constructor for an invincible power-up
     * @param imagePath path of the image of the invincible power-up
     * @param x x-coordinate of the invincible power-up
     * @param y y-coordinate of the invincible power-up
     * @param radius radius of the image
     * @param speed speed of invincible power-up
     */
    public Invincible(String imagePath, double x, double y, double radius, double speed) {
        super(imagePath, x, y, radius);
        this.setSpeed(speed);
    }

    /**
     * Returns whether invincible is currently active
     * @return true if active, false if not
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the activation status of the invincible power-up.
     * @param active true if the double score power-ups is active, false if not
     */
    public void setActive(boolean active) {
        isActive = active;
    }

}
