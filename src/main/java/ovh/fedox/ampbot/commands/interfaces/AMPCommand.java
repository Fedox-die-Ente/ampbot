package ovh.fedox.ampbot.commands.interfaces;

import ovh.fedox.ampbot.commands.models.AMPCommandData;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 4:59 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public interface AMPCommand {

    AMPCommandData getCommandData();

    void execute();

}
