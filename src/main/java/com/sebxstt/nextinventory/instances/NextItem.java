package com.sebxstt.nextinventory.instances;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.sebxstt.nextinventory.NextInventoryProvider;
import com.sebxstt.nextinventory.events.NextClickEvent;
import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.instances.http.FetchProfile;
import com.sebxstt.nextinventory.instances.http.FetchTextures;
import com.sebxstt.nextinventory.managers.HttpManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.sebxstt.nextinventory.NextInventoryProvider.mm;
import static com.sebxstt.nextinventory.InventoryHelper.*;

public class NextItem {
    private UUID id;
    private UUID parent;
    private Integer currentPage;

    private String name;
    private String description;
    private Material materialType;
    private PlayerProfile OwnerProfile;
    private String OnlinePlayer;
    private URL ProfileTexture;
    private FetchTextures TexturesPlayer;

    private boolean draggable = true;
    private boolean registry = false;
    private boolean button = false;
    private boolean head = false;

    private ItemStack instance;
    private int index;

    private List<Consumer<NextClickEvent>> onClickCallbacks = new ArrayList<>();

    private BukkitTask SwapConnection;
    private BukkitTask CycleConnection;

    // Main Functions
    public NextItem(String name, String description, Material materialType, NextInventory parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.materialType = materialType;

        this.parent = parent.getId();
        this.instance = new ItemStack(materialType);

        this.update();

        this.index = 0;
        parent.getItems().add(this);
    }
    public void update() {
        this.instance = new ItemStack(this.materialType);
        ItemMeta meta = this.instance.getItemMeta();

        if (this.OwnerProfile != null) {
            ((SkullMeta) meta).setPlayerProfile(this.OwnerProfile);
        }

        if (this.OnlinePlayer != null) {
            PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID(), this.OnlinePlayer);
            playerProfile.getProperties().add(new ProfileProperty("textures", this.TexturesPlayer.texture));
            ((SkullMeta) meta).setPlayerProfile(playerProfile);
        }

        if (materialType == null || materialType == Material.AIR) {
            throw new IllegalStateException("[NextItem] Material inválido para el ítem.");
        }

        meta.displayName(mm.deserialize("<gradient:#ff00ff:#00ffff><bold>" + this.name + "</bold></gradient>"));

        if (!description.isBlank()) {
            List<Component> lore = new ArrayList<>();
            for (String line : description.split("\n")) {
                lore.add(mm.deserialize("<gray>" + line + "</gray>"));
            }
            meta.lore(lore);
        }

        this.instance.setItemMeta(meta);
    }
    public void render() {
        this.update();

        if (this.currentPage == null) return;

        NextInventory nextInventory = next(this.parent);
        nextInventory.render();
    }
    public void stopSwap() {
        if (this.SwapConnection != null && !this.SwapConnection.isCancelled()) {
            this.SwapConnection.cancel();
            this.SwapConnection = null;
        }
    }
    public void stopCycle(){
        if (this.CycleConnection != null && !this.CycleConnection.isCancelled()) {
            this.CycleConnection.cancel();
            this.CycleConnection = null;
        }
    }

    // Internals Functions
    private void registry(boolean registry) {
        this.registry = registry;
        this.render();
    }
    private void restart() {
        if (this.currentPage != null) {
            NextPage currentPage = pagination(this.currentPage, this.parent);
            if (currentPage == null) throw new IllegalStateException("This page not found: " + this.currentPage);
            currentPage.remove(this);
            this.currentPage = null;
        }
    }

    // CallBack Functions
    public void onClick(Consumer<NextClickEvent> onClickCallback) {
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
            NextClickEvent event = new NextClickEvent(player);
            onClickCallbacks.get(i).accept(event);
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
    public int getIndex() {
        return index;
    }
    public UUID getParent() {
        return parent;
    }
    public ItemStack getInstance() {
        return instance;
    }
    public Integer getCurrentPage() {
        return currentPage;
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
    private NextItem page(int page) {
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
        if (this.currentPage == null) throw new IllegalStateException("Page Not Found When Move NextItem " + this.id);
        NextPage page = pagination(this.currentPage, this.parent);
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
        NextPage nextPage = pagination(this.currentPage, this.parent);
        if (nextPage == null) throw new IllegalStateException("Page Not Found When Remove NextItem " + this.id);

        NextInventory nextInventory = next(this.getParent());
        nextInventory.getItems().remove(this);
        nextPage.remove(this);

        this.currentPage = null;
        this.registry = false;
        this.onClickCallbacks.clear();

        nextInventory.render();
        return this;
    }
    public NextItem swap(Material MaterialA, Material MaterialB, Long interval) {
        Material[] materials = new Material[]{MaterialA, MaterialB};

        this.SwapConnection = new BukkitRunnable() {
            private int index = 0;

            @Override
            public void run() {
                setMaterialType(materials[index]);
                index = (index + 1) % materials.length;
            }
        }.runTaskTimer(NextInventoryProvider.plugin, 0L, interval);

        return this;
    }
    public NextItem cycle(List<Material> materials, long interval) {
        this.CycleConnection = new BukkitRunnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index >= materials.size()) index = 0;
                setMaterialType(materials.get(index));
                index++;
            }
        }.runTaskTimer(NextInventoryProvider.plugin, 0L, interval);

        return this;
    }
    public NextItem head(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);

        if (player != null) {
            this.OwnerProfile = player.getPlayerProfile();
        }

        return this;
    }
    public NextItem headOnline(String onlinePlayer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FetchProfile profilePlayer;
                FetchTextures texturesPlayer;

                try {
                    profilePlayer = HttpManager.getProfile(onlinePlayer);
                    texturesPlayer = HttpManager.getTextures(profilePlayer.id);
                } catch (Exception e) {
                    Bukkit.getLogger().severe("[NextInventory] Failed to fetch profile/textures for: " + onlinePlayer);
                    throw new RuntimeException(e);
                }

                TexturesPlayer = texturesPlayer;

                byte[] decodedBytes = Base64.getDecoder().decode(texturesPlayer.texture);
                String decodedJson = new String(decodedBytes);

                try {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(decodedJson);

                    JSONObject textures = (JSONObject) json.get("textures");
                    JSONObject skin = (JSONObject) textures.get("SKIN");
                    ProfileTexture = new URL((String) skin.get("url"));
                    OnlinePlayer = (String) json.get("profileName");
                } catch (ParseException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                if (ProfileTexture == null) {
                    throw new IllegalStateException("Profile texture URL is null for " + onlinePlayer);
                }

                setMaterialType(Material.PLAYER_HEAD);
                render();
            }
        }.runTask(NextInventoryProvider.plugin);

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
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }
}
