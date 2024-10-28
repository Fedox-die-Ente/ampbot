package ovh.fedox.ampbot.commands;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.commands.types.AMPCommandData;
import ovh.fedox.ampbot.commands.types.AMPCommandOption;
import ovh.fedox.ampbot.core.AMPClient;
import ovh.fedox.ampbot.core.AMPTranslation;

import java.util.Set;
import java.util.stream.Collectors;

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

        // Add options
        for (AMPCommandOption option : cmd.getOptions()) {
            String optionName = translator.getTranslation(option.name(), defaultLang);
            String optionDescription = translator.getTranslation(option.description(), defaultLang);

            if (!isValidDiscordName(optionName)) {
                logger.warn("Invalid option name for '{}' in language '{}'. Using fallback.", option.name(), defaultLang);
                optionName = option.name(); // Fallback to the key itself
            }

            if (optionDescription == null || optionDescription.isEmpty()) {
                logger.warn("Missing description for option '{}' in language '{}'. Using fallback.", option.name(), defaultLang);
                optionDescription = "No description available";
            }

            OptionData optionData = new OptionData(option.type(), optionName, optionDescription, option.required());
            jdaCommand.addOptions(optionData);
        }

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

                    // Localize options
                    for (AMPCommandOption option : cmd.getOptions()) {
                        String localizedOptionName = translator.getTranslation(option.name(), languageCode);
                        String localizedOptionDescription = translator.getTranslation(option.description(), languageCode);

                        if (!isValidDiscordName(localizedOptionName)) {
                            logger.warn("Invalid localized option name for '{}' in language '{}'. Skipping localization.", option.name(), languageCode);
                            continue;
                        }

                        if (localizedOptionDescription == null || localizedOptionDescription.isEmpty()) {
                            logger.warn("Missing localized description for option '{}' in language '{}'. Skipping localization.", option.name(), languageCode);
                            continue;
                        }

                        jdaCommand.getOptions().stream()
                                .filter(o -> o.getName().equals(translator.getTranslation(option.name(), defaultLang)))
                                .findFirst()
                                .ifPresent(o -> {
                                    o.setNameLocalization(locale, localizedOptionName);
                                    o.setDescriptionLocalization(locale, localizedOptionDescription);
                                });
                    }
                } else {
                    logger.warn("No mapping found for language code: " + languageCode);
                }
            }
        }

        return jdaCommand;
    }

    private boolean isValidDiscordName(String name) {
        return name != null && name.matches("^[\\w-]{1,32}$");
    }
}