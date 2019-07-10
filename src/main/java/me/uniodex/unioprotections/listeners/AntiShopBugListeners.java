package me.uniodex.unioprotections.listeners;

import com.snowgears.shop.event.PlayerCreateShopEvent;
import me.crafter.mc.lockettepro.LocketteProAPI;
import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiShopBugListeners implements Listener {

    private UnioProtections plugin;

    public AntiShopBugListeners(UnioProtections plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreateShop(PlayerCreateShopEvent event) {
        Player player = event.getPlayer();
        Block chest = event.getShop().getChestLocation().getBlock();
        if (LocketteProAPI.isProtected(chest)) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessage("antiShopBug.cantMakeShopsInLockedBlocks"));
        }
    }
}
