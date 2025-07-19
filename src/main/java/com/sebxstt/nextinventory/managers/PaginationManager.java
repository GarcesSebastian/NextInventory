package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.callbacks.HistorableCallbacks;
import com.sebxstt.nextinventory.callbacks.PaginationCallbacks;
import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.instances.NextItem;

import java.util.ArrayList;

import static com.sebxstt.nextinventory.InventoryHelper.DefaultButtons;
import static com.sebxstt.nextinventory.InventoryHelper.blocked;

public class PaginationManager {
    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : DefaultButtons(nextInventory)) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
    }

    public static void setup(NextInventory instance) {
        int slots = instance.getSize().getTotalSlots();
        ArrayList<Integer> blockedList = new ArrayList<>(instance.getSize().getBlockedSlots());

        int[] indexesPagination = new int[]{slots - 6, slots - 5, slots - 4};
        int[] indexesHistorable = new int[]{8, 0};

        for (int i : indexesPagination) {
            blockedList.remove((Integer) i);
        }

        if (instance.isHistorable()){
            for (int i : indexesHistorable) {
                blockedList.remove((Integer) i);
            }
        }

        instance.setIndexBlockedList(blockedList);
        instance.setIndexAllowedList(new ArrayList<>(instance.getSize().getAllowedSlots()));

        PaginationCallbacks.setup(instance, indexesPagination);
        HistorableCallbacks.setup(instance, indexesHistorable);

        RenderPagination(instance);
    }

    public static void update(NextInventory instance) {
        if (instance.getType() != InventoryType.PAGINATION) return;

        int currentPage = instance.getCurrentPage();

        String backDescription = (currentPage <= 1)
                ? "<red>No puedes retroceder</red>"
                : "Pagina anterior: <yellow>" + (currentPage - 1) + "</yellow>";

        String nextDescription = (currentPage >= instance.getPages().size())
                ? "<red>No puedes avanzar</red>"
                : "Pagina siguiente: <yellow>" + (currentPage + 1) + "</yellow>";

        instance.getBack().setDescription(backDescription);
        instance.getCurrent().setDescription("Pagina actual: <yellow>" + currentPage + "</yellow>");
        instance.getNext().setDescription(nextDescription);
    }
}
