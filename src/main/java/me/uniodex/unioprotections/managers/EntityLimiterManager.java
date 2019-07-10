package me.uniodex.unioprotections.managers;

import lombok.Getter;
import me.uniodex.unioprotections.UnioProtections;

public class EntityLimiterManager {

    private UnioProtections plugin;

    @Getter
    int entityLimitRange;
    @Getter
    int entityBreedingLimit;
    @Getter
    int entityNaturalLimit;
    @Getter
    int entitySpawnerLimit;
    @Getter
    int entitySpawnEggLimit;

    public EntityLimiterManager(UnioProtections plugin) {
        this.plugin = plugin;
        entityLimitRange = plugin.getConfig().getInt("settings.entityLimits.range");
        entityBreedingLimit = plugin.getConfig().getInt("settings.entityLimits.breeding");
        entityNaturalLimit = plugin.getConfig().getInt("settings.entityLimits.natural");
        entitySpawnerLimit = plugin.getConfig().getInt("settings.entityLimits.spawner");
        entitySpawnEggLimit = plugin.getConfig().getInt("settings.entityLimits.spawnegg");
    }

}
