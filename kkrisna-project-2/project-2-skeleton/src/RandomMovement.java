import java.util.Random;

/**
 * Interface for entities with behaviour of random movement
 */
public interface RandomMovement {

    /**
     * Chooses a random direction
     * @return 1 or -1 chosen randomly
     */
    default int chooseRandom() {
        Random rand = new Random();
        return rand.nextBoolean() ? 1 : -1;
    }

    /**
     * Updates the entity position randomly
     * Classes that implement the interface will specify the specific behaviour
     */
    void updateRandom();
}
