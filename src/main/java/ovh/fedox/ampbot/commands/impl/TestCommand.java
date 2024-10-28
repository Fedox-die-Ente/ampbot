package ovh.fedox.ampbot.commands.impl;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import ovh.fedox.ampbot.commands.types.AMPCommand;
import ovh.fedox.ampbot.commands.types.AMPCommandData;
import ovh.fedox.ampbot.commands.types.AMPCommandOption;

import java.util.List;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 4:59 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class TestCommand implements AMPCommand {

    @Override
    public AMPCommandData getCommandData() {
        return new AMPCommandData.Builder("test", "commands.test.name", "commands.test.description")
                .options(List.of(new AMPCommandOption("commands.test.args.message.name", "commands.test.args.message.description", OptionType.STRING, false)))
                .build();
    }

    @Override
    public void execute() {
        System.out.println("Test command executed");
    }
}
