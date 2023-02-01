package au.twobeetwotee.anarchy.utils;

import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.config.ConfigUtil;
import au.twobeetwotee.anarchy.utils.config.Messages;
import au.twobeetwotee.anarchy.utils.config.Motds;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public interface IAnarchy {
    default void processConfigs(File dataDirectory) {
        try {
            if (!dataDirectory.exists() && !dataDirectory.mkdir()) {
                return;
            }

            ConfigUtil.processConfig(dataDirectory, "config.yml", Config.class);
            ConfigUtil.processConfig(dataDirectory, "messages.yml", Messages.class);
            ConfigUtil.processConfig(dataDirectory, "motds.yml", Motds.class);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    default Logger logger() {
        return Logger.getLogger("AnarchyPatches");
    }

    default void error(String message) {
        this.logger().severe(message);
    }

    default void warn(String message) {
        this.logger().warning(message);
    }

    default void log(String message) {
        this.logger().info(message);
    }
}
