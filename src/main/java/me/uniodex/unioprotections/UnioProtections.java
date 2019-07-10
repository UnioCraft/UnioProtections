package me.uniodex.unioprotections;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.snowgears.shop.Shop;
import io.lumine.xikage.mythicmobs.MythicMobs;
import lombok.Getter;
import me.Zrips.TradeMe.TradeMe;
import me.crafter.mc.lockettepro.LockettePro;
import me.uniodex.unioprotections.commands.CmdUnioprotections;
import me.uniodex.unioprotections.managers.CheckManager;
import me.uniodex.unioprotections.managers.ConfigManager;
import me.uniodex.unioprotections.managers.ConfigManager.Config;
import me.uniodex.unioprotections.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class UnioProtections extends JavaPlugin {

    public static String hataPrefix;
    public static String dikkatPrefix;
    public static String bilgiPrefix;
    public static String consolePrefix;

    @Getter
    private ConfigManager configManager;
    @Getter
    private CheckManager checkManager;

    @Getter
    private MythicMobs mythicMobs;
    @Getter
    private Plugin factions;
    @Getter
    private WorldGuardPlugin worldGuard;
    @Getter
    private TradeMe tradeMe;
    @Getter
    private Essentials essentials;
    @Getter
    private LockettePro lockettePro;
    @Getter
    private Shop shop;

    public void onEnable() {
        configManager = new ConfigManager(this);

        initializeData();

        // Plugins
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            mythicMobs = (MythicMobs) Bukkit.getPluginManager().getPlugin("MythicMobs");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        } else {
            Bukkit.getLogger().log(Level.WARNING, "UnioProtections couldn't hooked with WorldGuard. That might cause issues.");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            factions = Bukkit.getPluginManager().getPlugin("Factions");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("TradeMe")) {
            tradeMe = TradeMe.getInstance();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("LockettePro")) {
            lockettePro = (LockettePro) Bukkit.getPluginManager().getPlugin("LockettePro");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Shop")) {
            shop = (Shop) Bukkit.getPluginManager().getPlugin("Shop");
        }

        // Managers
        checkManager = new CheckManager(this);

        // Commands
        new CmdUnioprotections(this);
    }

    public void onDisable() {
        save();
    }

    public String getMessage(String configSection) {
        if (configManager.getConfig(Config.LANG).getString(configSection) == null) return null;

        return Utils.colorizeMessage(configManager.getConfig(Config.LANG).getString(configSection));
    }

    private void initializeData() {
        bilgiPrefix = getMessage("prefix.bilgiPrefix");
        dikkatPrefix = getMessage("prefix.dikkatPrefix");
        hataPrefix = getMessage("prefix.hataPrefix");
        consolePrefix = getMessage("prefix.consolePrefix");
    }

    public void reload() {
        save();
        reloadConfig();
        for (Config config : Config.values()) {
            configManager.reloadConfig(config);
        }
        initializeData();

        checkManager = new CheckManager(this);
    }

    private void save() {
        configManager.saveConfig(Config.DATA);
    }
}
