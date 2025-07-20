package com.sebxstt.nextinventory.instances;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.functions.utils.InPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class PlayerView {
    private UUID id;
    private UUID player;
    private UUID inventory;

    private int current = 1;
    private Inventory instance;

    public PlayerView(UUID player, NextInventory inventory) {
        this.id = UUID.randomUUID();
        this.player = player;
        this.inventory = inventory.getId();
        this.instance = Bukkit.createInventory(null, inventory.getSize().getTotalSlots(), inventory.getTitle());
    }

    public Player player(){
        return InPlayer.instance(this.player);
    }

    public void setInventory(UUID inventory) {
        this.inventory = inventory;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public UUID getInventory() {
        return inventory;
    }

    public int getCurrent() {
        return current;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getId() {
        return id;
    }
}
