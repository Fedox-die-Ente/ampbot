package ovh.fedox.ampbot.commands.impl;

import ovh.fedox.ampbot.commands.interfaces.AMPCommand;
import ovh.fedox.ampbot.commands.models.AMPCommandData;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 4:59 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class TestCommand implements AMPCommand {

    @Override
    public AMPCommandData getCommandData() {
        return new AMPCommandData("test_command", "commands.test.name", "commands.test.description");
    }

    @Override
    public void execute() {
        System.out.println("Test command executed");
    }
}
