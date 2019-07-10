package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AntiCrashCodeListeners implements Listener {

    private UnioProtections plugin;
    private char BAD_CHARACTER = '\u0307';

    public AntiCrashCodeListeners(UnioProtections plugin) {
        this.plugin = plugin;
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
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                if (containsBadCharacter(event.getCurrentItem().getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getMessage("antiCrashCode.cantUseCrashCode").replaceAll("%s", String.valueOf(BAD_CHARACTER)));
                }
            }
        }
    }
}
