package me.uniodex.unioprotections.managers;

import lombok.Getter;
import lombok.Setter;
import me.uniodex.unioprotections.UnioProtections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnderPearlManager {

    private UnioProtections plugin;

    @Getter
    private Map<String, Long> enderPearlUsers = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private long enderPearlCooldown;

    public EnderPearlManager(UnioProtections plugin) {
        this.plugin = plugin;
        enderPearlCooldown = plugin.getConfig().getLong("settings.enderPearlCooldown", 3) * 1000;
    }

    public String getPearlCooldown(String player) {
        double cooldown = Math.abs((plugin.getCheckManager().getEnderPearlManager().getEnderPearlUsers().get(player) + plugin.getCheckManager().getEnderPearlManager().getEnderPearlCooldown() - System.currentTimeMillis()) / 1000.0D);
        String asString = Double.toString(cooldown);
        String fullNumber = asString.split("\\.")[0];
        return fullNumber + "." + (asString.split("\\.")[1].length() > 2 ? asString.split("\\.")[1].substring(0, 2) : asString.split("\\.")[1]);
    }

}
