package me.uniodex.unioprotections.managers;

import com.flobi.floauction.Auction;
import com.flobi.floauction.AuctionScope;
import com.flobi.floauction.FloAuction;
import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.entity.Player;

public class ObsidianAuctionsManager {

    private UnioProtections plugin;

    public ObsidianAuctionsManager(UnioProtections plugin) {
        this.plugin = plugin;
    }

    public boolean inAuction(Player player) {
        return FloAuction.getPlayerAuction(player) != null;
    }

    public boolean isBidding(Player player) {
        if (AuctionScope.getPlayerScope(player).getActiveAuction() != null) {
            if (AuctionScope.getPlayerScope(player).getActiveAuction().getCurrentBid() != null) {
                return AuctionScope.getPlayerScope(player).getActiveAuction().getCurrentBid().getBidder().equalsIgnoreCase(player.getName());
            }
        }
        return false;
    }

    public boolean inQueue(Player player) {
        for (Auction auc : AuctionScope.getPlayerScope(player).getAuctionQueue()) {
            if (auc.getOwner().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

}
