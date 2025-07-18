package com.sebxstt.nextinventory.listener;

import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.events.NextInventoryEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class NextInventoryListener {
    public UUID nextInventory;
    public InventoryType type;
    public boolean isHistorable = false;

    public NextInventoryListener(UUID nextInventory, InventoryType type) {
        this.nextInventory = nextInventory;
        this.type = type;
    }

    public void setType(InventoryType type) {
        this.type = type;
    }

    public void setIsHistorable(boolean isHistorable) {
        this.isHistorable = isHistorable;
    }

    public boolean isHistorable() {
        return isHistorable;
    }

    private Consumer<NextInventoryEvent> onBackCallback;
    private Consumer<NextInventoryEvent> onNextCallback;
    private Consumer<NextInventoryEvent> onForwardCallback;
    private Consumer<NextInventoryEvent> onBackwardCallback;

    public void onBack(Consumer<NextInventoryEvent> onBackCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onBackCallback = onBackCallback;
        } else {
            System.out.println("[NextInventoryListener] try to assign onBack in inventory not paginated: " + type.toString().toUpperCase());
        }
    }

    public void onNext(Consumer<NextInventoryEvent> onNextCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onNextCallback = onNextCallback;
        } else {
            System.out.println("[NextInventoryListener] try to assign onNext in inventory not paginated: " + type.toString().toUpperCase());
        }
    }

    public void onForward(Consumer<NextInventoryEvent> onForwardCallback) {
        if (this.isHistorable) {
            this.onForwardCallback = onForwardCallback;
        } else {
            System.out.println("[NextInventoryListener] try to assign onForward in inventory not historable");
        }
    }

    public void onBackward(Consumer<NextInventoryEvent> onBackwardCallback) {
        if (this.isHistorable) {
            this.onBackwardCallback = onBackwardCallback;
        } else {
            System.out.println("[NextInventoryListener] try to assign onBackward in inventory not historable");
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

    public void emitForward(NextInventoryEvent event) {
        if (onForwardCallback != null) {
            onForwardCallback.accept(event);
        }
    }

    public void emitBackward(NextInventoryEvent event) {
        if (onBackwardCallback != null) {
            onBackwardCallback.accept(event);
        }
    }
}
