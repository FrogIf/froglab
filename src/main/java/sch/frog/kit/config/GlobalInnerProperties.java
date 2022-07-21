package sch.frog.kit.config;

import java.io.InputStream;
import java.util.Properties;

public class GlobalInnerProperties {
    private final static Properties properties = new Properties();

    static {
        try(
                InputStream inputStream = GlobalInnerProperties.class.getClassLoader()
                        .getResourceAsStream("app-version.info")
        ){
            properties.load(inputStream);
        } catch (Exception e) {
            // TODO catch exception
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue){
        return properties.getProperty(key, defaultValue);
    }
}
