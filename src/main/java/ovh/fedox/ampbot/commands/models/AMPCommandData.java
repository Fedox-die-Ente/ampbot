package ovh.fedox.ampbot.commands.models;

import lombok.Getter;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 5:00 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class AMPCommandData {

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String description;

    public AMPCommandData(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
