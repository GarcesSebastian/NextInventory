package com.sebxstt.nextinventory.events;

import org.bukkit.entity.Player;

public class NextInventoryEvent {
    private Player player;
    private boolean isAborted = false;

    public NextInventoryEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void abort(boolean isAborted) {
        this.isAborted = isAborted;
    }

    public boolean isAborted() {
        return this.isAborted;
    }
}
