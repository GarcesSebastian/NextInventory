package com.sebxstt.nextinventory.instances;

import com.sebxstt.nextinventory.InventoryHelper;
import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.functions.utils.InPlayer;
import com.sebxstt.nextinventory.managers.HistoryManager;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.sebxstt.nextinventory.NextInventoryProvider.historyManager;

public class PlayerHistory {
    private UUID id;
    private UUID player;
    private UUID CurrentInventory;

    public PlayerHistory(UUID player) {
        this.id = UUID.randomUUID();
        this.player = player;
    }

    public void next() {
        int CurrentIndex = historyManager.interfaces.indexOf(CurrentInventory);
        if (CurrentIndex + 1 < historyManager.interfaces.size()) {
            UUID nextInventoryID = historyManager.interfaces.get(CurrentIndex + 1);
            NextInventory lastInventory = InventoryHelper.next(this.getCurrentInventory());
            NextInventory nextInventory = InventoryHelper.next(nextInventoryID);

            this.setCurrentInventory(nextInventoryID);

            lastInventory.close(this.player);
            nextInventory.open(this.player);
        }
    }

    public void back(){
        int CurrentIndex = historyManager.interfaces.indexOf(CurrentInventory);
        if (CurrentIndex > 0) {
            UUID nextInventoryID = historyManager.interfaces.get(CurrentIndex - 1);
            NextInventory lastInventory = InventoryHelper.next(this.getCurrentInventory());
            NextInventory nextInventory = InventoryHelper.next(nextInventoryID);

            this.setCurrentInventory(nextInventoryID);

            lastInventory.close(this.player);
            nextInventory.open(this.player);
        }
    }

    public void setCurrentInventory(UUID currentInventory) {
        CurrentInventory = currentInventory;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getCurrentInventory() {
        return CurrentInventory;
    }
}
