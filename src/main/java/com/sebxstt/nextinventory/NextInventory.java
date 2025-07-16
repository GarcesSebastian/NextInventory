package com.sebxstt.nextinventory;

import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.listener.NextInventoryListener;
import com.sebxstt.nextinventory.enums.InventorySize;
import com.sebxstt.nextinventory.instances.NextItem;
import com.sebxstt.nextinventory.instances.NextPage;
import com.sebxstt.nextinventory.managers.PaginationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sebxstt.nextinventory.InventoryHelper.*;

public class NextInventory extends NextInventoryListener {
    public UUID id;
    private String title;
    private InventorySize size;

    private InventoryType type;
    private ArrayList<UUID> players = new ArrayList<>();
    private Inventory instance;

    private NextItem back;
    private NextItem current;
    private NextItem next;

    private NextItem foward;
    private NextItem backward;

    private Integer currentPage = 1;
    private boolean isHistorable = false;

    private ArrayList<Integer> indexBlockedList = new ArrayList<>();
    private ArrayList<Integer> indexAllowedList = new ArrayList<>();

    private ArrayList<NextPage> pages = new ArrayList<>();
    private ArrayList<NextItem> items = new ArrayList<>();

    private NextInventory() {
        super(UUID.randomUUID(), InventoryType.NORMAL);
        this.id = super.nextInventory;
        this.title = "Untitled";
        this.size = InventorySize.NORMAL;

        this.instance = Bukkit.createInventory(null, size.getTotalSlots(), title);
        this.type = type;

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);

        this.pages.add(new NextPage(this).index(1));
        resolve(this);
    }

    public static NextInventory builder() {
        return new NextInventory();
    }

    public NextInventory title(String title) {
        this.title = title;

        NextInventoryProvider.nextInventoryMap.remove(this.instance);
        NextInventoryProvider.nextInventoryList.removeIf(inv -> inv.getId().equals(this.id));

        Inventory newInventory = Bukkit.createInventory(null, size.getTotalSlots(), title);
        newInventory.setContents(this.instance.getContents());
        this.instance = newInventory;

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);

        for (UUID player : this.players) {
            Player playerOnline = Bukkit.getPlayer(player);
            assert playerOnline != null;
            if (!playerOnline.isOnline()) continue;
            playerOnline.closeInventory();
            playerOnline.openInventory(this.instance);
        }

        return this;
    }

    public NextInventory type(InventoryType type) {
        this.type = type;
        resolve(this);

        return this;
    }

    public NextInventory size(InventorySize size) {
        Inventory newInventory = Bukkit.createInventory(null, size.getTotalSlots(), title);

        for (NextItem item : this.getItems()) {
            newInventory.setItem(item.getIndex(), item.getInstance());
        }

        this.instance.clear();

        NextInventoryProvider.nextInventoryMap.remove(this.instance);
        NextInventoryProvider.nextInventoryList.removeIf(inv -> inv.getId().equals(this.id));

        this.size = size;
        this.instance = newInventory;

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);

        resolve(this);

        for (UUID player : this.players) {
            Player playerOnline = Bukkit.getPlayer(player);
            assert playerOnline != null;
            if (!playerOnline.isOnline()) continue;
            playerOnline.closeInventory();
            playerOnline.openInventory(this.instance);
        }

        return this;
    }

    public NextInventory historable(boolean isHistorable) {
        this.isHistorable = isHistorable;
        resolve(this);

        return this;
    }

    public NextInventory open(UUID target) throws IllegalStateException {
        if (!this.players.contains(target)) {
            this.players.add(target);
        }

        Player plr = verifyPlayer(target);
        plr.openInventory(this.instance);

        return this;
    }

    public NextInventory close(UUID target) throws IllegalStateException {
        this.players.remove(target);
        Player plr = verifyPlayer(target);
        plr.closeInventory();

        return this;
    }

    public NextInventory current(int index) {
        if(index > this.pages.size()) throw new IllegalStateException("Not Found Index Page " + index);
        this.currentPage = index;
        this.render();

        return this;
    }

    public void destroy() {
        for (UUID playerUUID : new ArrayList<>(this.players)) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null && player.isOnline()) {
                player.closeInventory();
            }
        }

        NextInventoryProvider.nextInventoryMap.remove(this.instance);
        NextInventoryProvider.nextInventoryList.removeIf(inv -> inv.getId().equals(this.id));

        this.players.clear();
        this.items.clear();
        this.pages.clear();
        this.indexAllowedList.clear();
        this.indexBlockedList.clear();
        this.instance = null;
    }

    public void render() {
        PaginationManager.update(this);
        NextPage currentPage = pagination(this.currentPage, this.id);
        if (currentPage == null) return;

        this.instance.clear();
        RenderPagination(this);

        for (UUID item : currentPage.getStack()) {
            NextItem nextItem = item(item, this.id);
            if (nextItem == null) continue;
            this.instance.setItem(nextItem.getIndex(), nextItem.getInstance());
        }
    }

    public void back() {
        if (this.currentPage <= 0) return;
        this.currentPage--;
        if (this.pages.stream().noneMatch(p -> p.getIndex() == this.currentPage)) {
            this.currentPage++;
            return;
        }

        this.render();
    }

    public void next() {
        if (this.currentPage >= this.pages.size()) return;
        this.currentPage++;
        if (this.pages.stream().noneMatch(p -> p.getIndex() == this.currentPage)) {
            this.currentPage--;
            return;
        }

        this.render();
    }

    public NextItem CustomItem(String name, String description, Material material, int index) {
        Integer indexResolved = originalIndex(this, index);
        NextItem nextItem = new NextItem(name, description, material, this);
        nextItem.setIndex(indexResolved);
        return nextItem;
    }

    public NextInventory pages(int amount) {
        for (int i = 0; i < amount; i++) {
            NextPage newPage = new NextPage(this).index(this.pages.size() + 1);
            this.pages.add(newPage);
        }

        return this;
    }

    public ArrayList<NextItem> Buttons() {
        List<NextItem> buttonsOnly = items.stream()
                .filter(NextItem::isButton)
                .map(it -> (NextItem) it)
                .toList();

        return new ArrayList<>(buttonsOnly);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(InventorySize size) {
        this.size = size;
    }

    public void setType(InventoryType type) {
        this.type = type;
    }

    public void setBack(NextItem back) {
        this.back = back;
    }

    public void setCurrent(NextItem current) {
        this.current = current;
    }

    public void setNext(NextItem next) {
        this.next = next;
    }

    public void setFoward(NextItem foward) {
        this.foward = foward;
    }

    public void setBackward(NextItem backward) {
        this.backward = backward;
    }

    public void setIndexBlockedList(ArrayList<Integer> indexBlockedList) {
        this.indexBlockedList = indexBlockedList;
    }

    public void setIndexAllowedList(ArrayList<Integer> indexAllowedList) {
        this.indexAllowedList = indexAllowedList;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public InventorySize getSize() {
        return size;
    }

    public ArrayList<Integer> getAllowedList() {
        return indexAllowedList;
    }

    public ArrayList<Integer> getBlockedList() {
        return indexBlockedList;
    }

    public ArrayList<NextItem> getItems() {
        return items;
    }

    public ArrayList<NextPage> getPages() {
        return pages;
    }

    public InventoryType getType() {
        return type;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public Inventory getInstance() {
        return instance;
    }

    public NextItem getBack() {
        return back;
    }

    public NextItem getCurrent() {
        return current;
    }

    public NextItem getNext() {
        return next;
    }

    public NextItem getForward() {
        return this.foward;
    }

    public NextItem getBackward() {
        return this.backward;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public boolean isHistorable() { return this.isHistorable; }
}
