package org.aussie.anarchy.util;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.SparkHook;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.UUID;

public class Util {
    public Util() {
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static boolean isAdmin(String uuid) {
        return Config.ADMINS.contains(uuid);
    }

    public static boolean isAdmin(UUID uuid) {
        return isAdmin(uuid.toString());
    }

    public static boolean isAdmin(Player player) {
        return isAdmin(player.getUniqueId());
    }

    public static boolean isAdmin(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else {
            return sender instanceof Player && isAdmin((Player) sender);
        }
    }

    public static String locationToString(Location location) {
        return String.format("%s %s %s %s",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getWorld().getName()
        );
    }

    public static int countEntities(Entity[] e, EntityType type) {
        int count = 0;

        for (Entity entity : e) {
            if (entity.getType().equals(type)) {
                ++count;
            }
        }

        return count;
    }

    public static double getTPS() {
        try {
            return Anarchy.getHookManager().getHook(SparkHook.class).getTPS_10SEC();
        } catch (Exception var1) {
            return Bukkit.getServer().getTPS()[0];
        }
    }

    public static int countChunk(Chunk chunk, Material... material) {
        return getBlocksInChunk(chunk, material).size();
    }

    private static String getHostName(Player p) {
        InetSocketAddress server = p.getVirtualHost();
        return server == null ? "null" : server.getHostName();
    }

    public static boolean containsDomain(Player p, String... domains) {
        for (String domain : domains) {
            if (domain == null) {
                return false;
            }

            if (getHostName(p).contains(domain)) {
                return true;
            }
        }

        return false;
    }

    public static HashSet<Block> getBlocksInChunk(Chunk chunk, Material... lookingFor) {
        HashSet<Block> locations = new HashSet<>();

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                for(int y = 0; y < 256; ++y) {
                    for (Material m : lookingFor) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() == m) {
                            locations.add(block);
                        }
                    }
                }
            }
        }

        return locations;
    }

    public static OptionalInt parseInt(String s) {
        if (s == null) {
            return OptionalInt.empty();
        } else {
            int result = 0;
            boolean negative = false;
            int i = 0;
            int len = s.length();
            int limit = -2147483647;
            if (len > 0) {
                char firstChar = s.charAt(0);
                if (firstChar < '0') {
                    if (firstChar == '-') {
                        negative = true;
                        limit = -2147483648;
                    } else if (firstChar != '+') {
                        return OptionalInt.empty();
                    }

                    if (len == 1) {
                        return OptionalInt.empty();
                    }

                    ++i;
                }

                int digit;
                for(int multmin = limit / 10; i < len; result -= digit) {
                    digit = Character.digit(s.charAt(i++), 10);
                    if (digit < 0) {
                        return OptionalInt.empty();
                    }

                    if (result < multmin) {
                        return OptionalInt.empty();
                    }

                    result *= 10;
                    if (result < limit + digit) {
                        return OptionalInt.empty();
                    }
                }

                return OptionalInt.of(negative ? result : -result);
            } else {
                return OptionalInt.empty();
            }
        }
    }
}