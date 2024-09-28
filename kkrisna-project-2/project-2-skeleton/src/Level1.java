import java.util.List;
import java.util.Properties;

/**
 * Represents first level of the game
 */
public class Level1 extends Level {

    /**
     * The constructor for level one object
     * @param game_props properties related to coordinates, image filenames and other key values
     * @param message_props stores the message strings
     */
    public Level1(Properties game_props, Properties message_props) {
        super(game_props, message_props);

        // Extract the entities data
        List<String[]> data = IOUtils.readCsv(game_props.getProperty("level1File"));
        assert data != null;
        this.setEntityData(data);
    }
}
