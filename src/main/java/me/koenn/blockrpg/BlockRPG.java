package me.koenn.blockrpg;

import me.koenn.blockrpg.commands.SetKingdomCommand;
import me.koenn.blockrpg.daynight.DayNightSystem;
import me.koenn.blockrpg.enviromental.EnvironmentalSystem;
import me.koenn.blockrpg.hydration.HydrationSystem;
import me.koenn.blockrpg.listeners.MobSpawnListener;
import me.koenn.blockrpg.util.OpLogger;
import me.koenn.blockrpg.vegitation.VegetationSystem;
import me.koenn.blockrpg.waterlava.WaterLavaSystem;
import me.koenn.core.command.CommandAPI;
import me.koenn.core.misc.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BlockRPG extends JavaPlugin {

    private static List<Timer> timers = new ArrayList<>();
    private static BlockRPG instance;

    public static BlockRPG getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        Bukkit.getPluginManager().registerEvents(new MobSpawnListener(), this);

        CommandAPI.registerCommand(new SetKingdomCommand(), this);

        HydrationSystem hydrationSystem = new HydrationSystem();
        Bukkit.getPluginManager().registerEvents(hydrationSystem, this);
        Timer hydrationTimer = new Timer(2, true, this);
        hydrationTimer.start(hydrationSystem);
        timers.add(hydrationTimer);

        WaterLavaSystem waterLavaSystem = new WaterLavaSystem();
        Bukkit.getPluginManager().registerEvents(waterLavaSystem, this);
        Timer waterLavaTimer = new Timer(20, true, this);
        waterLavaTimer.start(waterLavaSystem);
        timers.add(waterLavaTimer);

        VegetationSystem vegetationSystem = new VegetationSystem();
        Bukkit.getPluginManager().registerEvents(vegetationSystem, this);
        Timer vegetationTimer = new Timer(20, true, this);
        vegetationTimer.start(vegetationSystem);
        timers.add(vegetationTimer);

        Bukkit.getPluginManager().registerEvents(new EnvironmentalSystem(), this);
        Bukkit.getPluginManager().registerEvents(new DayNightSystem(), this);
        Bukkit.getPluginManager().registerEvents(new OpLogger(), this);
    }

    @Override
    public void onDisable() {
        timers.forEach(Timer::stop);

        if (OpLogger.writer != null) {
            try {
                OpLogger.writer.flush();
                OpLogger.writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
