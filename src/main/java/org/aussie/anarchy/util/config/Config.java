package org.aussie.anarchy.util.config;

import java.util.List;

public final class Config {
    public static boolean ANTINETHERROOF;
    public static boolean ANTIINSTANTMINE;
    public static boolean ANTIBEDROCKHOLES;
    public static boolean ANTIBOATFLY;
    public static boolean ANTIELYTRAFLY;
    public static boolean ANTIPROJECTILEVELOCITY;
    public static boolean ANTIDUPE;
    public static boolean ANTIEXPLOITS;
    public static boolean ANTIGODMODE;
    public static boolean ANTILAG;
    public static boolean ANTILIQUIDLAG;
    public static boolean ANTINOCOM;
    public static boolean ANTIPACKETFLY;
    public static boolean ANTIREDSTONE;
    public static boolean FREEZECHUNK;
    public static boolean ANTIWITHERLAG;
    public static boolean RANDOMSPAWNENABLED;
    public static boolean TABENABLED;
    public static boolean NOGLOBALTHUNDER;
    public static boolean WITHERSOUND;
    public static boolean ANTICHUNKBAN;
    public static boolean VETCHECK;
    public static boolean WORLDSTATSINTB;
    public static boolean SPEEDLIMIT;
    public static int MAXDAMAGE;
    public static int CRYSTALDELAY;
    public static int MAXMINECARTS;
    public static int MAXFIREWORKS;
    public static int MAXNOCOMDIST;
    public static int MAXBOATFLYVL;
    public static int MAXELYTRAFLYVL;
    public static int MAXPACKETFLYVL;
    public static int MAXFALLINGBLOCKS;
    public static int FALLINGBLOCKSRANGE;
    public static int ANTIREDSTONETPS;
    public static int CHUNKCURRENTMAX;
    public static int PISTONCHUNKMAX;
    public static int ANTIWITHERTPS;
    public static int ANTIWITHERPLAYERS;
    public static int ANTIWITHERSKULLTICKS;
    public static int ANTIWITHERRANGE;
    public static int ANTILIQUIDLAGTPS;
    public static int ANTILIQUIDLAGRADIUS;
    public static int RANDOMSPAWNRADIUS;
    public static int ANTICHUNKBANMAX;
    public static int ANTICHUNKBANTIME;
    public static int WORLDSTATSTICKS;
    public static long VETTIME;
    public static long WORLDSTATSTIME;
    public static double SPEEDLIMITMAXBPS;
    public static String RANDOMSPAWNWORLD;
    public static List<String> CHUNKFREEZEMATERIALS;
    public static List<String> COMMANDSWHITELISTED;
    public static List<String> ADMINS;
    public static List<String> INSTANTMINEBLACKLIST;
    public static List<String> RANDOMSPAWNBLOCKS;
    public static List<String> RESTARTTIMES;
    public static List<String> RESTARTCOUNTDOWN;
    public static List<String> ANTICHUNKBANBLOCKS;
    public static List<String> FORCEVET;
    public static List<String> ANTIWITHERWORLDS;
    public static List<String> WORLDSTATSFILE;

    private Config() {
    }

    public static void reload() {
        ConfigUtil.reloadConfigFile("config.yml", Config.class);
    }

    public static void reloadAll() {
        reload();
        Messages.reload();
        Motds.reload();
    }
}