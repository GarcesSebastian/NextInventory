package com.sebxstt.nextinventory.events;

import org.bukkit.entity.Player;

public class NextClickEvent {
    private Player player;

    public NextClickEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
