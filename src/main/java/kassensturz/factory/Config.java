package kassensturz.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Very simple property access
 */
public class Config {

    private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getProperty(String property) {
        return (String) properties.get(property);
    }

}
