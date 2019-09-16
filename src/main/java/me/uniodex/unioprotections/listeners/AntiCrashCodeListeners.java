package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class AntiCrashCodeListeners implements Listener {

    private UnioProtections plugin;
    private char BAD_CHARACTER = '\u0307';

    public AntiCrashCodeListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private boolean containsBadCharacter(String string) {
        return string.contains(String.valueOf(BAD_CHARACTER));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (containsBadCharacter(event.getMessage())) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessage("antiCrashCode.cantUseCrashCode").replaceAll("%s", String.valueOf(BAD_CHARACTER)));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (containsBadCharacter(event.getMessage())) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessage("antiCrashCode.cantUseCrashCode").replaceAll("%s", String.valueOf(BAD_CHARACTER)));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getType().equals(InventoryType.ANVIL)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                if (containsBadCharacter(event.getCurrentItem().getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getMessage("antiCrashCode.cantUseCrashCode").replaceAll("%s", String.valueOf(BAD_CHARACTER)));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();

        if (anvilInventory.getRenameText() != null && containsBadCharacter(anvilInventory.getRenameText())) {
            event.setResult(new ItemStack(Material.AIR));
            for (HumanEntity player : event.getViewers()) {
                player.sendMessage(plugin.getMessage("antiCrashCode.cantUseCrashCode").replaceAll("%s", String.valueOf(BAD_CHARACTER)));
            }
        }
    }
}
