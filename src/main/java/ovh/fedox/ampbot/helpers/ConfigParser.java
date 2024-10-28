package ovh.fedox.ampbot.helpers;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.AMPConfig;

import java.io.File;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 3:02 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class ConfigParser {
    private final Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    private final File configFile;
    private final Toml tomlConfig;

    @Getter
    private boolean isLoaded;

    public ConfigParser() {
        isLoaded = false;
        logger.info("Loading config file...");

        configFile = new File(ConfigParser.class.getClassLoader().getResource("config.toml").getFile());

        if (!configFile.exists()) {
            logger.error("Config file not found");
            System.exit(1);
        }

        tomlConfig = new Toml().read(configFile);

        logger.info("Config file loaded successfully");
        isLoaded = true;
    }

    public String getString(AMPConfig config) {
        return tomlConfig.getString(config.getConfigPath());
    }

    public int getInt(AMPConfig config) {
        return tomlConfig.getLong(config.getConfigPath()).intValue();
    }

    public boolean getBoolean(AMPConfig config) {
        return tomlConfig.getBoolean(config.getConfigPath());
    }

    public double getDouble(AMPConfig config) {
        return tomlConfig.getDouble(config.getConfigPath());
    }

    public long getLong(AMPConfig config) {
        return tomlConfig.getLong(config.getConfigPath());
    }

    public Toml getToml() {
        return tomlConfig;
    }

}
