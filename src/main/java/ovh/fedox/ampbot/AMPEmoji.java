package ovh.fedox.ampbot;

import lombok.Getter;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 4:37 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Getter
public enum AMPEmoji {

    AMPBOT("AMPBOT");

    private final String emojiPath;

    AMPEmoji(String emojiPath) {
        this.emojiPath = emojiPath;
    }

}
