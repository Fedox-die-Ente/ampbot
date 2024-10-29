package ovh.fedox.ampbot.core;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.commands.CommandHandler;
import ovh.fedox.ampbot.commands.types.AMPCommand;
import ovh.fedox.ampbot.commands.types.AMPCommandData;
import ovh.fedox.ampbot.database.AMPDatabase;
import ovh.fedox.ampbot.listener.types.AMPListener;

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

        Set<Class<? extends AMPListener>> eventClasses = reflections.getSubTypesOf(AMPListener.class);
        for (Class<? extends AMPListener> eventClass : eventClasses) {
            try {
                AMPListener<?> eventInstance = eventClass.getDeclaredConstructor().newInstance();

                ListenerAdapter adapter = new ListenerAdapter() {
                    @Override
                    public void onGenericEvent(GenericEvent event) {
                        Class<?> listenerType = getListenerType(eventClass);
                        if (listenerType != null && listenerType.isAssignableFrom(event.getClass())) {
                            try {
                                eventClass.getMethod("dispatch", listenerType).invoke(eventInstance, event);
                            } catch (Exception e) {
                                logger.error("Error dispatching event: " + event.getClass().getSimpleName(), e);
                            }
                        }
                    }
                };

                bot.getJda().addEventListener(adapter);
                logger.info("Registered event: " + eventClass.getSimpleName());
            } catch (Exception e) {
                logger.error("Error instantiating event: " + eventClass.getSimpleName(), e);
            }
        }

        logger.info("Events loaded successfully. Total events: " + eventClasses.size());
    }

    private Class<?> getListenerType(Class<? extends AMPListener> listenerClass) {
        for (java.lang.reflect.Type genericInterface : listenerClass.getGenericInterfaces()) {
            if (genericInterface instanceof java.lang.reflect.ParameterizedType paramType) {
                if (paramType.getRawType() == AMPListener.class) {
                    return (Class<?>) paramType.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    public void loadDatabase() {
        AMPDatabase database = new AMPDatabase(bot);
        database.init();
    }

}