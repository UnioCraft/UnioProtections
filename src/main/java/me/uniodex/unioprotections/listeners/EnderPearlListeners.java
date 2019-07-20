package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderPearlListeners implements Listener {

    private UnioProtections plugin;

    public EnderPearlListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunchEPC(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ENDER_PEARL) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();

        if (player.hasPermission("uniofactions.epc.bypass")) return;

        if (plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().containsKey(player.getName())) {
            long start = plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().get(player.getName());
            if (System.currentTimeMillis() - start >= plugin.getCheckManager().getEnderPearlManager().getEnderPearlCooldown()) {
                plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().remove(player.getName());
            } else {
                event.setCancelled(true);
                return;
            }
        }

        plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().put(player.getName(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerInteractEPC(PlayerInteractEvent event) {
        if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.ENDER_PEARL) return;

        Player player = event.getPlayer();

        if (player.hasPermission("uniofactions.epc.bypass")) return;

        if (plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().containsKey(player.getName())) {
            long start = plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().get(player.getName());
            if (System.currentTimeMillis() - start >= plugin.getCheckManager().getEnderPearlManager().getEnderPearlCooldown()) {
                plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().remove(player.getName());
            } else {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(plugin.getMessage("enderPearlCooldown.youMustWait").replaceAll("%s", plugin.getCheckManager().getEnderPearlManager().getPearlCooldown(player.getName())));
            }
        }
    }
}
