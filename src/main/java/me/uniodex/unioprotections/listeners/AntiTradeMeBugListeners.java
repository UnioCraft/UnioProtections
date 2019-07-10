package me.uniodex.unioprotections.listeners;

import me.Zrips.TradeMe.Events.TradeFinishEvent;
import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.utils.Utils;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Level;

public class AntiTradeMeBugListeners implements Listener {

    private UnioProtections plugin;
    private char COLOR_CODE = '\u00A7';

    public AntiTradeMeBugListeners(UnioProtections plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoneyUpdate(UserBalanceUpdateEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTradeMe().getUtil().isTrading(player)) {
            player.closeInventory();
            player.sendMessage(plugin.getMessage("antiTradeMeBug.moneyHasChanged"));
            Bukkit.getLogger().log(Level.WARNING, plugin.getMessage("antiTradeMeBug.moneyHasChangedLog").replaceAll("%player%", player.getName()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (plugin.getTradeMe().getUtil().isTrading(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTradeMe().getUtil().isTrading(player)) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessage("antiTradeMeBug.youCantDropItemsWhileTrading"));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onTradeFinish(TradeFinishEvent event) {
        if (!plugin.getConfig().getBoolean("settings.disabledTradingPaper")) return;

        Player p1 = event.getPlayer1();
        Player p2 = event.getPlayer2();
        if (containsPaper(event.getP1trade().getTradedItemsList()) || containsPaper(event.getP2trade().getTradedItemsList())) {
            event.setCancelled(true);
            p1.sendMessage(plugin.getMessage("antiTradeMeBug.paperTradeCancelled"));
            p2.sendMessage(plugin.getMessage("antiTradeMeBug.paperTradeCancelled"));
        }
    }

    private boolean containsPaper(List<ItemStack> items) {
        boolean ignoreColored = plugin.getConfig().getBoolean("settings.allowTradingColoredNamedPapers");

        for (ItemStack item : items) {
            if (item.getType().equals(Material.PAPER)) {
                if (!ignoreColored) return true;
                if (Utils.isItemNamed(item) && item.getItemMeta().getDisplayName().contains(String.valueOf(COLOR_CODE))) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
