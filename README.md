# NextInventory

<div align="center">

<img src="https://github.com/GarcesSebastian/NextInventory/blob/main/src/main/java/com/sebxstt/nextinventory/assets/logo.png?raw=true" width="150"/>

<br><br>

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen.svg)](https://www.minecraft.net/)
[![Version](https://img.shields.io/badge/Version-1.0.0--alpha-blue.svg)](https://github.com/GarcesSebastian/NextInventory)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**A powerful, flexible library for creating interactive inventory GUIs in Minecraft 1.21.4**

</div>

## 📚 Overview

NextInventory is a lightweight yet powerful library for PaperMC plugins that simplifies the creation and management of interactive inventory GUIs in Minecraft. With NextInventory, you can create dynamic, responsive, and visually appealing inventory interfaces with minimal code.

Designed with flexibility and ease of use in mind, NextInventory is perfect for plugin developers who want to enhance their user interfaces without the complexity of traditional inventory management.

### ✨ Key Features

- **Simple Builder API** - Create inventory GUIs with a fluent, chainable API
- **Pagination Support** - Easily create multi-page inventories with navigation controls
- **Dynamic Item Management** - Add, remove, or update items in real-time
- **Event Handling** - Register click callbacks for interactive elements
- **Animation Support** - Create animated items that swap materials or cycle through a list
- **Customizable Layout** - Define which slots are usable or decorative
- **Type-Safe** - Strongly-typed API for improved code reliability

## 📦 Installation

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.GarcesSebastian</groupId>
        <artifactId>nextinventory</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.GarcesSebastian:nextinventory:1.0.0'
}
```

## 🚀 Getting Started

### Basic Setup

To use NextInventory in your plugin, you need to initialize it in your plugin's `onEnable` method:

```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Initialize NextInventory with your plugin instance
        NextInventoryProvider.setup(this);
        
        // Your other plugin initialization code...
    }
}
```

### Creating a Simple Inventory

```java
// Create a basic inventory
NextInventory inventory = NextInventory.builder()
    .title("Example Inventory")
    .size(InventorySize.NORMAL)
    .type(InventoryType.NORMAL);

// Create and add an item
NextItem item = inventory.CustomItem("Click Me!", "This is a button", Material.DIAMOND, 0)
    .button(true) // Make it a clickable button
    .insert();  // Insert into the inventory

// Add a click handler
item.onClick(event -> {
    Player player = event.getPlayer();
    player.sendMessage("You clicked the button!");
});

// Open the inventory for a player
inventory.open(player.getUniqueId());
```

## 🧩 Core Components

### NextInventory

The main class representing an inventory GUI. It manages inventory state, items, pages, and event handling.

```java
NextInventory inventory = NextInventory.builder()
    .title("Example Inventory") // Set inventory title
    .size(InventorySize.LARGE)  // Set size (LITTLE, NORMAL, MEDIUM, LARGE)
    .type(InventoryType.PAGINATION) // Set type (NORMAL, PAGINATION, SCROLLING)
    .build();
```

### NextItem

Represents an item in an inventory with enhanced functionality like click handling and animations.

```java
NextItem item = inventory.CustomItem("Item Name", "Item Description", Material.DIAMOND, slotIndex)
    .button(true) // Make it interactive
    .insert();    // Add to the inventory

// Add a click handler
item.onClick(event -> {
    // Do something when clicked
});
```

### InventorySize

Predefined inventory sizes with optimized layouts:

- `LITTLE` - 27 slots (3 rows)
- `NORMAL` - 36 slots (4 rows)
- `MEDIUM` - 45 slots (5 rows)
- `LARGE` - 54 slots (6 rows)

Each size automatically defines border slots and content slots for optimal UI design.

### InventoryType

Defines the behavior of the inventory:

- `NORMAL` - Standard inventory without pagination
- `PAGINATION` - Multi-page inventory with navigation controls
- `SCROLLING` - Scrollable inventory (planned for future release)

## 🎨 Features & Examples

### Pagination

Create multi-page inventories to organize large amounts of content:

```java
// Create a paginated inventory with 4 pages
NextInventory inventory = NextInventory.builder()
    .title("My Paginated Menu")
    .size(InventorySize.LARGE)
    .type(InventoryType.PAGINATION);

inventory.pages(4); // Create 4 pages

// Customize pagination controls (optional)
inventory.getBack().setMaterialType(Material.ARROW);
inventory.getCurrent().setMaterialType(Material.PAPER);
inventory.getNext().setMaterialType(Material.ARROW);

// Add items to specific pages
for (int i = 0; i < 40; i++) {
    inventory.CustomItem("Item " + i, "Page item", Material.EMERALD, i % 10)
        .button(true)
        .insert((i / 10) + 1); // Insert into page 1, 2, 3, or 4 based on index
}

// Navigate between pages programmatically
inventory.current(2); // Go to page 2

// Override pagination button behavior
inventory.onNext(event -> {
    // Custom logic when next button is clicked
    // Call event.abort(true) to prevent default navigation
});
```

### Animated Items

Create dynamic items that change their appearance over time:

```java
// Create an item that swaps between two materials
NextItem animatedItem = inventory.CustomItem("Animated Item", "Swaps materials", Material.DIAMOND, 0)
    .button(true)
    .swap(Material.DIAMOND, Material.EMERALD, 20L) // Swap every second (20 ticks)
    .insert();

// Create an item that cycles through multiple materials
NextItem cyclingItem = inventory.CustomItem("Cycling Item", "Cycles materials", Material.DIAMOND, 1)
    .button(true)
    .cycle(List.of(
        Material.DIAMOND,
        Material.EMERALD,
        Material.GOLD_INGOT,
        Material.IRON_INGOT
    ), 15L) // Cycle every 15 ticks
    .insert();

// Stop the swap animation when needed
// animatedItem.stopSwap();

// Stop the cycle animation when needed
// cyclingItem.stopCycle();
```

### Dynamic Content

Update inventory contents based on user interactions:

```java
NextItem menuButton = inventory.CustomItem("Open Submenu", "Click to view more items", Material.COMPASS, 0)
    .button(true)
    .insert();

menuButton.onClick(event -> {
    // Clear current page or create items in a different page
    for (int i = 0; i < 10; i++) {
        NextItem dynamicItem = inventory.CustomItem(
            "Dynamic Item " + i,
            "Created on click",
            Material.BOOK,
            i
        ).button(true).insert(2);
        
        // Each dynamic item can have its own click handler
        final int index = i;
        dynamicItem.onClick(subEvent -> {
            subEvent.getPlayer().sendMessage("Selected item " + index);
            dynamicItem.remove(); // Remove the item when clicked
        });
    }
    
    inventory.current(2); // Navigate to page 2 to show the new items
});
```

### Moving Items

Reposition items within the inventory:

```java
NextItem movableItem = inventory.CustomItem("Movable Item", "Click to move", Material.ENDER_PEARL, 0)
    .button(true)
    .insert();

movableItem.onClick(event -> {
    // Move to a new position in the current page
    movableItem.move(5);
    
    // Or move to a position in another page
    // movableItem.move(3, 2); // Move to position 3 in page 2
});
```

### Advanced Layout

Use blocked and allowed slots to create structured layouts:

```java
// The InventorySize enum already defines border and content areas
// You can access these predefined layouts
List<Integer> borderSlots = inventory.getSize().getBlockedSlots();
List<Integer> contentSlots = inventory.getSize().getAllowedSlots();

// Use the contentIndex helper to get the next available content slot
int nextAvailableSlot = InventoryHelper.contentIndex(inventory, 0);
```

### Item Customization

Create richly customized items with MiniMessage formatting:

```java
NextItem customItem = inventory.CustomItem(
    "<rainbow>Rainbow Title</rainbow>", // Title using MiniMessage format
    "<gradient:#ff0000:#00ff00>Description with gradient</gradient>", // Description with MiniMessage
    Material.DIAMOND,
    0
).insert();
```

## 🔔 Events and Callbacks

NextInventory provides a robust event system to respond to user interactions. These events allow you to execute code when players interact with your interface.

### Event Types

There are two main types of events in NextInventory:

#### 1. NextClickEvent

Fires when a player clicks on an item marked as a button (`button(true)`). 

**Properties:**
- `Player getPlayer()`: Returns the player who clicked

**Registration:**
```java
NextItem button = inventory.CustomItem("Click Me", "Description", Material.DIAMOND, 0)
    .button(true)
    .insert();

button.onClick(event -> {
    Player player = event.getPlayer();
    player.sendMessage("You clicked!");
    
    // You can execute any code here
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
});
```

**When it activates:**
- When left or right clicking on an item with `button(true)`
- Does not activate if the item is not marked as a button
- Does not activate for items without registered callbacks

#### 2. NextInventoryEvent

Fires during navigation events in inventories with pagination (`InventoryType.PAGINATION`).

**Properties:**
- `Player getPlayer()`: Returns the player who triggered the event
- `boolean isAborted()`: Checks if the event has been aborted
- `void abort(boolean)`: Sets whether the event should be aborted

**Registration:**
```java
// Event when clicking the "Back" button
inventory.onBack(event -> {
    Player player = event.getPlayer();
    player.sendMessage("Navigating to previous page");
    
    // If you want to cancel the default navigation
    event.abort(true);
    
    // You can implement your own logic here
    // For example, play a sound before navigating
    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
});

// Event when clicking the "Next" button
inventory.onNext(event -> {
    // Custom logic for next page navigation
    if (specialCondition) {
        event.abort(true); // Cancel default navigation
        // Do something else instead
    }
});
```

**When it activates:**
- `onBack`: When clicking the "Back" navigation button
- `onNext`: When clicking the "Next" navigation button
- Only available in `PAGINATION` type inventories

### Advanced Event Usage

#### Callback Chain

You can register multiple callbacks for the same event:

```java
NextItem multiButton = inventory.CustomItem("Multi Action", "", Material.EMERALD, 0)
    .button(true)
    .insert();

// First callback
multiButton.onClick(event -> {
    event.getPlayer().sendMessage("Action 1");
});

// Second callback (both will execute)
multiButton.onClick(event -> {
    event.getPlayer().sendMessage("Action 2");
});
```

#### Conditional Events

You can implement conditional logic within your callbacks:

```java
NextItem conditionalButton = inventory.CustomItem("Conditional", "", Material.GOLD_INGOT, 0)
    .button(true)
    .insert();

conditionalButton.onClick(event -> {
    Player player = event.getPlayer();
    
    if (player.hasPermission("myplugin.admin")) {
        // Admin actions
        player.sendMessage("Admin access granted");
    } else {
        // Normal user actions
        player.sendMessage("You don't have sufficient permissions");
    }
});
```

#### One-Time Callbacks

Implement callbacks that execute only once:

```java
NextItem oneTimeButton = inventory.CustomItem("One-time use", "", Material.REDSTONE, 0)
    .button(true)
    .insert();

// Variable to track if it's been used
final boolean[] used = {false};

oneTimeButton.onClick(event -> {
    if (!used[0]) {
        event.getPlayer().sendMessage("Button activated!");
        used[0] = true;
        
        // Change appearance to show it's been used
        oneTimeButton.setMaterialType(Material.GRAY_DYE);
        oneTimeButton.setName("Already used");
    } else {
        event.getPlayer().sendMessage("This button has already been used");
    }
});
```

### Composite Events

Combine multiple events to create complex interactions:

```java
// Example: Two-step confirmation
NextItem dangerButton = inventory.CustomItem("Danger!", "Click to confirm", Material.TNT, 0)
    .button(true)
    .insert();

dangerButton.onClick(event -> {
    Player player = event.getPlayer();
    
    // Change the button to a confirmation button
    dangerButton.setName("CONFIRM?");
    dangerButton.setDescription("Click again to confirm dangerous action");
    dangerButton.setMaterialType(Material.REDSTONE_BLOCK);
    
    // Replace the callback with a new one
    // Note: This requires a custom implementation,
    // as you cannot directly replace existing callbacks
    
    // One solution is to use a state variable
    final boolean[] confirmationMode = {true};
    
    dangerButton.onClick(confirmEvent -> {
        if (confirmationMode[0]) {
            player.sendMessage("Dangerous action executed!");
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f);
            
            // Restore button after a delay
            Bukkit.getScheduler().runTaskLater(NextInventoryProvider.plugin, () -> {
                dangerButton.setName("Danger!");
                dangerButton.setDescription("Click to confirm");
                dangerButton.setMaterialType(Material.TNT);
                confirmationMode[0] = false;
            }, 60L); // 3 seconds
        }
    });
});
```

### Best Practices for Events

1. **Organization**: Keep event registrations close to item creation for better readability.

2. **Exception Handling**: Always handle potential exceptions within your callbacks to prevent errors that could affect the server.
   ```java
   button.onClick(event -> {
       try {
           // Your logic here
       } catch (Exception e) {
           System.out.println("Error in callback: " + e.getMessage());
           e.printStackTrace();
       }
   });
   ```

3. **Weak References**: Avoid creating memory leaks by maintaining strong references to temporary objects within your callbacks.

4. **Separation of Concerns**: Extract complex logic to separate methods to keep your callbacks clean and readable.
   ```java
   button.onClick(event -> handleButtonClick(event.getPlayer()));
   
   private void handleButtonClick(Player player) {
       // Complex logic here
   }
   ```

## 🔌 API Reference

### NextInventory Methods

| Method | Description |
|--------|-------------|
| `builder()` | Creates a new inventory builder |
| `title(String)` | Sets the inventory title |
| `size(InventorySize)` | Sets the inventory size |
| `type(InventoryType)` | Sets the inventory type |
| `open(UUID)` | Opens the inventory for a player |
| `close(UUID)` | Closes the inventory for a player |
| `pages(int)` | Creates multiple pages for pagination |
| `current(int)` | Navigates to a specific page |
| `CustomItem(...)` | Creates a new item in this inventory |
| `destroy()` | Cleans up and destroys the inventory |
| `render()` | Updates the inventory display |
| `onBack(Consumer<NextInventoryEvent>)` | Sets callback for back navigation event |
| `onNext(Consumer<NextInventoryEvent>)` | Sets callback for next navigation event |

### NextItem Methods

| Method | Description |
|--------|-------------|
| `button(boolean)` | Sets whether the item is interactive |
| `draggable(boolean)` | Sets whether the item can be dragged |
| `onClick(Consumer<NextClickEvent>)` | Registers a click handler |
| `swap(Material, Material, Long)` | Creates a swapping animation |
| `cycle(List<Material>, Long)` | Creates a cycling animation |
| `insert()` | Adds the item to the inventory |
| `insert(int)` | Adds the item to a specific page |
| `move(int)` | Moves the item to a new position |
| `remove()` | Removes the item from the inventory |

## ⚠️ Current Limitations

- Currently only supports Minecraft 1.21.4
- Future versions will add support for Minecraft 1.21.X and 1.20.X
- The SCROLLING inventory type is planned for a future release

## 📝 License

NextInventory is available under the MIT License. See the LICENSE file for more information.

## 🔧 Integration with Plugins

NextInventory is designed to integrate easily with any PaperMC plugin. Here we show an example of how to integrate it into an existing plugin:

### Example: Team System

```java
// GUI manager class for teams
public class TeamGUI {
    public static final NextInventory NextGUI = NextInventory.builder()
            .title("Team Manager")
            .type(InventoryType.PAGINATION)
            .size(InventorySize.LARGE);

    public static void setup() {
        NextGUI.pages(4); // Create GUI with 4 pages

        // Customize navigation buttons (optional)
        // NextGUI.getBack().setMaterialType(Material.ARROW);
        // NextGUI.getCurrent().setMaterialType(Material.PAPER);
        // NextGUI.getNext().setMaterialType(Material.ARROW);

        // Create main menu buttons
        NextItem storageButton = NextGUI.CustomItem("Open Storage Team", "", Material.GREEN_STAINED_GLASS, 1)
                .button(true) // It's an interactive button
                .swap(Material.DIAMOND, Material.EMERALD, 10L) // Alternate between materials
                .insert(); // Insert into inventory
                
        NextItem warpsButton = NextGUI.CustomItem("Warps Team", "", Material.BLUE_STAINED_GLASS, 3)
                .button(true)
                .cycle(List.of(
                        Material.AIR,
                        Material.BLUE_STAINED_GLASS_PANE,
                        Material.AIR,
                        Material.RED_STAINED_GLASS_PANE
                ), 10L) // Cycle between materials
                .insert();
                
        NextItem exitButton = NextGUI.CustomItem("Exit", "", Material.RED_STAINED_GLASS, 5)
                .button(true)
                .insert();

        // Add event handlers for each button
        storageButton.onClick(event -> {
            // Create items dynamically when clicked
            int count = 0;
            for (Player plr : Bukkit.getOnlinePlayers()) {
                NextItem playerItem = NextGUI.CustomItem(plr.getName(), "Team Player", Material.EMERALD, count)
                        .button(true)
                        .insert(2); // Create custom item on page 2

                playerItem.onClick(eventClick -> {
                    playerItem.remove(); // Remove item when clicked
                });
                count++;
            }

            NextGUI.current(2); // Go to page 2
        });

        // Configure other event handlers...

        // Control navigation events
        NextGUI.onBack(event -> {
            event.abort(true); // Cancel default navigation
            // Custom logic here
        });
    }

    // Method to open the menu for a player
    public static void openMenu(Player player) {
        NextGUI.open(player.getUniqueId());
    }
}
```

### Initialization in the Main Plugin

```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Initialize NextInventory
        NextInventoryProvider.setup(this);
        
        // Configure menus
        TeamGUI.setup();
        
        // Register commands
        getCommand("team").setExecutor((sender, cmd, label, args) -> {
            if (sender instanceof Player player) {
                TeamGUI.openMenu(player);
                return true;
            }
            return false;
        });
    }
    
    @Override
    public void onDisable() {
        // Clean up resources if needed
    }
}
```

## 🛠️ Troubleshooting

### Common Issues

#### Items don't appear in the inventory

- Check that you're using the `.insert()` method after creating each item
- Make sure the item index is within the valid range for the inventory size
- For paginated inventories, confirm you're inserting on the correct page

#### Click events don't activate

- Verify that you've set `.button(true)` on the item
- Ensure that NextInventoryProvider is correctly initialized with your plugin instance
- Check that no other plugins might be cancelling inventory events

#### Animations don't work

- Animations require the plugin to be running (they don't work in development mode without a server)
- Verify that the time interval (in ticks) isn't too small
- Make sure the specified materials are valid

#### Concurrency errors

- Avoid modifying inventories from multiple threads
- Use `Bukkit.getScheduler().runTask(plugin, () -> {})` to ensure inventory modifications happen on the main thread

## 💡 Best Practices

### Performance

- **Reuse inventories** when possible instead of creating new ones
- **Limit animations** to important items to reduce CPU usage
- **Destroy inventories** that are no longer in use with the `destroy()` method
- **Group content updates** and then call `render()` just once

### Code Organization

- Create a separate class for each type of menu in your plugin
- Use constants for common item indices for better maintainability
- Separate item creation logic from event handling logic

### User Experience

- Use consistent materials for similar functions across all your menus
- Include clear instructions in item descriptions
- Implement sound effects to confirm actions (using Bukkit API)
- Add confirmations for destructive actions

## 📋 Roadmap

These are the features planned for future versions:

- **v1.1.0**
  - Support for Minecraft versions 1.21.X
  - Implementation of the SCROLLING inventory type
  - API for integrated sound effects

- **v1.2.0**
  - Support for Minecraft versions 1.20.X
  - Condition system to show/hide items based on permissions
  - Loading GUI designs from configuration files (YAML/JSON)

- **v1.3.0**
  - Support for earlier versions (1.19.X)
  - Menu template system
  - Integration with PlaceholderAPI

## 🔍 Example Projects

Here are some plugins that use NextInventory:

- [**Sebxstt**](https://github.com/GarcesSebastian/Sebxstt-Teams) - Team management system with interactive menus
- *Do you use NextInventory in your plugin? Let me know to add it here!*

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

### Steps to contribute

1. Fork the repository
2. Create a branch for your feature (`git checkout -b feature/new-function`)
3. Make your changes and add tests if possible
4. Commit the changes (`git commit -m 'Add new feature'`)
5. Push to the branch (`git push origin feature/new-function`)
6. Open a Pull Request

---

<div align="center">

**Made with ❤️ by [GarcesSebastian](https://github.com/GarcesSebastian)**

</div>
