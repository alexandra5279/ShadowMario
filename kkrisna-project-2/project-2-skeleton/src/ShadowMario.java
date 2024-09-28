import bagel.*;

import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2024
 *
 * Please enter your name below
 * @Kiesha Lexandra Krisna
 *
 * This class holds the main class for the game
 * Handles initialisation and level transitions
 */
public class ShadowMario extends AbstractGame {

    private final Image BACKGROUND_IMAGE;
    private final Font TITLE_FONT;
    private final Font INSTRUCTION_FONT;
    private final String TITLE_MESSAGE;
    private final String INSTRUCTION_MESSAGE;
    private final double TITLE_X, TITLE_Y, INSTRUCTION_X, INSTRUCTION_Y;

    private final Font END_FONT;
    private final String WIN_MESSAGE, LOSE_MESSAGE;
    private final double END_X, END_Y;

    private boolean isGameStarted = false;
    private Level currentLevel;

    /**
     * The constructor for Shadow Mario
     * @param game_props properties related to coordinates, image filenames and other key values
     * @param message_props stores the message strings
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        final int titleSize, instructionSize, winSize;
        final double instructionLength;

        // Initialize the properties of title message
        this.TITLE_MESSAGE = message_props.getProperty("title");
        titleSize = Integer.parseInt(game_props.getProperty("title.fontSize"));
        this.TITLE_X = Double.parseDouble(game_props.getProperty("title.x"));
        this.TITLE_Y = Double.parseDouble(game_props.getProperty("title.y"));
        TITLE_FONT = new Font(game_props.getProperty("font"), titleSize);

        // Initialize the properties of instruction message
        this.INSTRUCTION_MESSAGE = message_props.getProperty("instruction");
        instructionSize = Integer.parseInt(game_props.getProperty("instruction.fontSize"));
        this.INSTRUCTION_Y = Double.parseDouble(game_props.getProperty("instruction.y"));
        INSTRUCTION_FONT = new Font(game_props.getProperty("font"), instructionSize);
        instructionLength = INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE);
        this.INSTRUCTION_X = (Double.parseDouble(game_props.getProperty("windowWidth")) / 2.0) - (instructionLength / 2.0);

        // Initialize properties of winning and losing message
        this.WIN_MESSAGE = message_props.getProperty("gameWon");
        this.LOSE_MESSAGE = message_props.getProperty("gameOver");
        winSize = Integer.parseInt(game_props.getProperty("message.fontSize"));
        END_FONT = new Font(game_props.getProperty("font"), winSize);
        this.END_X = this.INSTRUCTION_X; // centred horizontally
        this.END_Y = Double.parseDouble(game_props.getProperty("message.y"));
    }

    /**
     * The entry point for the program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update of the selected level.
     * Allows the game to exit when the escape key is pressed.
     * Handle screen navigation between levels and instruction pages here.
     * @param input the input received from the user
     */
    @Override
    protected void update(Input input) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!isGameStarted) {

            // Draw the title and instruction message
            TITLE_FONT.drawString(TITLE_MESSAGE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE, INSTRUCTION_X, INSTRUCTION_Y);

            // Start level one if key 1 is pressed
            if (input.wasPressed(Keys.NUM_1)){
                isGameStarted = true;
                currentLevel = new Level1(game_props, message_props);
                currentLevel.initializeEntities();
            }

            // Start level two if key 2 is pressed
            else if (input.wasPressed(Keys.NUM_2)){
                isGameStarted = true;
                currentLevel = new Level2(game_props, message_props);
                currentLevel.initializeEntities();
            }

            // Start level two if key 3 is pressed
            else if (input.wasPressed(Keys.NUM_3)){
                isGameStarted = true;
                currentLevel = new Level3(game_props, message_props);
                currentLevel.initializeEntities();
            }

        } else if (currentLevel.checkWinCondition()) {
            END_FONT.drawString(WIN_MESSAGE, END_X, END_Y);

            if (input.wasPressed(Keys.SPACE)) {
                isGameStarted = false;
            }

        } else if (currentLevel.checkLoseCondition()) {
            END_FONT.drawString(LOSE_MESSAGE, END_X, END_Y);

            if (input.wasPressed(Keys.SPACE)) {
                isGameStarted = false;
            }

        } else {
            currentLevel.startGame(input);
        }

        // Used to slow down the speed of the entities being drawn on screen
        // Code from Elliott Young on 6th April in ED Discussion
        try {
            Thread.sleep(1000 / 120); // arg is in milliseconds
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


