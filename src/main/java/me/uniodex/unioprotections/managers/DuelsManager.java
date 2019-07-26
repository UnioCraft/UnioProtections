package me.uniodex.unioprotections.managers;

import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.entity.Player;

public class DuelsManager {

    private UnioProtections plugin;

    public DuelsManager(UnioProtections plugin) {
        this.plugin = plugin;
    }

    public boolean canJoinDuel(Player player) {
        return (!(plugin.getCheckManager().getObsidianAuctionsManager().inAuction(player) || plugin.getCheckManager().getObsidianAuctionsManager().isBidding(player) || plugin.getCheckManager().getObsidianAuctionsManager().inQueue(player)));
    }

}
