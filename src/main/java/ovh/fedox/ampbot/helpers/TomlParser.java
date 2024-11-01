package ovh.fedox.ampbot.helpers;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.AMPConfig;
import ovh.fedox.ampbot.AMPEmoji;

import java.io.File;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 3:02 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class TomlParser {

    private final Toml tomlConfig;

    @Getter
    private boolean isLoaded;

    public TomlParser(String fileName) {
        Logger logger = LoggerFactory.getLogger(TomlParser.class);

        logger.info("Loading {}...", fileName);
        isLoaded = false;

        File tomlFile = new File(TomlParser.class.getClassLoader().getResource(fileName).getFile());

        if (!tomlFile.exists()) {
            logger.error("Toml file not found ({})", fileName);
            System.exit(1);
        }

        tomlConfig = new Toml().read(tomlFile);

        logger.info("{} loaded successfully", fileName);
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

    public String getEmoji(AMPEmoji emoji) {
        return tomlConfig.getString(emoji.getEmojiPath());
    }

}
