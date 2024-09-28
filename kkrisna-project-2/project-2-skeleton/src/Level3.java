import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents the third level of the game
 */
public class Level3 extends Level2 {

    private EnemyBoss enemyBoss;
    private List<Fireball> playerfireballs;
    private List<Fireball> enemyfireballs;
    private final String FIREBALL_IMAGE_PATH;
    private final double FIREBALL_RADIUS;
    private final double FIREBALL_SPEED;
    private final Font BOSS_HEALTH_FONT;
    private final double BOSS_HEALTH_X, BOSS_HEALTH_Y;
    private final int HEALTH_SIZE;
    private final double FIREBALL_DAMAGE;
    private final double ENEMY_BOSS_SPEED;
    private final double BOSS_INITIAL_HEALTH;
    private double bossCurrentHealth;
    private int frameCounter = 0;
    private boolean bossAlive = true;

    /**
     * The constructor for level three object
     * @param game_props properties related to coordinates, image filenames and other key values
     * @param message_props stores the message strings
     */
    public Level3(Properties game_props, Properties message_props) {
        super(game_props, message_props);

        // Extract the entities data
        List<String[]> data = IOUtils.readCsv(game_props.getProperty("level3File"));
        assert data != null;
        this.setEntityData(data);

        // Initialize the properties of health message for the enemy boss
        HEALTH_SIZE = Integer.parseInt(game_props.getProperty("enemyBossHealth.fontSize"));
        BOSS_HEALTH_FONT = new Font(game_props.getProperty("font"), HEALTH_SIZE);
        this.BOSS_HEALTH_X = Double.parseDouble(game_props.getProperty("enemyBossHealth.x"));
        this.BOSS_HEALTH_Y = Double.parseDouble(game_props.getProperty("enemyBossHealth.y"));

        // Initialise entities
        this.playerfireballs = new ArrayList<>();
        this.enemyfireballs = new ArrayList<>();

        // Initialise specific properties
        FIREBALL_IMAGE_PATH = game_props.getProperty("gameObjects.fireball.image");
        FIREBALL_RADIUS = Double.parseDouble(game_props.getProperty("gameObjects.fireball.radius"));
        FIREBALL_DAMAGE = Double.parseDouble(game_props.getProperty("gameObjects.fireball.damageSize"));
        FIREBALL_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.fireball.speed"));
        ENEMY_BOSS_SPEED = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.speed"));
        BOSS_INITIAL_HEALTH = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.health"));
        bossCurrentHealth = BOSS_INITIAL_HEALTH;
    }

    /**
     * Initializes entities for level 3
     */
    @Override
    public void initializeEntities() {
        super.initializeEntities();
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");

        // Goes through every data in CSV file and initialize each entity
        for (String[] entry : getEntityData()) {
            double radius;
            String entityName = entry[0];
            double x = Double.parseDouble(entry[1]);
            double y = Double.parseDouble(entry[2]);
            String imagePath;

            // Each entity created is specific to each type
            if (entityName.equals("ENEMY_BOSS")) {
                imagePath = game_props.getProperty("gameObjects.enemyBoss.image");
                radius = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.radius"));
                enemyBoss = new EnemyBoss(imagePath, x, y, radius, game_props);
            }
        }
    }

    /**
     * Starts the game for level 3, handles all entity interactions
     * @param input input received from the user
     */
    @Override
    public void startGame(Input input) {
        super.startGame(input);

        // Draws the score for the enemy boss in red colour
        DrawOptions color = new DrawOptions();
        color.setBlendColour(255, 0, 0);
        BOSS_HEALTH_FONT.drawString("Health " + (int)(bossCurrentHealth * 100), BOSS_HEALTH_X, BOSS_HEALTH_Y, color);

        enemyBoss.draw();

        // Move enemy boss when left key is press
        if (input.wasPressed(Keys.LEFT) || (input.isDown(Keys.LEFT))) {
            enemyBoss.move(ENEMY_BOSS_SPEED);
        }

        // Move enemy boss when right key is pressed
        if ((input.wasPressed(Keys.RIGHT)) || (input.isDown(Keys.RIGHT))) {
            enemyBoss.move(-ENEMY_BOSS_SPEED);
        }

        for (Fireball fireball: playerfireballs) {
            fireball.draw();
            fireball.update();
        }

        for (Fireball fireball: enemyfireballs) {
            fireball.draw();
            fireball.update();
        }

        int shouldShoot = enemyBoss.chooseRandom();

        // If the boss is alive, shoot fireballs if player is within range and for every 100 frame
        if (bossAlive) {

            if (shouldShoot >= 1 && startShoot(enemyBoss, getPlayer())) {
                frameCounter++;
                if (frameCounter >= 100) {

                    // Determine the direction of shooting the fireball from enemy boss
                    int direction = shootDirection(enemyBoss, getPlayer());
                    Fireball currFireball = new Fireball(FIREBALL_IMAGE_PATH, enemyBoss.getPosition().x, enemyBoss.getPosition().y, FIREBALL_RADIUS, direction * FIREBALL_SPEED);
                    enemyfireballs.add(currFireball);
                    frameCounter = 0;
                }
            }
        }

        fireballDamage();

        // Shoot fireball from the player when S key is pressed and if boss is still alive
        if (input.wasPressed(Keys.S) && bossAlive) {
            if (startShoot(enemyBoss, getPlayer())) {

                // Determine the direction of shooting the fireball from the player
                // It has the opposite logic than the enemy boss
                int direction = -1 * shootDirection(enemyBoss, getPlayer());
                Fireball newFireball = new Fireball(FIREBALL_IMAGE_PATH, getPlayer().getPosition().x, getPlayer().getPosition().y, FIREBALL_RADIUS,direction * FIREBALL_SPEED);
                playerfireballs.add(newFireball);
            }
        }

        // If boss health reaches below 0, it dies
        if (bossCurrentHealth <= 0) {
            enemyBoss.setVerticalSpeed(2);
            enemyBoss.updateEnemy();
            bossAlive = false;
        }

        if (getPlayerDown()) {
            getPlayer().playerDown();
        }
    }

    /**
     * Based on the distance, determines whether enemy boss & player can start shooting
     * @param enemyBoss enemy boss entity
     * @param player player entity
     * @return true if distance between player and boss is within 500 pixels, false if not
     */
    private boolean startShoot(EnemyBoss enemyBoss, Player player) {
        return (Math.abs(enemyBoss.getPosition().x - player.getPosition().x) <= 500);
    }

    /**
     * Determines direction of shooting of fireball
     * @param enemyBoss enemy boss entity
     * @param player player entity
     * @return -1 if the player is on the left of enemy boss, 1 if on the right
     */
    private int shootDirection(EnemyBoss enemyBoss, Player player) {

        // If the player is on the left of the enemy, shoot to the left
        if (enemyBoss.getPosition().x - player.getPosition().x >= 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Handles collision calculations of fireball with player and boss
     */
    private void fireballDamage() {

        // Checks for collision for fireball from player
        List<Fireball> playerRemove = new ArrayList<>();
        for (Fireball fireball: playerfireballs) {
            double dist = euclideanDistance(enemyBoss, fireball);
            double enemyRange = enemyBoss.getRadius() + fireball.getRadius();
            if (isColliding(dist, enemyRange)) {
                bossCurrentHealth -= FIREBALL_DAMAGE;
                playerRemove.add(fireball);
            }
        }
        playerfireballs.removeAll(playerRemove);

        // Checks for collision for fireball from enemy
        List<Fireball> enemyRemove = new ArrayList<>();
        for (Fireball fireball: enemyfireballs) {
            double dist = euclideanDistance(getPlayer(), fireball);
            double playerRange = getPlayer().getRadius() + fireball.getRadius();
            if (isColliding(dist, playerRange)) {
                getPlayer().setHealth(getPlayer().getHealth() - FIREBALL_DAMAGE);
                enemyRemove.add(fireball);
                if (getPlayer().isDead()) {
                    getPlayer().dead();
                }
            }
        }
        enemyfireballs.removeAll(enemyRemove);
    }

    /**
     * Checks win condition for level 3
     * @return true if the player collides with end flag and boss is dead, false if not
     */
    @Override
    public boolean checkWinCondition() {

        double endFlagRange = getPlayer().getRadius() + getEndFlag().getRadius();

        // Check collision with end flag
        double dist = euclideanDistance(getPlayer(), getEndFlag());
        boolean collidesWithEndFlag = isColliding(dist, endFlagRange);

        return !bossAlive && collidesWithEndFlag;
    }
}
