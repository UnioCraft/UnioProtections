package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class AnvilListeners implements Listener {

    private UnioProtections plugin;

    public AnvilListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        for (String disallowedItem : plugin.getCheckManager().getCustomItemsManager().getDisallowedItemNames()) {
            if (anvilInventory.getRenameText().toLowerCase().contains(disallowedItem)) {
                event.setResult(new ItemStack(Material.AIR));
                for (HumanEntity player : event.getViewers()) {
                    player.sendMessage(plugin.getMessage("disallowNamingFakeItems.youCantNameItemsSpecial"));
                }
                return;
            }
        }
    }
}
