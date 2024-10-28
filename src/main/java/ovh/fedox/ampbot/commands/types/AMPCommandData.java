package ovh.fedox.ampbot.commands.types;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;

import java.util.List;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/28/2024 5:00 PM
 */

@Getter
public class AMPCommandData {

    private final String id;
    private final String name;
    private final String description;
    private final Permission[] botPermissions;
    private final Permission[] userPermissions;
    private final boolean ownerOnly;
    private final boolean staffOnly;
    private final List<AMPCommandOption> options;

    private AMPCommandData(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.botPermissions = builder.botPermissions;
        this.userPermissions = builder.userPermissions;
        this.ownerOnly = builder.ownerOnly;
        this.staffOnly = builder.staffOnly;
        this.options = builder.options;
    }

    public static class Builder {
        private final String id;
        private final String name;
        private final String description;
        private Permission[] botPermissions = new Permission[0];
        private Permission[] userPermissions = new Permission[0];
        private boolean ownerOnly = false;
        private boolean staffOnly = false;
        private List<AMPCommandOption> options;

        public Builder(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Builder botPermissions(Permission... botPermissions) {
            this.botPermissions = botPermissions;
            return this;
        }

        public Builder userPermissions(Permission... userPermissions) {
            this.userPermissions = userPermissions;
            return this;
        }

        public Builder ownerOnly(boolean ownerOnly) {
            this.ownerOnly = ownerOnly;
            return this;
        }

        public Builder staffOnly(boolean staffOnly) {
            this.staffOnly = staffOnly;
            return this;
        }

        public Builder options(List<AMPCommandOption> options) {
            this.options = options;
            return this;
        }

        public AMPCommandData build() {
            return new AMPCommandData(this);
        }
    }
}
