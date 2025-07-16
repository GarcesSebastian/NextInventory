package com.sebxstt.nextinventory;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NextInventoryProvider {
    public static final MiniMessage mm = MiniMessage.miniMessage();
    public static final Map<Inventory, NextInventory> nextInventoryMap = new HashMap<>();
    public static final ArrayList<NextInventory> nextInventoryList = new ArrayList<>();
    public static Plugin plugin;

    public static void setup(Plugin plugin) {
        Bukkit.getLogger().info("[NextInventory] Execute Provider");
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
        NextInventoryProvider.plugin = plugin;
    }
}
