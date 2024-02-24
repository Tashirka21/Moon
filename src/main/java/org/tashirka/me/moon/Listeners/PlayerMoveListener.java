package org.tashirka.me.moon.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class PlayerMoveListener implements Listener {
    private Map<Player, BukkitRunnable> playerTasks;

    public PlayerMoveListener(Map<Player, BukkitRunnable> playerTasks) {
        this.playerTasks = playerTasks;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playerTasks.containsKey(player)) {
            return;
        }

        Location from = event.getFrom();
        player.teleport(from);
    }
}