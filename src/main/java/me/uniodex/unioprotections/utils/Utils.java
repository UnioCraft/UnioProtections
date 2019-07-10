package me.uniodex.unioprotections.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String colorizeMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("%hataPrefix%", UnioProtections.hataPrefix).replaceAll("%bilgiPrefix%", UnioProtections.bilgiPrefix).replaceAll("%dikkatPrefix%", UnioProtections.dikkatPrefix).replaceAll("%prefix%", UnioProtections.bilgiPrefix));
    }

    public static boolean isLocationInRegion(WorldGuardPlugin worldGuard, String regionName, Location location) {
        List<String> regionIds = new ArrayList<>();
        RegionManager regionManager = worldGuard.getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);

        for (ProtectedRegion region : regionsAtLocation) {
            regionIds.add(region.getId());
        }

        return regionIds.contains(regionName);
    }

    public static boolean isEntityLimitReached(Entity entity, int limit, int range) {
        int count = 0;
        for (Entity nearbyEntity : entity.getNearbyEntities(range, 255.0D, range)) {
            if (nearbyEntity.getType().equals(entity.getType())) {
                count++;
            }
        }
        return count > limit;
    }

    public static boolean isItemNamed(ItemStack item) {
        if (item == null) return false;
        if (item.getItemMeta() == null) return false;
        if (item.getItemMeta().getDisplayName() == null) return false;
        return true;
    }
}
