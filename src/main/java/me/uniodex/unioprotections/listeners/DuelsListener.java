package me.uniodex.unioprotections.listeners;

import me.realized.duels.api.event.request.RequestAcceptEvent;
import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelsListener implements Listener {

    private UnioProtections plugin;

    public DuelsListener(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRequestAccept(RequestAcceptEvent event) {
        Player source = event.getSource();
        Player target = event.getTarget();

        if (!plugin.getCheckManager().getDuelsManager().canJoinDuel(source)) {
            source.sendMessage(plugin.getMessage("denyDuelOnAuction.inAuction").replaceAll("%player%", source.getName()));
            target.sendMessage(plugin.getMessage("denyDuelOnAuction.inAuction").replaceAll("%player%", source.getName()));
            event.setCancelled(true);
            return;
        }

        if (!plugin.getCheckManager().getDuelsManager().canJoinDuel(target)) {
            source.sendMessage(plugin.getMessage("denyDuelOnAuction.inAuction").replaceAll("%player%", target.getName()));
            target.sendMessage(plugin.getMessage("denyDuelOnAuction.inAuction").replaceAll("%player%", target.getName()));
            event.setCancelled(true);
            return;
        }
    }
}
