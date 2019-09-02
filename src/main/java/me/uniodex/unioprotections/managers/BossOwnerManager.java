package me.uniodex.unioprotections.managers;

import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.managers.ConfigManager.Config;
import me.uniodex.unioprotections.utils.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossOwnerManager {

    private UnioProtections plugin;
    private Map<UUID, String> bossOwners = new HashMap<>();

    public BossOwnerManager(UnioProtections plugin) {
        this.plugin = plugin;
        loadBossOwners();
        Bukkit.getScheduler().runTaskTimer(plugin, () -> saveBossOwners(), 6000L, 6000L);
    }

    public void addBossOwner(String player, UUID boss) {
        bossOwners.put(boss, player);
    }

    public void removeBossOwner(UUID boss) {
        bossOwners.remove(boss);
    }

    public String getBossOwner(UUID boss) {
        return bossOwners.get(boss);
    }

    public void loadBossOwners() {
        if (plugin.getConfigManager().getConfig(Config.BOSSOWNERS).getConfigurationSection("bossOwners") == null)
            return;

        for (String bossUuid : plugin.getConfigManager().getConfig(Config.BOSSOWNERS).getConfigurationSection("bossOwners").getKeys(false)) {
            UUID entityUuid = UUID.fromString(bossUuid);
            if (Utils.entityExist(entityUuid)) {
                bossOwners.put(entityUuid, plugin.getConfigManager().getConfig(Config.BOSSOWNERS).getString("bossOwners." + bossUuid));
            }
        }
    }

    public void saveBossOwners() {
        plugin.getConfigManager().getConfig(Config.BOSSOWNERS).set("bossOwners", null);


        for (UUID boss : bossOwners.keySet()) {
            plugin.getConfigManager().getConfig(Config.BOSSOWNERS).set("bossOwners." + boss.toString(), bossOwners.get(boss));
        }

        plugin.getConfigManager().saveConfig(Config.BOSSOWNERS);
    }
}
