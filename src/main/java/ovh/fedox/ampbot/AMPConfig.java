package ovh.fedox.ampbot;

import lombok.Getter;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 3:03 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Getter
public enum AMPConfig {

    BOT_TOKEN("general.BOT_TOKEN"),
    MONGODB_URI("database.MONGODB_URI"),
    DATABASE_NAME("database.DATABASE_NAME");

    private final String configPath;

    AMPConfig(String configPath) {
        this.configPath = configPath;
    }

}
