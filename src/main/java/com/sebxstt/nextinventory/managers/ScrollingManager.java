package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.instances.NextItem;

import java.util.List;

import static com.sebxstt.nextinventory.InventoryHelper.blocked;

public class ScrollingManager {
    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : List.of(nextInventory.getBack(), nextInventory.getCurrent(), nextInventory.getNext())) {
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
