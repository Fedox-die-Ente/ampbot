package ovh.fedox.ampbot.database.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/29/2024 7:57 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Guild {

    private long guildId;
    private String locale;

}
