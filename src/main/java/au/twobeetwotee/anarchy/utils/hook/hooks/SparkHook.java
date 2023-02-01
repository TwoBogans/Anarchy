package au.twobeetwotee.anarchy.utils.hook.hooks;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SparkHook extends PluginHook<AnarchyPatches> {
    public static final String NAME = "Spark";
    private Spark spark;

    public SparkHook(AnarchyPatches plugin) {
        super(plugin, "Spark");
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        if (provider == null) {
            plugin.warn("Couldn't detect Spark");
        } else {
            this.spark = provider.getProvider();
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