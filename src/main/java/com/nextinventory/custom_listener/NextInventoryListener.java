package com.nextinventory.custom_listener;

import com.nextinventory.enums.InventoryType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class NextInventoryListener {
    public UUID nextInventory;
    public InventoryType type;

    public NextInventoryListener(UUID nextInventory, InventoryType type) {
        this.nextInventory = nextInventory;
        this.type = type;
    }

    private Consumer<Player> onBackCallback;
    private Consumer<Player> onNextCallback;

    public void onBack(Consumer<Player> onBackCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onBackCallback = onBackCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onBack en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void onNext(Consumer<Player> onNextCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onNextCallback = onNextCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onNext en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void emitBack(Player player) {
        if (onBackCallback != null) {
            onBackCallback.accept(player);
        }
    }

    public void emitNext(Player player) {
        if (onNextCallback != null) {
            onNextCallback.accept(player);
        }
    }
}
