package com.sebxstt.nextinventory.instances;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.functions.utils.InPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerView {
    private UUID id;
    private UUID player;
    private NextInventory template;

    private Inventory instance;
    private List<NextItem> items = new ArrayList<>();
    private List<NextPage> pages = new ArrayList<>();

    private int current = 1;

    public PlayerView(UUID player, NextInventory inventory) {
        this.id = UUID.randomUUID();
        this.player = player;
        this.template = inventory;

        this.instance = Bukkit.createInventory(null, inventory.getSize().getTotalSlots(), inventory.getTitle());

        this.items = new ArrayList<>(inventory.getItems()).stream()
                .map(NextItem::clone)
                .collect(Collectors.toList());

        this.pages = new ArrayList<>(inventory.getPages()).stream()
                .map(NextPage::clone)
                .collect(Collectors.toList());
    }

    public Player player(){
        return InPlayer.instance(this.player);
    }

    public void setTemplate(NextInventory template) {
        this.template = template;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public NextInventory getTemplate() {
        return template;
    }

    public Inventory getInstance() {
        return instance;
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
