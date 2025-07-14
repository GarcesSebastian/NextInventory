package com.nextinventory.functions.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InPlayer {
    public static Player instance(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getPlayer();
    }
}
