package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.instances.NextItem;

import static com.sebxstt.nextinventory.InventoryHelper.DefaultButtons;
import static com.sebxstt.nextinventory.InventoryHelper.blocked;

public class ScrollingManager {
    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : DefaultButtons(nextInventory)) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
    }

    public static void setup(NextInventory instance) {
        RenderPagination(instance);
    }
}
