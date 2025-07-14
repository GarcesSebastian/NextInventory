package com.sebxstt.nextinventory.instances;

import com.sebxstt.nextinventory.NextInventoryProvider;
import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.NextInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.sebxstt.nextinventory.NextInventoryProvider.mm;
import static com.sebxstt.nextinventory.InventoryHelper.*;

public class NextItem {
    private UUID id;
    private UUID parent;
    private UUID pageID;

    private String name;
    private String description;
    private Material materialType;

    private boolean draggable = true;
    private boolean registry = false;
    private boolean button = false;

    private ItemStack instance;
    private ItemMeta meta;
    private int index;

    private List<Consumer<Player>> onClickCallbacks = new ArrayList<>();

    // Main Functions
    public NextItem(String name, String description, Material materialType, NextInventory parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.materialType = materialType;

        this.parent = parent.id;
        this.instance = new ItemStack(materialType);

        this.update();

        this.index = 0;
        parent.getItems().add(this);
    }
    public void update() {
        this.meta = this.instance.getItemMeta();

        this.meta.displayName(mm.deserialize("<gradient:#ff00ff:#00ffff><bold>" + this.name + "</bold></gradient>"));

        if (!description.isBlank()) {
            List<Component> lore = new ArrayList<>();
            for (String line : description.split("\n")) {
                lore.add(mm.deserialize("<gray>" + line + "</gray>"));
            }
            this.meta.lore(lore);
        }

        this.instance.setItemMeta(this.meta);
    }
    public void render() {
        this.update();

        if (this.pageID == null) return;
        NextInventory nextInventory = next(this.parent);
        nextInventory.render();
    }

    // Internals Functions
    private void registry(boolean registry) {
        this.registry = registry;
        this.render();
    }
    private void restart() {
        if (this.pageID != null) {
            NextPage currentPage = pagination(this.pageID, this.parent);
            if (currentPage == null) throw new IllegalStateException("This page not found: " + this.pageID);
            currentPage.remove(this);
            this.pageID = null;
        }
    }

    // CallBack Functions
    public void onClick(Consumer<Player> onClickCallback) {
        if (!this.button) throw new IllegalStateException("[NextItem] This method only buttons");
        this.onClickCallbacks.add(onClickCallback);
    }

    // Emitters Functions
    public void emitClick(Player player) {
        if (!this.button) {
            System.out.println("[NextItem] emitClick llamado en ítem no marcado como botón.");
            return;
        }

        for (int i = 0; i < onClickCallbacks.size(); i++) {
            onClickCallbacks.get(i).accept(player);
        }
    }

    // Checking Functions
    public boolean isRegistry() {
        return this.registry;
    }
    public boolean isDraggable() {
        return this.draggable;
    }
    public boolean isButton() {
        return this.button;
    }

    // Getters Functions
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Material getMaterialType() {
        return materialType;
    }
    public ItemMeta getMeta() {
        return meta;
    }
    public int getIndex() {
        return index;
    }
    public UUID getParent() {
        return parent;
    }
    public ItemStack getInstance() {
        return instance;
    }
    public UUID getPageID() {
        return pageID;
    }

    // Helpers Functions
    public NextItem draggable(boolean draggable) {
        this.draggable = draggable;
        this.render();
        return this;
    }
    public NextItem button(boolean button) {
        this.button = button;
        if (!button) {
            this.onClickCallbacks.clear();
        }
        this.render();
        return this;
    }
    public NextItem page(int page) {
        NextPage pageInstance = pagination(page, this.parent);
        if (pageInstance == null) throw new IllegalStateException("Page Not Found " + page);
        pageInstance.insert(this);
        return this;
    }
    public NextItem insert() {
        NextInventory nextInventory = next(this.getParent());
        NextPage findedPage = null;

        for (NextPage nextPage : nextInventory.getPages()) {
            if (nextPage.getStack().size() >= nextPage.getMaxStack()) continue;
            findedPage = nextPage;
            break;
        }

        if (findedPage == null) throw new IllegalStateException("Not Found Page When Insert NextItem " + this.id);

        return this.insert(findedPage.getIndex());
    }
    public NextItem insert(int page) {
        NextInventory nextInventory = next(this.getParent());

        this.restart();
        this.registry(true);
        this.page(page);

        if (page == nextInventory.getCurrentPage()) {
            nextInventory.getInstance().setItem(this.getIndex(), this.getInstance());
        }

        return this;
    }
    public NextItem move(int index) {
        if (this.pageID == null) throw new IllegalStateException("Page Not Found When Move NextItem " + this.id);
        NextPage page = pagination(this.pageID, this.parent);
        if (page == null) throw new IllegalStateException("Page Not Found When Move NextItem " + this.id);
        int indexResolved = originalIndex(next(this.parent), index);

        for (UUID itemID : page.getStack()) {
            if (itemID == this.id) continue;

            NextItem item = item(itemID, this.parent);
            if (item == null) throw new IllegalStateException("Item Not Found " + itemID);
            if (item.getIndex() == indexResolved) throw new IllegalStateException("Index In Use " + item.getIndex());
        }

        this.setIndex(indexResolved);
        return this;
    }
    public NextItem move(int index, int page) {
        this.insert(page);
        return this.move(index);
    }
    public NextItem remove() {
        NextPage nextPage = pagination(this.pageID, this.parent);
        if (nextPage == null) throw new IllegalStateException("Page Not Found When Remove NextItem " + this.id);

        NextInventory nextInventory = next(this.getParent());
        nextInventory.getItems().remove(this);
        nextPage.remove(this);

        this.pageID = null;
        this.registry = false;
        this.onClickCallbacks.clear();

        nextInventory.render();
        return this;
    }

    // Setters Functions
    public void setIndex(int index) {
        this.index = index;
        this.render();
    }
    public void setName(String name) {
        this.name = name;
        this.render();
    }
    public void setDescription(String description) {
        this.description = description;
        this.render();
    }
    public void setMaterialType(Material materialType) {
        this.materialType = materialType;
        this.render();
    }
    public void setParent(UUID parent) {
        this.parent = parent;
    }
    public void setPageID(UUID pageID) {
        this.pageID = pageID;
    }
}
