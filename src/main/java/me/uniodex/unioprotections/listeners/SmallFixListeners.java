package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.managers.CheckManager.Check;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SmallFixListeners implements Listener {

    private UnioProtections plugin;

    public SmallFixListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Leave vehicle on join
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getCheckManager().isCheckEnabled(Check.SMALLFIX_LEAVEVEHICLEONJOIN)) {
            if (player.getVehicle() != null) {
                player.leaveVehicle();
            }
        }
    }

    // Don't allow putting item frame to dispenser Start
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!plugin.getCheckManager().isCheckEnabled(Check.SMALLFIX_DISALLOWITEMFRAMEINDISPENSER)) return;

        if (event.getInventory().getType() == InventoryType.DISPENSER) {
            Player p = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if ((item != null) && (item.getType() == Material.ITEM_FRAME)) {
                event.setCancelled(true);
                p.sendMessage(plugin.getMessage("smallFixes.dispenserFix"));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDispense(BlockDispenseEvent event) {
        if (!plugin.getCheckManager().isCheckEnabled(Check.SMALLFIX_DISALLOWITEMFRAMEINDISPENSER)) return;

        if (event.getBlock().getType() == Material.ITEM_FRAME) {
            event.setCancelled(true);
        }
    }
    // Don't allow putting item frame to dispenser End
}
