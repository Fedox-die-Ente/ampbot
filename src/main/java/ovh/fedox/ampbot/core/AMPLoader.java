package ovh.fedox.ampbot.core;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.commands.CommandHandler;
import ovh.fedox.ampbot.commands.types.AMPCommand;
import ovh.fedox.ampbot.commands.types.AMPCommandData;

import java.util.HashSet;
import java.util.Set;

public class AMPLoader {

    private static final Logger logger = LoggerFactory.getLogger(AMPLoader.class);

    private final AMPClient bot;
    private final Set<AMPCommandData> commandDataSet;

    public AMPLoader(AMPClient bot) {
        this.bot = bot;
        this.commandDataSet = new HashSet<>();
    }

    public void loadCommands() {
        logger.info("Loading commands...");

        Reflections reflections = new Reflections("ovh.fedox.ampbot.commands.impl");

        Set<Class<? extends AMPCommand>> commandClasses = reflections.getSubTypesOf(AMPCommand.class);
        for (Class<? extends AMPCommand> commandClass : commandClasses) {
            try {
                AMPCommand commandInstance = commandClass.getDeclaredConstructor().newInstance();
                commandDataSet.add(commandInstance.getCommandData());
            } catch (Exception e) {
                logger.error("Error instantiating command: " + commandClass.getSimpleName(), e);
            }
        }

        bot.setCommands(commandDataSet);
        logger.info("Commands loaded successfully. Total commands: " + commandDataSet.size());

        CommandHandler commandHandler = new CommandHandler(bot, commandDataSet);
        commandHandler.registerCommands();
    }

    public void loadEvents() {
        logger.info("Loading events...");

        Reflections reflections = new Reflections("ovh.fedox.ampbot.listener");

        Set<Class<? extends ListenerAdapter>> eventClasses = reflections.getSubTypesOf(ListenerAdapter.class);
        for (Class<? extends ListenerAdapter> eventClass : eventClasses) {
            try {
                ListenerAdapter eventInstance = eventClass.getDeclaredConstructor().newInstance();
                bot.getJda().addEventListener(eventInstance);
            } catch (Exception e) {
                logger.error("Error instantiating event: " + eventClass.getSimpleName(), e);
            }
        }

        logger.info("Events loaded successfully. Total events: " + eventClasses.size());
    }


}