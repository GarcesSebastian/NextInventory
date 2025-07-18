package com.sebxstt.nextinventory;

import com.sebxstt.nextinventory.events.NextInventoryEvent;
import com.sebxstt.nextinventory.functions.utils.InPlayer;
import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.instances.NextItem;
import com.sebxstt.nextinventory.instances.NextPage;
import com.sebxstt.nextinventory.managers.NormalManager;
import com.sebxstt.nextinventory.managers.PaginationManager;
import com.sebxstt.nextinventory.managers.ScrollingManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sebxstt.nextinventory.NextInventoryProvider.nextInventoryList;

public class InventoryHelper {
    public static List<NextItem> DefaultButtons(NextInventory nextInventory) {
        List<NextItem> items = new ArrayList<>();

        if (nextInventory.getType() == InventoryType.PAGINATION) {
            items.addAll(List.of(nextInventory.getBack(), nextInventory.getCurrent(), nextInventory.getNext()));
        }

        if (nextInventory.isHistorable()) {
            items.addAll(List.of(nextInventory.getForward(), nextInventory.getBackward()));
        }

        return items;
    }

    public static Player verifyPlayer(UUID player) {
        Player plr = InPlayer.instance(player);
        if (plr == null) {
            throw new IllegalStateException("No se encontro el jugador");
        }

        return plr;
    }

    public static Integer originalIndex(NextInventory nextInventory, int index) {
        Integer indexResolved = nextInventory.getAllowedList().get(index);
        if (indexResolved == null) {
            throw new IllegalStateException("Index Not Found " + index);
        }

        return indexResolved;
    }

    public static Integer contentIndex(NextInventory nextInventory, int index) {
        Integer tempAllowed = nextInventory.getAllowedList().stream()
                        .filter(allowed -> allowed.equals(index)).findFirst().orElse(null);

        Integer indexResolved = nextInventory.getAllowedList().indexOf(tempAllowed);

        if (indexResolved == null) {
            throw new IllegalStateException("Index Not Found " + index);
        }

        return indexResolved;
    }

    public static void RenderPagination(NextInventory nextInventory) {
        if (nextInventory.getType() == InventoryType.NORMAL){
            NormalManager.RenderPagination(nextInventory);
            return;
        }

        if (nextInventory.getType() == InventoryType.PAGINATION) {
            PaginationManager.RenderPagination(nextInventory);
            return;
        }

        if (nextInventory.getType()  == InventoryType.SCROLLING) {
            ScrollingManager.RenderPagination(nextInventory);
            return;
        }
    }

    public static ItemStack blocked() {
        ItemStack blocker = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = blocker.getItemMeta();
        meta.setDisplayName("BLOCKED");
        blocker.setItemMeta(meta);

        return blocker;
    }

    public static void resolve(NextInventory nextInventory) {
        if (nextInventory == null) {
            throw new IllegalStateException("InventoryGUI NULL");
        }

        InventoryType type = nextInventory.getType();

        nextInventory.setIndexBlockedList(new ArrayList<>(nextInventory.getSize().getBlockedSlots()));
        nextInventory.setIndexAllowedList(new ArrayList<>(nextInventory.getSize().getAllowedSlots()));

        if (type == InventoryType.NORMAL) {
            NormalManager.setup(nextInventory);
            return;
        }

        if (type == InventoryType.PAGINATION) {
            PaginationManager.setup(nextInventory);
            return;
        }

        if (type == InventoryType.SCROLLING) {
            ScrollingManager.setup(nextInventory);
            return;
        }
    }

    public static NextInventory next(UUID id) {
        NextInventory instance = nextInventoryList.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
        if (instance == null) throw new IllegalStateException("Inventory Not Found " + id);
        return instance;
    }

    public static NextItem item(UUID id, UUID inventory) {
        NextInventory instance = next(inventory);
        NextItem itemInstance = instance.getItems().stream().filter(im -> im.getId().equals(id)).findFirst().orElse(null);
        if (itemInstance == null) {
            System.out.println("Item Not Found " + id);
            return null;
        }
        return itemInstance;
    }

    public static NextPage pagination(int index, UUID inventory) {
        NextInventory instance = next(inventory);
        NextPage pageInstance = instance.getPages().stream().filter(p -> p.getIndex() == index).findFirst().orElse(null);
        if (pageInstance == null) {
            System.out.println("Page Not Found " + index);
            return null;
        }
        return pageInstance;
    }

    public static NextPage pagination(UUID page, UUID inventory) {
        NextInventory instance = next(inventory);
        NextPage pageInstance = instance.getPages().stream().filter(p -> p.getId().equals(page)).findFirst().orElse(null);
        if (pageInstance == null) {
            System.out.println("Page Not Found " + page);
            return null;
        }
        return pageInstance;
    }

    public static void checkItem(NextInventory nextInventory, NextItem nextItem) {
        Optional<NextItem> maybeItemInventory = nextInventory.getItems().stream()
                .filter(maybe -> maybe.getIndex() == nextItem.getIndex() && maybe.isRegistry())
                .findFirst();

        if (maybeItemInventory.isPresent()) {
            throw new IllegalStateException("Index " +  nextItem.getIndex() + " registered");
        }
    }
}
