package net.daechler.performanceprotection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PerformanceProtection extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + getName() + " has been disabled!");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if ((damager instanceof Player && shouldCancelDamage((Player) damager)) ||
                (damaged instanceof Player && shouldCancelDamage((Player) damaged))) {
            event.setCancelled(true);

            if (damager instanceof Player) {
                ((Player) damager).sendMessage(ChatColor.RED + "You can't deal damage due to your ping or server performance.");
            }

            if (damaged instanceof Player) {
                ((Player) damaged).sendMessage(ChatColor.RED + "You can't receive damage due to your ping or server performance.");
            }
        }
    }

    private boolean shouldCancelDamage(Player player) {
        int ping = player.spigot().getPing();
        double tps = Bukkit.getTPS()[0];
        return ping > 300 || tps < 18;
    }
}
