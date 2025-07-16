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
        for (NextItem bt : List.of(
                nextInventory.getBack(), nextInventory.getCurrent(),
                nextInventory.getNext(), nextInventory.getForward(),
                nextInventory.getBackward())) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
    }

    public static void setup(NextInventory instance) {
        int slots = instance.getSize().getTotalSlots();
        int[] indexes = new int[]{slots - 6, slots - 5, slots - 4, 8, 0};
        ArrayList<Integer> blockedList = new ArrayList<>(instance.getSize().getBlockedSlots());

        for (int i : indexes) {
            blockedList.remove((Integer) i);
        }

        instance.setIndexBlockedList(blockedList);
        instance.setIndexAllowedList(new ArrayList<>(instance.getSize().getAllowedSlots()));

        NextItem backItem = new NextItem("Retroceder", "---", Material.ARROW, instance);
        backItem.setIndex(indexes[0]);
        backItem.button(true).draggable(false);
        instance.setBack(backItem);
        instance.getItems().remove(backItem);

        NextItem currentItem = new NextItem("Pagina Actual", "Pagina actual: 1", Material.CLOCK, instance);
        currentItem.setIndex(indexes[1]);
        currentItem.draggable(false);
        instance.setCurrent(currentItem);
        instance.getItems().remove(currentItem);

        NextItem nextItem = new NextItem("Avanzar", "Pagina siguiente: 2", Material.ARROW, instance);
        nextItem.setIndex(indexes[2]);
        nextItem.button(true).draggable(false);
        instance.setNext(nextItem);
        instance.getItems().remove(nextItem);

        if (instance.isHistorable()) {
            NextItem ForwardItem = new NextItem("Siguiente Interfaz", "Siguiente interfaz: -", Material.GREEN_STAINED_GLASS_PANE, instance);
            ForwardItem.setIndex(8);
            ForwardItem.button(true).draggable(false);
            instance.setFoward(ForwardItem);
            instance.getItems().remove(ForwardItem);

            NextItem BackwardItem = new NextItem("Anterior Interfaz", "Anterior interfaz: -", Material.BLUE_STAINED_GLASS_PANE, instance);
            BackwardItem.setIndex(0);
            BackwardItem.button(true).draggable(false);
            instance.setBackward(BackwardItem);
            instance.getItems().remove(BackwardItem);
        }

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
