package com.sebxstt.nextinventory.callbacks;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.events.NextInventoryEvent;
import com.sebxstt.nextinventory.instances.NextItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.sebxstt.nextinventory.NextInventoryProvider.historyManager;

public class HistorableCallbacks {
    public static void setup(NextInventory instance, int[] indexes) {
        if (instance.isHistorable()) {
            NextItem ForwardItem = new NextItem("Next Interface", "---", Material.GREEN_STAINED_GLASS_PANE, instance);
            ForwardItem.setIndex(indexes[0]);
            ForwardItem.button(true).draggable(false);
            instance.setForward(ForwardItem);
            instance.getItems().remove(ForwardItem);

            NextItem BackwardItem = new NextItem("Back Interface", "---", Material.BLUE_STAINED_GLASS_PANE, instance);
            BackwardItem.setIndex(indexes[1]);
            BackwardItem.button(true).draggable(false);
            instance.setBackward(BackwardItem);
            instance.getItems().remove(BackwardItem);

            instance.getForward().onClick(event -> {
                Player player = event.getPlayer();
                NextInventoryEvent inventoryEvent = new NextInventoryEvent(player);

                instance.emitForward(inventoryEvent);

                if (inventoryEvent.isAborted()) {
                    return;
                }

                historyManager.next(player.getUniqueId());
            });

            instance.getBackward().onClick(event -> {
                Player player = event.getPlayer();
                NextInventoryEvent inventoryEvent = new NextInventoryEvent(player);

                instance.emitBackward(inventoryEvent);

                if (inventoryEvent.isAborted()) {
                    return;
                }

                historyManager.back(player.getUniqueId());
            });
        }
    }
}
