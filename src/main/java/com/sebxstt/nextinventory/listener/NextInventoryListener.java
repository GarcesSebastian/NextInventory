package com.sebxstt.nextinventory.listener;

import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.events.NextInventoryEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class NextInventoryListener {
    public UUID nextInventory;
    public InventoryType type;

    public NextInventoryListener(UUID nextInventory, InventoryType type) {
        this.nextInventory = nextInventory;
        this.type = type;
    }

    public void setType(InventoryType type) {
        this.type = type;
    }

    private Consumer<NextInventoryEvent> onBackCallback;
    private Consumer<NextInventoryEvent> onNextCallback;

    public void onBack(Consumer<NextInventoryEvent> onBackCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onBackCallback = onBackCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onBack en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void onNext(Consumer<NextInventoryEvent> onNextCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onNextCallback = onNextCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onNext en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void emitBack(NextInventoryEvent event) {
        if (onBackCallback != null) {
            onBackCallback.accept(event);
        }
    }

    public void emitNext(NextInventoryEvent event) {
        if (onNextCallback != null) {
            onNextCallback.accept(event);
        }
    }
}
