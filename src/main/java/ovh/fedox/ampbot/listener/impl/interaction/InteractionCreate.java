package ovh.fedox.ampbot.listener.impl.interaction;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ovh.fedox.ampbot.AMPConfig;
import ovh.fedox.ampbot.core.AMPClient;
import ovh.fedox.ampbot.listener.types.AMPListener;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/29/2024 8:22 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class InteractionCreate implements AMPListener<SlashCommandInteractionEvent> {

    @Override
    public void dispatch(SlashCommandInteractionEvent event, AMPClient client) {
        System.out.println("Interaction created");

        // TODO: Test this
        event.reply(client.getConfig().getString(AMPConfig.DATABASE_NAME)).queue();
    }

}
