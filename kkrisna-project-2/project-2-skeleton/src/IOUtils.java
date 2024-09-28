import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class that contains methods to read a CSV file and a properties file.
 * You may edit this as you wish.
 */
public class IOUtils {

    /***
     * Method that reads a CSV file and return a 2D String array
     * @param csvFile path to the CSV file
     * @return String ArrayList
     */
    public static List<String[]> readCsv(String csvFile) {
        List<String[]> entityRecords = new ArrayList<>();

        // Skeleton code taken from lecture 8, slide 35
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            String text;
            while ((text = br.readLine()) != null) {

                // Splits each text with a comma and add to array list
                String[] cells = text.split(",");
                entityRecords.add(cells);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return entityRecords;
    }

    /***
     * Method that reads a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }
}
