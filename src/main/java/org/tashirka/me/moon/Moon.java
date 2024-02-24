package org.tashirka.me.moon;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public final class Moon extends JavaPlugin {
    private static Moon instance;

    private Map<Player, Double> playerSpeeds = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        MoonCommand moonCommandExecutor = new MoonCommand(this);
        getCommand("moon").setExecutor(moonCommandExecutor);
    }

    public static Moon getInstance() {
        return instance;
    }

}
