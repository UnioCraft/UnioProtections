package me.uniodex.unioprotections.managers;

import lombok.Getter;
import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.listeners.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CheckManager {

    private UnioProtections plugin;

    private List<Check> enabledChecks;

    @Getter
    private LootProtectionManager lootProtectionManager;
    @Getter
    private EnderPearlManager enderPearlManager;
    @Getter
    private NoXZoneManager noXZoneManager;
    @Getter
    private EntityLimiterManager entityLimiterManager;
    @Getter
    private DuelsManager duelsManager;

    public CheckManager(UnioProtections plugin) {
        this.plugin = plugin;
        initializeChecks();
    }

    private void initializeChecks() {
        enabledChecks = new ArrayList<>();

        // Loot Protection
        if (plugin.getConfig().getBoolean("checks.lootProtection")) {
            lootProtectionManager = new LootProtectionManager(plugin);
            new LootProtectionListeners(plugin);
            enabledChecks.add(Check.LOOTPROTECTION);
        }

        // Only Allow List of Commands
        if (plugin.getConfig().getBoolean("checks.onlyAllowListOfCommands")) {
            new CommandProtectionListeners(plugin);
            enabledChecks.add(Check.ONLYALLOWLISTOFCOMMANDS);
        }

        // Ender Pearl Cooldown
        if (plugin.getConfig().getBoolean("checks.enderPearlCooldown")) {
            enderPearlManager = new EnderPearlManager(plugin);
            new EnderPearlListeners(plugin);
            enabledChecks.add(Check.ENDERPEARLCOOLDOWN);
        }

        // No Fly and Teleport Zones
        if (plugin.getConfig().getBoolean("checks.noFlyZones")) {
            noXZoneManager = new NoXZoneManager(plugin);
            new NoXZoneListeners(plugin);
            enabledChecks.add(Check.NOFLYZONE);
        }

        if (plugin.getConfig().getBoolean("checks.noTeleportZones")) {
            if (noXZoneManager == null) {
                noXZoneManager = new NoXZoneManager(plugin);
                new NoXZoneListeners(plugin);
            }
            enabledChecks.add(Check.NOTELEPORTZONE);
        }

        // Entity Limiter
        if (plugin.getConfig().getBoolean("checks.entitiyLimiter")) {
            entityLimiterManager = new EntityLimiterManager(plugin);
            new EntityLimiterListeners(plugin);
            enabledChecks.add(Check.ENTITYLIMITER);
        }

        // Anti TradeMe Bugs
        if (plugin.getConfig().getBoolean("checks.antiTradeMeBug") && plugin.getTradeMe() != null && plugin.getEssentials() != null) {
            new AntiTradeMeBugListeners(plugin);
            enabledChecks.add(Check.ANTITRADEMEBUG);
        }

        // Anti Crash Code
        if (plugin.getConfig().getBoolean("checks.antiCrashCode")) {
            new AntiCrashCodeListeners(plugin);
            enabledChecks.add(Check.ANTICRASHCODE);
        }

        // Anti Shop Bug
        if (plugin.getConfig().getBoolean("checks.antiShopBug") && plugin.getShop() != null && plugin.getLockettePro() != null) {
            new AntiShopBugListeners(plugin);
            enabledChecks.add(Check.ANTISHOPBUG);
        }

        // Deny Duel on Auction
        if (plugin.getConfig().getBoolean("checks.denyDuelOnAuction") && plugin.getFloAuction() != null && plugin.getDuels() != null) {
            duelsManager = new DuelsManager(plugin);
            new DuelsListener(plugin);
            enabledChecks.add(Check.NODUELONAUCTION);
        }

        // Small Fixes
        new SmallFixListeners(plugin);

        /// Leave Vehicle On Join
        if (plugin.getConfig().getBoolean("checks.smallFixes.leaveVehicleOnjoin")) {
            enabledChecks.add(Check.SMALLFIX_LEAVEVEHICLEONJOIN);
        }

        /// Disallow Item Frame in Dispenser
        if (plugin.getConfig().getBoolean("checks.smallFixes.disallowItemFrameInDispenser")) {
            enabledChecks.add(Check.SMALLFIX_DISALLOWITEMFRAMEINDISPENSER);
        }

        Bukkit.getLogger().log(Level.INFO, "UnioProtections loaded. Enabled checks:");
        for (Check check : enabledChecks) {
            Bukkit.getLogger().log(Level.INFO, "- " + check.toString());
        }
    }

    public boolean isCheckEnabled(Check check) {
        return enabledChecks.contains(check);
    }

    public enum Check {
        LOOTPROTECTION,
        ONLYALLOWLISTOFCOMMANDS,
        ENDERPEARLCOOLDOWN,
        NOFLYZONE,
        NOTELEPORTZONE,
        ENTITYLIMITER,
        ANTITRADEMEBUG,
        ANTICRASHCODE,
        ANTISHOPBUG,
        NODUELONAUCTION,
        SMALLFIX_LEAVEVEHICLEONJOIN,
        SMALLFIX_DISALLOWITEMFRAMEINDISPENSER
    }

}
