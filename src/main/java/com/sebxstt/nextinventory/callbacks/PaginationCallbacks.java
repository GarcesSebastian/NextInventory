package com.sebxstt.nextinventory.callbacks;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.events.NextInventoryEvent;
import com.sebxstt.nextinventory.instances.NextItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PaginationCallbacks {
    public static void setup(NextInventory instance, int[] indexes) {
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
}
