import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents the second level of the game
 */
public class Level2 extends Level {

    private ArrayList<FlyingPlatform> flyingPlatforms;
    private ArrayList<DoubleScore> doubleScores;
    private ArrayList<Invincible> invincibles;
    private final double FLYING_PLATFORM_SPEED, DOUBLE_SCORE_SPEED, INVINCIBLE_SPEED;
    private final double HALF_LENGTH, HALF_HEIGHT;
    private final int DOUBLE_MAX_FRAME, INVINCIBLE_MAX_FRAME;
    private int doubleFrame, invincibleFrame;

    /**
     * The constructor for level two object
     * @param game_props properties related to coordinates, image filenames and other key values
     * @param message_props stores the message strings
     */
    public Level2(Properties game_props, Properties message_props) {
        super(game_props, message_props);

        // Extract the entities data
        List<String[]> data = IOUtils.readCsv(game_props.getProperty("level2File"));
        assert data != null;
        this.setEntityData(data);

        // Initialise the entities
        this.flyingPlatforms = new ArrayList<>();
        this.invincibles = new ArrayList<>();
        this.doubleScores = new ArrayList<>();

        // Initialise constants used
        FLYING_PLATFORM_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.speed"));
        DOUBLE_SCORE_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.doubleScore.speed"));
        INVINCIBLE_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.invinciblePower.speed"));
        HALF_LENGTH = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.halfLength"));
        HALF_HEIGHT = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.halfHeight"));
        DOUBLE_MAX_FRAME = Integer.parseInt(game_props.getProperty("gameObjects.doubleScore.maxFrames"));
        INVINCIBLE_MAX_FRAME = Integer.parseInt(game_props.getProperty("gameObjects.invinciblePower.maxFrames"));
        doubleFrame = DOUBLE_MAX_FRAME;
        invincibleFrame = INVINCIBLE_MAX_FRAME;
    }

    /**
     * Initializes entities for level 2
     */
    @Override
    public void initializeEntities() {
        super.initializeEntities();
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");

        // Goes through every data in CSV file and initialize each entity
        for (String[] entry : getEntityData()) {
            double radius, speed;
            String entityName = entry[0];
            double x = Double.parseDouble(entry[1]);
            double y = Double.parseDouble(entry[2]);
            String imagePath;

            // Each entity created is specific to each type
            switch (entityName) {
                case "FLYING_PLATFORM":
                    imagePath = game_props.getProperty("gameObjects.flyingPlatform.image");
                    FlyingPlatform newFlyPlat = new FlyingPlatform(imagePath, x, y, game_props);
                    flyingPlatforms.add(newFlyPlat);
                    break;
                case "INVINCIBLE_POWER":
                    imagePath = game_props.getProperty("gameObjects.invinciblePower.image");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.invinciblePower.radius"));
                    speed = Double.parseDouble(game_props.getProperty("gameObjects.invinciblePower.speed"));
                    Invincible newInvincible = new Invincible(imagePath, x, y, radius, speed);
                    invincibles.add(newInvincible);
                    break;
                case "DOUBLE_SCORE":
                    imagePath = game_props.getProperty("gameObjects.doubleScore.image");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.doubleScore.radius"));
                    speed = Double.parseDouble(game_props.getProperty("gameObjects.doubleScore.speed"));
                    DoubleScore newDouble = new DoubleScore(imagePath, x, y, radius, speed);
                    doubleScores.add(newDouble);
                    break;
            }
        }
    }

    /**
     * Renders the entities for level 2
     */
    public void renderEntitiesLevel2() {
        for (FlyingPlatform flyPlat: flyingPlatforms) {
            flyPlat.draw();
        }

        for (Invincible invincible: invincibles) {
            invincible.draw();
        }

        for (DoubleScore doubleScore: doubleScores) {
            doubleScore.draw();
        }
    }

    /**
     * Moves entities to the right when the left key is pressed.
     */
    public void leftMoveLevel2() {
        for (FlyingPlatform flyPlat: flyingPlatforms) {
            flyPlat.move(FLYING_PLATFORM_SPEED);
        }

        for (Invincible invincible: invincibles) {
            invincible.move(INVINCIBLE_SPEED);
        }

        for (DoubleScore doubleScore: doubleScores) {
            doubleScore.move(DOUBLE_SCORE_SPEED);
        }
    }

    /**
     * Move entities to the left when right key is pressed
     */
    public void rightMoveLevel2() {
        for (FlyingPlatform flyPlat: flyingPlatforms) {
            flyPlat.move(-FLYING_PLATFORM_SPEED);
        }

        for (Invincible invincible: invincibles) {
            invincible.move(-INVINCIBLE_SPEED);
        }

        for (DoubleScore doubleScore: doubleScores) {
            doubleScore.move(-DOUBLE_SCORE_SPEED);
        }
    }

    /**
     * Starts the game for level 2, handles all entity interactions
     * @param input input received from the user
     */
    @Override
    public void startGame(Input input) {
        super.startGame(input);
        renderEntitiesLevel2();

        checkPlayerOnFlyingPlatform(input);
        getPlayer().updatePlayer(input);
        checkCollisionLevel2(getPlayer());

        for (FlyingPlatform flyPlat: flyingPlatforms) {
            flyPlat.updateRandom();
        }

        for (Invincible invincible: invincibles) {
            invincible.powerupsUpdate();
        }

        for (DoubleScore doubleScore: doubleScores) {
            doubleScore.powerupsUpdate();
        }

        // Move the entities when the left key is pressed
        if (input.wasPressed(Keys.LEFT) || (input.isDown(Keys.LEFT))) {
            leftMoveLevel2();
        }

        // Move the entities when right key is pressed
        if ((input.wasPressed(Keys.RIGHT)) || (input.isDown(Keys.RIGHT))) {
            rightMoveLevel2();
        }
    }

    /**
     * Checks for player collisions with any power-ups
     * @param player player entity
     */
    private void checkCollisionLevel2(Player player) {

        // Checks collision with double score power-up
        for (DoubleScore doubleScore: doubleScores) {
            double dist = euclideanDistance(player, doubleScore);
            double doubleRange = player.getRadius() + doubleScore.getRadius();

            if (isColliding(dist, doubleRange)) {
                doubleScore.moveUp();

                // Updates the score
                if (!doubleScore.getHasCollided() && !doubleScore.isActive()) {
                    doubleScore.setActive(true);
                    setCoinScore(2 * getCoinScore());
                    doubleScore.setHasCollided(true);
                }
            }

            // Updates the frame when the double score is active
            if (doubleScore.isActive()) {
                doubleFrame--;
                if (doubleFrame <= 0) {
                    setCoinScore(getInitialCoin());
                    doubleScore.setHasCollided(false);
                    doubleFrame = DOUBLE_MAX_FRAME;
                }
            }
        }

        // Checks for collision with invincible power-up
        for (Invincible invincible: invincibles) {
            double dist = euclideanDistance(player, invincible);
            double doubleRange = player.getRadius() + invincible.getRadius();

            if (isColliding(dist, doubleRange)) {
                invincible.moveUp();

                // Updates the damage
                if (!invincible.getHasCollided() && !invincible.isActive()) {
                    invincible.setActive(true);
                    setEnemyDamage(0);
                    invincible.setHasCollided(true);
                }
            }

            // Updates the frame when the invincible is active
            if (invincible.isActive()) {
                invincibleFrame--;
                if (invincibleFrame <= 0) {
                    setEnemyDamage(getInitialEnemy());
                    invincible.setHasCollided(false);
                    invincibleFrame = INVINCIBLE_MAX_FRAME;
                }
            }
        }
    }


    /**
     * Checks if the player is colliding with flying platform.
     * @param flyPlat flying platform entity
     * @param player player entity
     * @return true if player is colliding with flying platform, false if not
     */
    public boolean checkPlayerCollisionFLyingPlatform(FlyingPlatform flyPlat, Player player) {
        double flyPlatX = flyPlat.getPosition().x;
        double flyPlatY = flyPlat.getPosition().y;
        double playerX = getPlayer().getPosition().x;
        double playerY = getPlayer().getPosition().y;

        // Calculate whether player is within the given range with the flying platform
        boolean withinX = Math.abs(playerX - flyPlatX) < HALF_LENGTH;
        boolean withinY = ((flyPlatY - playerY) <= HALF_HEIGHT) && ((flyPlatY - playerY) >= (HALF_HEIGHT - 1));

        return withinX && withinY;
    }

    /**
     * Checks if player is on a flying platform.
     * @param input input received from user
     */
    private void checkPlayerOnFlyingPlatform(Input input) {

        // Check for every flying platform
        for (FlyingPlatform flyPlat : flyingPlatforms) {
            boolean isCollide = checkPlayerCollisionFLyingPlatform(flyPlat, getPlayer());
            double flyPlatY = flyPlat.getPosition().y;
            double topOfPlatform = flyPlatY - (0.5 * flyPlat.getHeight());

            // If player is within range
            if (isCollide) {

                // Check whether a player has landed on a platform or not
                if (!getPlayer().getOnFlyingPlatform() || getPlayer().getPosition().y < flyPlatY) {

                    // Player has not landed on a platform
                    getPlayer().setPosition(new Point(getPlayer().getPosition().x, topOfPlatform - (0.5 * getPlayer().getHeight())));
                    getPlayer().setVerticalSpeed(0);
                    getPlayer().setOnFlyingPlatform(true);
                    break;
                }
            }

            // Player no longer on a flying platform
            getPlayer().setOnFlyingPlatform(false);
        }
    }
}
