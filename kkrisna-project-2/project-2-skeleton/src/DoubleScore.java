import bagel.util.Point;

/**
 * Represents a double score power-ups entity
 */
public class DoubleScore extends Powerups {

    private boolean isActive = false;

    /**
     * The constructor for a double score power-up object
     * @param imagePath path of the image of the double score
     * @param x x-coordinate of the double score power-up
     * @param y y-coordinate of the double score power-up
     * @param radius radius of the image
     * @param speed speed of double score power-up
     */
    public DoubleScore(String imagePath, double x, double y, double radius, double speed) {
        super(imagePath, x, y, radius);
        this.setSpeed(speed);
    }

    /**
     * Returns whether double score is currently active
     * @return true if active, false if not
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the activation status of the double score power-up.
     * @param active true if the double score power-ups is active, false if not
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}
