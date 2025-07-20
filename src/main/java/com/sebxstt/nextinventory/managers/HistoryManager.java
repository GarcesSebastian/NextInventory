package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.InventoryHelper;
import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.functions.utils.InPlayer;
import com.sebxstt.nextinventory.instances.PlayerHistory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class HistoryManager {
    public static HistoryManager instance;
    public ArrayList<PlayerHistory> PlayersHistory = new ArrayList<>();
    public ArrayList<UUID> interfaces = new ArrayList<>();

    private HistoryManager() {};

    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }

        return instance;
    }

    public PlayerHistory getHistory(UUID player) {
        PlayerHistory history = PlayersHistory.stream()
                .filter(h -> h.getPlayer().equals(player))
                .findFirst().orElse(null);

        if (history == null) throw new IllegalStateException("Not Found History To Player " + player);

        return history;
    }

    public void update(Player viewer) {
        for (UUID interfaceID : this.interfaces) {
            String forwardDescription = "<red>You cannot go forward</red>";
            String backwardDescription = "<red>You cannot go back</red>";

            NextInventory CurrentInventory = InventoryHelper.next(interfaceID);
            if (CurrentInventory == null) {
                System.err.println("[HistoryManager] Error: Current interface not found for ID: " + interfaceID);
                continue;
            }

            int CurrentIndex = this.interfaces.indexOf(interfaceID);

            if (CurrentIndex - 1 >= 0) {
                UUID BackwardID = this.interfaces.get(CurrentIndex - 1);
                NextInventory BackwardInventory = InventoryHelper.next(BackwardID);
                if (BackwardInventory != null) {
                    backwardDescription = "<yellow>" + BackwardInventory.getTitle() + "</yellow>";
                    BackwardInventory.render();
                    BackwardInventory.getInstance().setItem(
                            BackwardInventory.getBackward().getIndex(),
                            BackwardInventory.getBackward().getInstance()
                    );
                } else {
                    System.err.println("[HistoryManager] Error: Previous interface not found for ID: " + BackwardID);
                }
            }

            if (CurrentIndex + 1 < this.interfaces.size()) {
                UUID ForwardID = this.interfaces.get(CurrentIndex + 1);
                NextInventory ForwardInventory = InventoryHelper.next(ForwardID);
                if (ForwardInventory != null) {
                    forwardDescription = "<yellow>" + ForwardInventory.getTitle() + "</yellow>";
                    ForwardInventory.render();
                    ForwardInventory.getInstance().setItem(
                            ForwardInventory.getForward().getIndex(),
                            ForwardInventory.getForward().getInstance()
                    );
                } else {
                    System.err.println("[HistoryManager] Error: Next interface not found for ID: " + ForwardID);
                }
            }

            CurrentInventory.getForward().setDescription(forwardDescription);
            CurrentInventory.getBackward().setDescription(backwardDescription);
        }
    }

    public PlayerHistory view(UUID viewer, UUID inventory) {
        PlayerHistory history = PlayersHistory.stream()
                .filter(h -> h.getPlayer().equals(viewer)).findFirst().orElse(null);

        if (history == null) {
            System.out.println("[HistoryManager] Not Found Viewer");
            history = new PlayerHistory(viewer);
            PlayersHistory.add(history);
        }

        history.setCurrentInventory(inventory);
        this.update(InPlayer.instance(viewer));

        return history;
    }

    public void save(NextInventory nextInventory) {
        UUID InterfaceID = interfaces.stream()
                .filter(id -> id.equals(nextInventory.getId()))
                .findFirst().orElse(null);

        if (InterfaceID != null) return;

        interfaces.add(nextInventory.getId());
    }

    public void next(UUID player) {
        PlayerHistory history = getHistory(player);
        history.next();
    }

    public void back(UUID player) {
        PlayerHistory history = getHistory(player);
        history.back();
    }
}
