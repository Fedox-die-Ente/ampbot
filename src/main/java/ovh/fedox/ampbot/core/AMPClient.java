package ovh.fedox.ampbot.core;

import com.mongodb.client.MongoClient;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.AMPConfig;
import ovh.fedox.ampbot.commands.types.AMPCommandData;
import ovh.fedox.ampbot.helpers.TomlParser;
import ovh.fedox.ampbot.listener.impl.ClientReady;

import java.util.Collection;
import java.util.List;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 2:35 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class AMPClient extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AMPClient.class);
    private final TomlParser configParser = new TomlParser("config.toml");
    private final TomlParser emojiParser = new TomlParser("emojis.toml");
    @Getter
    private JDA jda;

    @Getter
    @Setter
    private Collection<AMPCommandData> commands;

    @Getter
    private AMPTranslation translator;

    @Setter
    @Getter
    private MongoClient mongoClient;

    public AMPClient() {
        try {
            if (!configParser.isLoaded()) {
                logger.error("Config file not loaded");
                System.exit(1);
            }

            if (!emojiParser.isLoaded()) {
                logger.error("Emoji file not loaded");
                System.exit(1);
            }

            translator = new AMPTranslation();

            jda = JDABuilder.createDefault(configParser.getString(AMPConfig.BOT_TOKEN), getIntents()).setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.EMOJI, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS).setStatus(OnlineStatus.DO_NOT_DISTURB).build();

            logger.info("JDA instance created successfully");

            jda.addEventListener(new ClientReady());
        } catch (Exception e) {
            logger.error("Error while creating JDA instance", e);
        }
    }

    public TomlParser getConfig() {
        return configParser;
    }

    public TomlParser getEmoji() {
        return emojiParser;
    }

    private Collection<GatewayIntent> getIntents() {
        return List.of(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MODERATION,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.SCHEDULED_EVENTS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.MESSAGE_CONTENT);
    }

}
