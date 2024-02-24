package org.tashirka.me.moon;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.tashirka.me.moon.Listeners.PlayerMoveListener;

import java.util.HashMap;
import java.util.Map;

public class MoonCommand implements CommandExecutor {

    private Map<Player, Double> playerSpeeds = new HashMap<>();
    private Map<Player, BukkitRunnable> playerTasks = new HashMap<>();
    private Moon plugin;

    public MoonCommand(Moon plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(playerTasks), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Вы не можете использовать эту команду, находясь в консоли!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("moon.reload")) {
                    Moon.getInstance().reloadConfig();
                    sender.sendMessage("Конфиг плагина Moon перезагружен!");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("stop")) {
                cancelPlayerTask(player);
                sender.sendMessage("Ивент остановлен!");
                return true;
            }
        }

        sender.sendMessage("Ивент запущен!");
        double startSpeed = Moon.getInstance().getConfig().getDouble("startspeed");
        playerSpeeds.put(player, 0.0);
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                double speed = playerSpeeds.get(player);
                if(playerTasks.containsKey(player)) {
                    speed += startSpeed;
                    speed *= 0.1;
                    double angle = (double) System.currentTimeMillis() / 1000 * Math.PI; // Вычисление угла на основе времени
                    double x = Math.cos(angle) * Moon.getInstance().getConfig().getDouble("radius");
                    double z = Math.sin(angle) * Moon.getInstance().getConfig().getDouble("radius");

                    player.setVelocity(new Vector(x, player.getVelocity().getY(), z));

                    playerSpeeds.put(player, speed);
                }
            }
        };
        playerTasks.put(player, task);
        task.runTaskTimer(this.plugin, 0, 1);

        return true;
    }

    private void cancelPlayerTask(Player player) {
        BukkitRunnable task = playerTasks.get(player);
        if (task != null) {
            task.cancel();
            playerTasks.remove(player); // Удаление задачи из коллекции playerTasks
            player.sendMessage("Ивент остановлен!");
        } else {
            player.sendMessage("Ивент не был запущен!");
        }
    }
}

