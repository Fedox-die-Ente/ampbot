package ovh.fedox.ampbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.core.AMPClient;
import ovh.fedox.ampbot.core.AMPLoader;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 2:29 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class AMPBot {
    private static final Logger logger = LoggerFactory.getLogger(AMPBot.class);

    private static AMPClient client;
    private static AMPLoader loader;

    public static void main(String[] args) {
        client = new AMPClient();
        loader = new AMPLoader(client);
        loader.loadDatabase();
        loader.loadCommands();
        loader.loadEvents();
    }

}
