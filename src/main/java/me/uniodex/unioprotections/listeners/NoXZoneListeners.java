package me.uniodex.unioprotections.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.managers.CheckManager.Check;
import me.uniodex.unioprotections.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class NoXZoneListeners implements Listener {

    private UnioProtections plugin;

    public NoXZoneListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (!plugin.getCheckManager().isCheckEnabled(Check.NOFLYZONE)) return;
        if (p.hasPermission("unioprotections.fly.bypass")) return;
        if (plugin.getWorldGuard() == null) return;

        List<String> regions = plugin.getCheckManager().getNoXZoneManager().getNoFlyZones();
        String world = event.getPlayer().getWorld().getName();
        double px = p.getLocation().getX();
        double py = p.getLocation().getY() + 1.0D;
        double pz = p.getLocation().getZ();
        if (p.isFlying()) {
            for (String region : regions) {
                String[] regionInfo = region.split(":");
                if ((regionInfo[0].equalsIgnoreCase(world)) &&
                        (plugin.getWorldGuard().getRegionManager(Bukkit.getWorld(world)).hasRegion(regionInfo[1]))) {
                    ProtectedRegion reg = plugin.getWorldGuard().getRegionManager(Bukkit.getWorld(world)).getRegion(regionInfo[1]);
                    if (reg.contains((int) px, (int) py, (int) pz)) {
                        for (double y = py; y >= 0.0D; y -= 1.0D) {
                            Block topBlock = p.getWorld().getBlockAt(new Location(p.getWorld(), px, y, pz));
                            Block bottomBlock = p.getWorld().getBlockAt(new Location(p.getWorld(), px, y - 1.0D, pz));
                            Block ground = p.getWorld().getBlockAt(new Location(p.getWorld(), px, y - 2.0D, pz));
                            if (((topBlock.isEmpty()) || (topBlock.isLiquid())) && ((bottomBlock.isEmpty()) || (bottomBlock.isLiquid())) &&
                                    (ground.getType().isSolid())) {
                                p.teleport(new Location(p.getWorld(), px, y, pz, p.getLocation().getYaw(), p.getLocation().getPitch()));
                                p.setFlying(false);
                                p.setAllowFlight(false);
                                p.sendMessage(plugin.getMessage("noFlyAndTeleportZones.youCantFlyHere"));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getCheckManager().isCheckEnabled(Check.NOTELEPORTZONE)) return;
        if (player.hasPermission("unioprotections.teleport.bypass")) return;
        if (plugin.getWorldGuard() == null) return;

        for (String region : plugin.getCheckManager().getNoXZoneManager().getNoTeleportZones()) {
            if (Utils.isLocationInRegion(plugin.getWorldGuard(), region, event.getTo())) {
                event.setCancelled(true);
                player.sendMessage(plugin.getMessage("noFlyAndTeleportZones.youCantTeleportThere"));
                break;
            }
        }
    }
}
