package config;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Singleton
public class Configuration {
    private final Properties p;

    private Configuration() {
        Path p1 = Paths.get("src/main/resources/mysql-properties.xml");
        p = new Properties();
        try {
            InputStream propertiesStream = Files.newInputStream(p1);
            p.loadFromXML(propertiesStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return p.getProperty(key);
    }

}
