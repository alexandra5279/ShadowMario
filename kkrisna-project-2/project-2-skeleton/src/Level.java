import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents the abstract level which manages common entities for all levels
 */
public abstract class Level {

    private final Font SCORE_FONT;
    private final Font HEALTH_FONT;
    private final double SCORE_X, SCORE_Y, HEALTH_X, HEALTH_Y;

    private double coinScore, enemyDamage;
    private final double INITIAL_COIN_SCORE, INITIAL_ENEMY_DAMAGE;
    private double platformWidth;
    private double score = 0.0;

    private List<String[]> entityData;

    private Platform platform;
    private EndFlag endFlag;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;

    private final double COIN_SPEED, PLATFORM_SPEED, ENEMY_SPEED, END_FLAG_SPEED;
    private boolean playerDown = false;

    /**
     * The constructor for a level
     * @param game_props properties related to coordinates, image filenames and other key values
     * @param message_props stores the message strings
     */
    public Level(Properties game_props, Properties message_props) {
        final int scoreSize, healthSize;

        // Initialize the properties of score message
        scoreSize = Integer.parseInt(game_props.getProperty("score.fontSize"));
        SCORE_FONT = new Font(game_props.getProperty("font"), scoreSize);
        this.SCORE_X = Double.parseDouble(game_props.getProperty("score.x"));
        this.SCORE_Y = Double.parseDouble(game_props.getProperty("score.y"));

        // Initialize the properties of health message
        healthSize = Integer.parseInt(game_props.getProperty("playerHealth.fontSize"));
        HEALTH_FONT = new Font(game_props.getProperty("font"), healthSize);
        this.HEALTH_X = Double.parseDouble(game_props.getProperty("playerHealth.x"));
        this.HEALTH_Y = Double.parseDouble(game_props.getProperty("playerHealth.y"));

        // Initialize coin score and enemy damage size
        INITIAL_COIN_SCORE = Double.parseDouble(game_props.getProperty("gameObjects.coin.value"));
        coinScore = INITIAL_COIN_SCORE;
        INITIAL_ENEMY_DAMAGE = Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        enemyDamage = INITIAL_ENEMY_DAMAGE;

        // Initialise the speed for each entity except player
        COIN_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.coin.speed"));
        END_FLAG_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.speed"));
        ENEMY_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.enemy.speed"));
        PLATFORM_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.platform.speed"));

        // Initialise entities
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
    }

    /**
     * Returns the list of entity data.
     * @return list of entity data
     */
    public List<String[]> getEntityData() {
        return entityData;
    }

    /**
     * Sets the entity data from the list
     * @param data list of entity data
     */
    public void setEntityData(List<String[]> data) {
        this.entityData = new ArrayList<>(data);
    }

    /**
     * Returns player entity.
     * @return the player entity
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns end flag entity.
     * @return the end flag entity
     */
    public EndFlag getEndFlag() {
        return endFlag;
    }

    /**
     * Returns coin score value.
     * @return the coin score value
     */
    public double getCoinScore() {
        return coinScore;
    }

    /**
     * Sets the coin score to the value given
     * @param score new coin score
     */
    public void setCoinScore(double score) {
        this.coinScore = score;
    }

    /**
     * Returns the initial damage by enemy
     * @return initial damage by enemy
     */
    public double getInitialEnemy() {
        return INITIAL_ENEMY_DAMAGE;
    }

    /**
     * Returns the initial score of a coin
     * @return initial score of a coin
     */
    public double getInitialCoin() {
        return INITIAL_COIN_SCORE;
    }

    /**
     * Sets the damage score to the damage value given
     * @param damage new damage score
     */
    public void setEnemyDamage(double damage) {
        this.enemyDamage = damage;
    }

    /**
     * Returns whether the player is down.
     *
     * @return true if the player is down, false if not
     */
    public boolean getPlayerDown() {
        return playerDown;
    }

    /**
     * Initializes common entities for all levels
     */
    public void initializeEntities() {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");

        // Goes through every data in CSV file and initialize each entity
        for (String[] entry : entityData) {
            double radius;
            String entityName = entry[0];
            double x = Double.parseDouble(entry[1]);
            double y = Double.parseDouble(entry[2]);
            String imagePath;

            // Each entity created is specific to each type
            switch (entityName) {
                case "PLATFORM":
                    imagePath = game_props.getProperty("gameObjects.platform.image");
                    platformWidth = x;
                    platform = new Platform(imagePath, x, y);
                    break;
                case "PLAYER":
                    imagePath = game_props.getProperty("gameObjects.player.imageRight");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"));
                    double health = Double.parseDouble(game_props.getProperty("gameObjects.player.health"));
                    player = new Player(imagePath, x, y, radius, y, health);
                    break;
                case "COIN":
                    imagePath = game_props.getProperty("gameObjects.coin.image");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.coin.radius"));
                    Coin newCoin = new Coin(imagePath, x, y, radius);
                    coins.add(newCoin);
                    break;
                case "ENEMY":
                    imagePath = game_props.getProperty("gameObjects.enemy.image");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.enemy.radius"));
                    Enemy newEnemy = new Enemy(imagePath, x, y, radius, game_props);
                    enemies.add(newEnemy);
                    break;
                case "END_FLAG":
                    imagePath = game_props.getProperty("gameObjects.endFlag.image");
                    radius = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius"));
                    endFlag= new EndFlag(imagePath, x, y, radius);
                    break;
            }
        }
    }

    /**
     * Renders the common entities for all levels
     */
    public void renderCommonEntities() {
        platform.draw();
        endFlag.draw();
        player.draw();

        // Draw all the enemies
        for (Enemy enemy : enemies) {
            enemy.draw();
        }

        // Draw all the coins
        for (Coin coin : coins) {
            coin.draw();
        }
    }

    /**
     * Starts the game for the level, handles all entity interactions
     * @param input input received from the user
     */
    public void startGame(Input input) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        SCORE_FONT.drawString("Score " + score, SCORE_X, SCORE_Y);
        HEALTH_FONT.drawString("Health " + (int)(player.getHealth() * 100), HEALTH_X, HEALTH_Y);

        renderCommonEntities();
        player.updatePlayer(input);
        checkCommonCollision(player);

        // Player exits the screen once health reaches zero
        if (playerDown) {
            player.playerDown();
        }

        for (Coin coin : coins) {
            coin.coinUpdate();
        }

        for (Enemy enemy: enemies) {
            enemy.updateRandom();
        }

        // Move the entities when the left key is pressed
        if (input.wasPressed(Keys.LEFT) || (input.isDown(Keys.LEFT))) {
            leftMove(game_props);
        }

        // Move the entities when right key is pressed
        if ((input.wasPressed(Keys.RIGHT)) || (input.isDown(Keys.RIGHT))) {
            rightMove();
        }
    }

    /**
     * Moves entities to the right when the left key is pressed.
     * @param game_props properties file with key values
     */
    private void leftMove(Properties game_props) {

        // Left image of player is drawn when left key is pressed
        String imagePathLeft = game_props.getProperty("gameObjects.player.imageLeft");
        Image imageLeft = new Image(imagePathLeft);
        player.drawLeft(imageLeft);

        endFlag.move(END_FLAG_SPEED);

        if (platform.getPosition().x < platformWidth) {
            platform.move(PLATFORM_SPEED);
        }

        for (Enemy enemy : enemies) {
            enemy.move(ENEMY_SPEED);
        }

        for (Coin coin : coins) {
            coin.move(COIN_SPEED);
        }
    }

    /**
     * Move entities to the left when right key is pressed
     */
    private void rightMove() {
        platform.move(-PLATFORM_SPEED);
        endFlag.move(-END_FLAG_SPEED);

        for (Enemy enemy : enemies) {
            enemy.move(-ENEMY_SPEED);
        }

        for (Coin coin : coins) {
            coin.move(-COIN_SPEED);
        }
    }

    /**
     * Checks for player collisions with coins and enemies.
     * @param player player entity
     */
    private void checkCommonCollision(Player player) {

        // Check collision with coins
        for (Coin coin: coins) {
            double dist = euclideanDistance(player, coin);
            double coinRange = player.getRadius() + coin.getRadius();

            if (isColliding(dist, coinRange)) {
                coin.moveUp();
                // Updates the score
                if (!coin.getHasCollided()) {
                    score += coinScore;
                    coin.setHasCollided(true);
                }
            }
        }

        // Check collision with enemy
        for (Enemy enemy : enemies) {
            double dist = euclideanDistance(player, enemy);
            double enemyRange = player.getRadius() + enemy.getRadius();

            if (isColliding(dist, enemyRange)) {
                // Updates the health
                if (!enemy.getHasDamaged()) {
                    player.setHealth(player.getHealth() - enemyDamage);
                    if (player.isDead()) {
                        player.dead();
                    }
                    enemy.setHasDamaged(true);
                }
            }
        }
    }

    /**
     * Checks if win conditions have been satisfied
     * @return true if player has collided with end flag, false if not
     */
    public boolean checkWinCondition() {
        double endFlagRange = player.getRadius() + endFlag.getRadius();

        // Check collision with end flag
        double dist = euclideanDistance(player, endFlag);
        return isColliding(dist, endFlagRange);
    }

    /**
     * Checks if lose conditions have been satisfied
     * @return true if the player is off-screen, false if not
     */
    public boolean checkLoseCondition() {
        return player.offScreen();
    }

    /**
     * Checks whether entities are close enough to be colliding.
     * @param dist  distance between the entities
     * @param range collision range
     * @return true if the entities are colliding, false if not
     */
    public static boolean isColliding(double dist, double range) {
        return dist <= range;
    }

    /**
     * Calculates the Euclidean distance between two entities
     * @param entity1 first entity
     * @param entity2 second entity
     * @return Euclidean distance between the two entities
     */
    public static double euclideanDistance(Entity entity1, Entity entity2) {
        double xDiff = entity1.getPosition().x - entity2.getPosition().x;
        double yDiff = entity1.getPosition().y - entity2.getPosition().y;
        return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

}
