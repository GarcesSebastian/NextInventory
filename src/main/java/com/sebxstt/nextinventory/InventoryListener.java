package com.sebxstt.nextinventory;

import com.sebxstt.nextinventory.instances.NextItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.List;
import java.util.Optional;

import static com.sebxstt.nextinventory.InventoryHelper.DefaultButtons;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory clicked = event.getClickedInventory();
        InventoryView view = player.getOpenInventory();

        if (clicked == null) return;

        NextInventory inv = NextInventoryProvider.nextInventoryMap.get(view.getTopInventory());
        if (inv == null) return;

        int rawSlot = event.getRawSlot();

        boolean topInventory = rawSlot < view.getTopInventory().getSize();
        boolean bottomInventory = rawSlot >= view.getTopInventory().getSize();

        if ((topInventory && bottomInventory) || (event.isShiftClick() && bottomInventory)) {
            event.setCancelled(true);
            return;
        }

        switch (event.getAction()) {
            case HOTBAR_SWAP:
            case MOVE_TO_OTHER_INVENTORY:
                event.setCancelled(true);
                return;
        }

        if (inv.getBlockedList().contains(rawSlot)) {
            System.out.println("[InventoryListener] Slot bloqueado: " + rawSlot);
            event.setCancelled(true);
            return;
        }

        for (NextItem bt : inv.Buttons()) {
            if (bt.getIndex() == rawSlot && bt.getCurrentPage().equals(inv.getCurrentPage()) && bt.isRegistry()) {
                event.setCancelled(true);
                bt.emitClick(player);
                return;
            }
        }

        for (NextItem bt : DefaultButtons(inv)) {
            if (bt.getIndex() == rawSlot) {
                event.setCancelled(true);
                bt.emitClick(player);
                return;
            }
        }

        Optional<NextItem> maybeItem = inv.getItems().stream()
                .filter(item -> item.getIndex() == rawSlot && item.getCurrentPage().equals(inv.getCurrentPage()) && item.isRegistry())
                .findFirst();
        if (maybeItem.isPresent() && !maybeItem.get().isDraggable()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory top = player.getOpenInventory().getTopInventory();
        NextInventory inv = NextInventoryProvider.nextInventoryMap.get(top);
        if (inv == null) return;

        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < top.getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
