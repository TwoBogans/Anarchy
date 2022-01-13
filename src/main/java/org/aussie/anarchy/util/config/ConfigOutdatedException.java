package org.aussie.anarchy.util.config;

public class ConfigOutdatedException {
    public ConfigOutdatedException() {
    }

    public static String getException(String message, String config) {
        return message + " is missing in " + config + "; Please remove the old config and restart server to get the new config.";
    }
}
