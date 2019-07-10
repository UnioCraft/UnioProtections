package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandProtectionListeners implements Listener {

    private UnioProtections plugin;

    public CommandProtectionListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("unioprotections.command.bypass")) return;

        List<String> allowedCommands = new ArrayList<>(plugin.getConfig().getStringList("settings.allowedCommands.player"));

        if (player.hasPermission("unioprotections.command.moderator")) {
            allowedCommands.addAll(plugin.getConfig().getStringList("settings.allowedCommands.moderator"));
        }

        String command = event.getMessage().split(" ")[0].replace("/", "").toLowerCase();
        if (allowedCommands.contains(command)) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(plugin.getMessage("onlyAllowListOfCommands.unknownCommand"));
    }
}
