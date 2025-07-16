package com.sebxstt.nextinventory.managers;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.events.NextInventoryEvent;
import com.sebxstt.nextinventory.instances.NextItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.sebxstt.nextinventory.InventoryHelper.blocked;

public class PaginationManager {
    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : List.of(nextInventory.getBack(), nextInventory.getCurrent(), nextInventory.getNext())) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
    }

    public static void setup(NextInventory instance) {
        int slots = instance.getSize().getTotalSlots();
        int[] indexes = new int[]{slots - 6, slots - 5, slots - 4};
        ArrayList<Integer> blockedList = new ArrayList<>(instance.getSize().getBlockedSlots());

        for (int i : indexes) {
            blockedList.remove((Integer) i);
        }

        instance.setIndexBlockedList(blockedList);
        instance.setIndexAllowedList(new ArrayList<>(instance.getSize().getAllowedSlots()));

        NextItem BackItem = new NextItem("Retroceder", "---", Material.ARROW, instance);
        BackItem.setIndex(indexes[0]);
        BackItem.button(true).draggable(false);
        instance.setBack(BackItem);
        instance.getItems().remove(BackItem);

        NextItem CurrentItem = new NextItem("Pagina Actual", "Pagina actual: 1", Material.CLOCK, instance);
        CurrentItem.setIndex(indexes[1]);
        CurrentItem.draggable(false);
        instance.setCurrent(CurrentItem);
        instance.getItems().remove(CurrentItem);

        NextItem NextItem = new NextItem("Avanzar", "Pagina siguiente: 2", Material.ARROW, instance);
        NextItem.setIndex(indexes[2]);
        NextItem.button(true).draggable(false);
        instance.setNext(NextItem);
        instance.getItems().remove(NextItem);

        RenderPagination(instance);

        instance.getBack().onClick(event -> {
            Player player = event.getPlayer();
            NextInventoryEvent inventoryEvent = new NextInventoryEvent(player);

            instance.emitBack(inventoryEvent);

            if (inventoryEvent.isAborted()) {
                return;
            }

            instance.back();
        });

        instance.getNext().onClick(event -> {
            Player player = event.getPlayer();
            NextInventoryEvent inventoryEvent = new NextInventoryEvent(player);

            instance.emitNext(inventoryEvent);

            if (inventoryEvent.isAborted()) {
                return;
            }

            instance.next();
        });
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
