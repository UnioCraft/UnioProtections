package me.uniodex.unioprotections.managers;

import lombok.Getter;
import me.UnioDex.CustomItems.managers.ItemManager.Items;
import me.uniodex.unioprotections.UnioProtections;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsManager {

    private UnioProtections plugin;
    @Getter
    private List<String> disallowedItemNames = new ArrayList<>();

    public CustomItemsManager(UnioProtections plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("settings.disallowUCIItemNames")) {
            initUCINames();
        }

        if (plugin.getConfig().getStringList("settings.disallowedItemNames") != null && plugin.getConfig().getStringList("settings.disallowedItemNames").size() > 0) {
            initConfigNames();
        }
    }

    private void initUCINames() {
        for (Items item : Items.values()) {
            String itemName = ChatColor.stripColor(plugin.getCustomItems().itemManager.getItem(item).getItemMeta().getDisplayName().toLowerCase());
            disallowedItemNames.add(itemName);
            if (itemName.contains(" (sağ tıkla)")) {
                itemName = itemName.replace(" (sağ tıkla)", "");
                disallowedItemNames.add(itemName);
            }
        }
    }

    private void initConfigNames() {
        for (String itemName : plugin.getConfig().getStringList("settings.disallowedItemNames")) {
            disallowedItemNames.add(itemName.toLowerCase());
        }
    }
}
