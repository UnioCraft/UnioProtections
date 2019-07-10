package me.uniodex.unioprotections.commands;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdUnioprotections implements CommandExecutor {

    private UnioProtections plugin;

    public CmdUnioprotections(UnioProtections plugin) {
        this.plugin = plugin;
        plugin.getCommand("unioprotections").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("unioprotections.reload")) {
                    plugin.reload();
                    sender.sendMessage(plugin.getMessage("plugin.configReloaded"));
                } else {
                    sender.sendMessage(plugin.getMessage("plugin.noPermission"));
                }
            }
        }
        return true;
    }
}
