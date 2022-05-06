package org.aussie.anarchy.util;

import org.bukkit.Chunk;
import org.bukkit.block.Sign;

import java.util.HashSet;
import java.util.Set;

public class SignUtil {

    public static void clearSigns(Chunk chunk) {
        SignUtil.getSigns(chunk).forEach(sign -> {
            for (int i = 0; i < 4; ++i) {
                sign.setLine(i, "");
            }
        });
    }

    public static Set<Sign> getSigns(Chunk chunk) {
        Set<Sign> signs = new HashSet<>();

        chunk.getTileEntities(block -> block instanceof Sign, false).forEach(
                blockState -> {
                    Sign sign = (Sign) blockState;

                    if (checkDate(sign.getLines())) {
                        signs.add(sign);
                    }
                }
        );

        return signs;
    }

    public static boolean checkDate(String[] lines) {
        String[] dates = new String[] {"2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

        for (String line : lines) {
            for (String date : dates) {
                if (line.contains(date) || line.equalsIgnoreCase(date)) {
                    return true;
                }
            }
        }
        return false;
    }

}
