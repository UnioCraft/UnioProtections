package me.uniodex.unioprotections.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.uniodex.unioprotections.UnioProtections;
import me.uniodex.unioprotections.objects.EntityData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class LootProtectionListeners implements Listener {

    private UnioProtections plugin;

    public LootProtectionListeners(UnioProtections plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (!plugin.getCheckManager().getLootProtectionManager().isItemProtected(event.getItem())) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

        if (player.hasPermission("unioprotections.lootprotection.bypass")) return;

        String owner = plugin.getCheckManager().getLootProtectionManager().getItemOwner(event.getItem());
        if (owner == null) return;
        if (player.getName().equals(owner)) return;

        if (plugin.getFactions() != null) {
            Player ownerPlayer = Bukkit.getPlayerExact(owner);
            if (ownerPlayer != null) {
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
                FPlayer fowner = FPlayers.getInstance().getByPlayer(ownerPlayer);

                if (fplayer.getFaction().equals(fowner.getFaction())) {
                    return;
                }
            }
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = null;
        if (event.getDamager() instanceof Player) player = (Player) event.getDamager();
        if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                player = (Player) ((Projectile) event.getDamager()).getShooter();
            }
        }
        if (player == null) return;

        Entity entity = event.getEntity();

        if (!plugin.getCheckManager().getLootProtectionManager().getEntityDatas().containsKey(entity.getUniqueId())) {
            plugin.getCheckManager().getLootProtectionManager().getEntityDatas().put(entity.getUniqueId(), new EntityData(entity.getUniqueId()));
        }

        plugin.getCheckManager().getLootProtectionManager().getEntityDatas().get(entity.getUniqueId()).addDamage(player.getName(), event.getDamage());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getDrops().isEmpty()) return;
        if (!plugin.getCheckManager().getLootProtectionManager().getEntityDatas().containsKey(event.getEntity().getUniqueId()))
            return;

        Entity entity = event.getEntity();
        String topDamager = plugin.getCheckManager().getLootProtectionManager().getEntityDatas().get(entity.getUniqueId()).getTopDamager();
        if (topDamager == null) {
            plugin.getCheckManager().getLootProtectionManager().clearData(event.getEntity());
            return;
        }

        try {
            plugin.getCheckManager().getLootProtectionManager().protectLoot(event.getDrops(), topDamager, entity.getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.getDrops().clear();
        }

        plugin.getCheckManager().getLootProtectionManager().clearData(event.getEntity());

        if (!plugin.getCheckManager().getLootProtectionManager().shouldSendMessage(event.getEntity())) return;

        if (event.getEntity().getKiller() != null && !event.getEntity().getKiller().getName().equals(topDamager)) {
            Player killer = event.getEntity().getKiller();
            if (event.getEntity() instanceof Player) {
                killer.sendMessage(plugin.getMessage("lootProtection.playerLootBelongToSomeoneElse").replaceAll("%lootOwner%", topDamager));
            } else {
                killer.sendMessage(plugin.getMessage("lootProtection.mobLootBelongToSomeoneElse").replaceAll("%lootOwner%", topDamager));
            }
        }

        if (Bukkit.getPlayerExact(topDamager) != null) {
            Player killer = Bukkit.getPlayerExact(topDamager);
            killer.sendMessage(plugin.getMessage("lootProtection.lootBelongToYou"));

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (Bukkit.getPlayerExact(topDamager) != null) {
                    killer.sendMessage(plugin.getMessage("lootProtection.lootProtectionTimedOut"));
                }
            }, plugin.getCheckManager().getLootProtectionManager().getProtectionTimeout() * 20);
        }
    }
}
