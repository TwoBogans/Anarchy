package au.twobeetwotee.anarchy.utils.config;

import java.util.List;

public final class Motds {
    public static boolean ENABLED;
    public static String PREFIX;
    public static String SUFFIX;
    public static int WRAP;
    public static List<String> MOTDS;
    public static List<String> NAMES;

    private Motds() {
    }

    public static void reload() {
        ConfigUtil.reloadConfigFile("motds.yml", Motds.class);
    }
}
