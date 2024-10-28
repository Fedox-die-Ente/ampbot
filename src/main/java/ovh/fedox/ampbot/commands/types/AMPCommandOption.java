package ovh.fedox.ampbot.commands.types;

import net.dv8tion.jda.api.interactions.commands.OptionType;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 9:27 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public record AMPCommandOption(String name, String description, OptionType type, boolean required) {
}