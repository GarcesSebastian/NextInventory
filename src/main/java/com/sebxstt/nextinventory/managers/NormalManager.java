package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.callbacks.HistorableCallbacks;
import com.sebxstt.nextinventory.instances.NextItem;

import java.util.ArrayList;

import static com.sebxstt.nextinventory.InventoryHelper.DefaultButtons;
import static com.sebxstt.nextinventory.InventoryHelper.blocked;

public class NormalManager {
    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : DefaultButtons(nextInventory)) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
    }

    public static void setup(NextInventory instance) {
        int[] indexesHistorable = new int[]{8, 0};
        ArrayList<Integer> blockedList = new ArrayList<>(instance.getSize().getBlockedSlots());

        for (int i : indexesHistorable) {
            blockedList.remove((Integer) i);
        }

        instance.setIndexBlockedList(blockedList);
        instance.setIndexAllowedList(new ArrayList<>(instance.getSize().getAllowedSlots()));

        HistorableCallbacks.setup(instance, indexesHistorable);

        RenderPagination(instance);
    }
}
