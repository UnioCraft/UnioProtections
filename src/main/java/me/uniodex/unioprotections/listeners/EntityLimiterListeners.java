package me.uniodex.unioprotections.listeners;

import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class EntityLimiterListeners implements Listener {

    private UnioProtections plugin;

    public EntityLimiterListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        SpawnReason spawnReason = event.getSpawnReason();
        int limit;
        switch (spawnReason) {
            case BREEDING:
            case EGG:
            case DISPENSE_EGG:
                limit = plugin.getCheckManager().getEntityLimiterManager().getEntityBreedingLimit();
                break;
            case NATURAL:
            case NETHER_PORTAL:
                limit = plugin.getCheckManager().getEntityLimiterManager().getEntityNaturalLimit();
                break;
            case SPAWNER:
                limit = plugin.getCheckManager().getEntityLimiterManager().getEntitySpawnerLimit();
                break;
            case SPAWNER_EGG:
                limit = plugin.getCheckManager().getEntityLimiterManager().getEntitySpawnEggLimit();
                break;
            default:
                return;
        }

        if (Utils.isEntityLimitReached(event.getEntity(), limit, plugin.getCheckManager().getEntityLimiterManager().getEntityLimitRange())) {
            event.setCancelled(true);
        }
    }
}
