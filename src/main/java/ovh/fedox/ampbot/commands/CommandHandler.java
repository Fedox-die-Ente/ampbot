package ovh.fedox.ampbot.commands;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.commands.models.AMPCommandData;
import ovh.fedox.ampbot.core.AMPClient;
import ovh.fedox.ampbot.helpers.AMPTranslation;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 5:02 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private final Set<AMPCommandData> commandDataSet;
    private final AMPClient bot;

    public CommandHandler(AMPClient client, Set<AMPCommandData> commandDataSet) {
        this.bot = client;
        this.commandDataSet = commandDataSet;
    }

    public void registerCommands() {
        logger.info("Registering commands...");

        try {
            Set<CommandData> jdaCommands = commandDataSet.stream()
                    .map(this::createLocalizedCommandData)
                    .collect(Collectors.toSet());

            bot.getJda().updateCommands().addCommands(jdaCommands).queue(
                    _ -> logger.info("Global commands registered successfully"),
                    error -> logger.error("An error occurred while registering global commands: " + error.getMessage())
            );
        } catch (Exception e) {
            logger.error("An error occurred while registering global commands", e);
        }
    }

    private CommandData createLocalizedCommandData(AMPCommandData cmd) {
        AMPTranslation translator = bot.getTranslator();
        String defaultLang = "en-US";

        String commandName = translator.getTranslation(cmd.getName(), defaultLang);
        String description = translator.getTranslation(cmd.getDescription(), defaultLang);

        SlashCommandData jdaCommand = Commands.slash(commandName, description);

        for (String languageCode : translator.getAvailableLanguages()) {
            if (!languageCode.equals(defaultLang)) {
                DiscordLocale locale = DiscordLocale.from(languageCode);
                if (locale != DiscordLocale.UNKNOWN) {
                    String localizedName = translator.getTranslation(cmd.getName(), languageCode);
                    String localizedDescription = translator.getTranslation(cmd.getDescription(), languageCode);

                    if (!localizedName.equals(commandName)) {
                        jdaCommand.setNameLocalization(locale, localizedName);
                    }
                    if (!localizedDescription.equals(description)) {
                        jdaCommand.setDescriptionLocalization(locale, localizedDescription);
                    }
                } else {
                    logger.warn("No mapping found for language code: " + languageCode);
                }
            }
        }

        return jdaCommand;
    }

}
