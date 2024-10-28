package ovh.fedox.ampbot.helpers;

import ovh.fedox.ampbot.AMPConfig;
import ovh.fedox.ampbot.core.AMPClient;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 2:30 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class AMPLoader {

    private final AMPClient bot;

    public AMPLoader(AMPClient bot) {
        this.bot = bot;
    }

    public void loadCommands() {
        System.out.println("Loading commands...");
        System.out.println(bot.getConfig().getString(AMPConfig.BOT_TOKEN));
    }

}
