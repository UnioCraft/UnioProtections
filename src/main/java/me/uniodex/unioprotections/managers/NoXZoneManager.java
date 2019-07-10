package me.uniodex.unioprotections.managers;

import lombok.Getter;
import me.uniodex.unioprotections.UnioProtections;

import java.util.ArrayList;
import java.util.List;

public class NoXZoneManager {

    private UnioProtections plugin;

    @Getter
    private List<String> noFlyZones = new ArrayList<>();
    @Getter
    private List<String> noTeleportZones = new ArrayList<>();

    public NoXZoneManager(UnioProtections plugin) {
        this.plugin = plugin;
        noFlyZones.addAll(plugin.getConfig().getStringList("settings.noFlyZones"));
        noTeleportZones.addAll(plugin.getConfig().getStringList("settings.noTeleportZones"));
    }

}
