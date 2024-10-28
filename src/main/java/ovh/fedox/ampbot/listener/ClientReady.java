package ovh.fedox.ampbot.listener;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 10:06 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class ClientReady extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientReady.class);

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("=====================================");
        logger.info("Logged in as " + event.getJDA().getSelfUser().getAsTag());
        logger.info("Guilds: " + event.getJDA().getGuilds().size());
        logger.info("=====================================");
    }
    
}
