package org.aussie.anarchy.hook.hooks;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SparkHook extends PluginHook<Anarchy> {
    public static final String NAME = "Spark";
    private Spark spark;

    public SparkHook(Anarchy plugin) {
        super(plugin, "Spark");
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        if (provider == null) {
            plugin.warn("Couldn't detect Spark");
        } else {
            this.spark = (Spark)provider.getProvider();
        }
    }

    public double getTPS(StatisticWindow.TicksPerSecond type) {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = this.spark.tps();
        return tps != null ? tps.poll(type) : Bukkit.getServer().getTPS()[0];
    }

    public double getTPS_10SEC() {
        return this.getTPS(StatisticWindow.TicksPerSecond.SECONDS_10);
    }

    public Spark getSpark() {
        return this.spark;
    }
}