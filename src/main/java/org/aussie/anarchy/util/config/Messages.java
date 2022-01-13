package org.aussie.anarchy.util.config;

import java.util.List;

public final class Messages {
    public static String KICK_MESSAGE;
    public static String JOIN_MOTD;
    public static String JOIN_MESSAGE;
    public static String JOIN_CLICK;
    public static String JOIN_HOVER;
    public static String QUIT_MESSAGE;
    public static String BAD_COMMAND;
    public static String USAGE_COMMAND;
    public static String PLAYER_NOT_ONLINE;
    public static String PLAYER_NOT_FOUND;
    public static String SET_GAMEMODE;
    public static String TOG_CON_MSGS;
    public static String RESTARTING_TIME;
    public static String RESTARTING_NOW;
    public static String TAB1DOMAIN;
    public static List<String> HELP_MESSAGE;
    public static List<String> QUEUE_MESSAGE;
    public static List<String> PLAYTIME_OFFLINE;
    public static List<String> PLAYTIME_ONLINE;
    public static List<String> HEADER;
    public static List<String> FOOTER;
    public static List<String> HEADER1;
    public static List<String> FOOTER1;
    public static List<String> WORLDSTATS;
    public static List<String> VETERAN_TRUE;
    public static List<String> VETERAN_FALSE;
    public static List<String> CONFIG_HELP;
    public static List<String> CONFIG_RELOADED;

    private Messages() {
    }

    public static void reload() {
        ConfigUtil.reloadConfigFile("messages.yml", Messages.class);
    }
}
