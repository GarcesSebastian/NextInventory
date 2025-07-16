# NextInventory

<div align="center">

<img src="https://github.com/GarcesSebastian/NextInventory/blob/main/src/main/java/com/sebxstt/nextinventory/assets/logo.png?raw=true" width="150"/>


[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen.svg)](https://www.minecraft.net/)
[![Version](https://img.shields.io/badge/Version-1.0.0--alpha-blue.svg)](https://github.com/GarcesSebastian/NextInventory)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Una biblioteca potente y flexible para crear GUIs de inventario interactivas en Minecraft 1.21.4**

</div>

## 📚 Descripción general

NextInventory es una biblioteca ligera pero potente para plugins de PaperMC que simplifica la creación y gestión de GUIs de inventario interactivas en Minecraft. Con NextInventory, puedes crear interfaces de inventario dinámicas, receptivas y visualmente atractivas con un código mínimo.

Diseñado con flexibilidad y facilidad de uso en mente, NextInventory es perfecto para desarrolladores de plugins que desean mejorar sus interfaces de usuario sin la complejidad de la gestión tradicional de inventarios.

### ✨ Características Principales

- **API de Constructor Simple** - Crea GUIs de inventario con una API fluida y encadenable
- **Soporte de Paginación** - Crea fácilmente inventarios de múltiples páginas con controles de navegación
- **Gestión Dinámica de Ítems** - Añade, elimina o actualiza ítems en tiempo real
- **Manejo de Eventos** - Registra callbacks de clic para elementos interactivos
- **Soporte de Animaciones** - Crea ítems animados que cambian de materiales o realizan ciclos
- **Diseño Personalizable** - Define qué slots son utilizables o decorativos
- **Tipado Seguro** - API con tipos fuertes para mejorar la fiabilidad del código

## 📦 Instalación

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

## 🚀 Primeros Pasos

### Configuración Básica

Para usar NextInventory en tu plugin, necesitas inicializarlo en el método `onEnable` de tu plugin:

```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Inicializar NextInventory con la instancia de tu plugin
        NextInventoryProvider.setup(this);
        
        // Tu otro código de inicialización del plugin...
    }
}
```

### Creando un Inventario Simple

```java
// Crear un inventario básico
NextInventory inventory = NextInventory.builder()
    .title("Inventario de Ejemplo")
    .size(InventorySize.NORMAL)
    .type(InventoryType.NORMAL);

// Crear y añadir un ítem
NextItem item = inventory.CustomItem("¡Haz clic!", "Este es un botón", Material.DIAMOND, 0)
    .button(true) // Convertirlo en un botón clickeable
    .insert();  // Insertar en el inventario

// Añadir un manejador de clic
item.onClick(event -> {
    Player player = event.getPlayer();
    player.sendMessage("¡Has hecho clic en el botón!");
});

// Abrir el inventario para un jugador
inventory.open(player.getUniqueId());
```

## 🧩 Componentes Principales

### NextInventory

La clase principal que representa una interfaz gráfica de inventario. Gestiona el estado del inventario, los ítems, páginas y manejo de eventos.

```java
NextInventory inventory = NextInventory.builder()
    .title("Inventario de Ejemplo") // Establece el título del inventario
    .size(InventorySize.LARGE)  // Establece el tamaño (LITTLE, NORMAL, MEDIUM, LARGE)
    .type(InventoryType.PAGINATION) // Establece el tipo (NORMAL, PAGINATION, SCROLLING)
    .build();
```

### NextItem

Representa un ítem en un inventario con funcionalidad mejorada como manejo de clics y animaciones.

```java
NextItem item = inventory.CustomItem("Nombre del Ítem", "Descripción del Ítem", Material.DIAMOND, indicePosicion)
    .button(true) // Hacerlo interactivo
    .insert();    // Añadir al inventario

// Añadir un manejador de clic
item.onClick(event -> {
    // Hacer algo cuando se hace clic
});
```

### InventorySize

Tamaños de inventario predefinidos con diseños optimizados:

- `LITTLE` - 27 slots (3 filas)
- `NORMAL` - 36 slots (4 filas)
- `MEDIUM` - 45 slots (5 filas)
- `LARGE` - 54 slots (6 filas)

Cada tamaño define automáticamente slots de borde y slots de contenido para un diseño de interfaz óptimo.

### InventoryType

Define el comportamiento del inventario:

- `NORMAL` - Inventario estándar sin paginación
- `PAGINATION` - Inventario multi-página con controles de navegación
- `SCROLLING` - Inventario con desplazamiento (planificado para una futura versión)

## 🎨 Características y Ejemplos

### Paginación

Crea inventarios multi-página para organizar grandes cantidades de contenido:

```java
// Crear un inventario paginado con 4 páginas
NextInventory inventory = NextInventory.builder()
    .title("Mi Menú Paginado")
    .size(InventorySize.LARGE)
    .type(InventoryType.PAGINATION);

inventory.pages(4); // Crear 4 páginas

// Personalizar controles de paginación (opcional)
inventory.getBack().setMaterialType(Material.ARROW);
inventory.getCurrent().setMaterialType(Material.PAPER);
inventory.getNext().setMaterialType(Material.ARROW);

// Añadir ítems a páginas específicas
for (int i = 0; i < 40; i++) {
    inventory.CustomItem("Ítem " + i, "Ítem de la página", Material.EMERALD, i % 10)
        .button(true)
        .insert((i / 10) + 1); // Insertar en la página 1, 2, 3, o 4 basado en el índice
}

// Navegar entre páginas programáticamente
inventory.current(2); // Ir a la página 2

// Sobrescribir comportamiento de botones de paginación
inventory.onNext(event -> {
    // Lógica personalizada cuando se hace clic en el botón siguiente
    // Llama a event.abort(true) para prevenir la navegación predeterminada
});
```

### Ítems Animados

Crea ítems dinámicos que cambian su apariencia con el tiempo:

```java
// Crear un ítem que alterna entre dos materiales
NextItem animatedItem = inventory.CustomItem("Ítem Animado", "Alterna materiales", Material.DIAMOND, 0)
    .button(true)
    .swap(Material.DIAMOND, Material.EMERALD, 20L) // Alternar cada segundo (20 ticks)
    .insert();

// Crear un ítem que cicla entre múltiples materiales
NextItem cyclingItem = inventory.CustomItem("Ítem Cíclico", "Cicla materiales", Material.DIAMOND, 1)
    .button(true)
    .cycle(List.of(
        Material.DIAMOND,
        Material.EMERALD,
        Material.GOLD_INGOT,
        Material.IRON_INGOT
    ), 15L) // Ciclar cada 15 ticks
    .insert();

// Detener la animación de alternancia cuando sea necesario
// animatedItem.stopSwap();

// Detener la animación de ciclo cuando sea necesario
// cyclingItem.stopCycle();
```

### Contenido Dinámico

Actualiza el contenido del inventario basado en interacciones del usuario:

```java
NextItem menuButton = inventory.CustomItem("Abrir Submenú", "Haz clic para ver más ítems", Material.COMPASS, 0)
    .button(true)
    .insert();

menuButton.onClick(event -> {
    // Limpiar la página actual o crear ítems en una página diferente
    for (int i = 0; i < 10; i++) {
        NextItem dynamicItem = inventory.CustomItem(
            "Ítem Dinámico " + i,
            "Creado al hacer clic",
            Material.BOOK,
            i
        ).button(true).insert(2);
        
        // Cada ítem dinámico puede tener su propio manejador de clic
        final int index = i;
        dynamicItem.onClick(subEvent -> {
            subEvent.getPlayer().sendMessage("Seleccionado ítem " + index);
            dynamicItem.remove(); // Eliminar el ítem cuando se hace clic
        });
    }
    
    inventory.current(2); // Navegar a la página 2 para mostrar los nuevos ítems
});
```

### Mover Ítems

Reposicionar ítems dentro del inventario:

```java
NextItem movableItem = inventory.CustomItem("Ítem Móvil", "Haz clic para mover", Material.ENDER_PEARL, 0)
    .button(true)
    .insert();

movableItem.onClick(event -> {
    // Mover a una nueva posición en la página actual
    movableItem.move(5);
    
    // O mover a una posición en otra página
    // movableItem.move(3, 2); // Mover a la posición 3 en la página 2
});
```

### Diseño Avanzado

Utiliza slots bloqueados y permitidos para crear diseños estructurados:

```java
// La enumeración InventorySize ya define áreas de borde y contenido
// Puedes acceder a estos diseños predefinidos
List<Integer> borderSlots = inventory.getSize().getBlockedSlots();
List<Integer> contentSlots = inventory.getSize().getAllowedSlots();

// Usa el helper contentIndex para obtener el siguiente slot de contenido disponible
int nextAvailableSlot = InventoryHelper.contentIndex(inventory, 0);
```

### Personalización de Ítems

Crea ítems con personalización avanzada utilizando formato MiniMessage:

```java
NextItem customItem = inventory.CustomItem(
    "<rainbow>Título Arcoíris</rainbow>", // Título usando formato MiniMessage
    "<gradient:#ff0000:#00ff00>Descripción con gradiente</gradient>", // Descripción con MiniMessage
    Material.DIAMOND,
    0
).insert();
```

## 🔔 Eventos y Callbacks

NextInventory proporciona un sistema robusto de eventos para responder a las interacciones del usuario. Estos eventos te permiten ejecutar código cuando los jugadores interactúan con tu interfaz.

### Tipos de Eventos

Existen dos tipos principales de eventos en NextInventory:

#### 1. NextClickEvent

Se dispara cuando un jugador hace clic en un ítem marcado como botón (`button(true)`). 

**Propiedades:**
- `Player getPlayer()`: Devuelve el jugador que realizó el clic

**Registro:**
```java
NextItem button = inventory.CustomItem("Haz Clic", "Descripción", Material.DIAMOND, 0)
    .button(true)
    .insert();

button.onClick(event -> {
    Player player = event.getPlayer();
    player.sendMessage("¡Has hecho clic!");
    
    // Puedes ejecutar cualquier código aquí
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
});
```

**Cuándo se activa:**
- Al hacer clic izquierdo o derecho en un ítem con `button(true)`
- No se activa si el ítem no está marcado como botón
- No se activa para ítems sin callbacks registrados

#### 2. NextInventoryEvent

Se dispara durante eventos de navegación en inventarios con paginación (`InventoryType.PAGINATION`).

**Propiedades:**
- `Player getPlayer()`: Devuelve el jugador que activó el evento
- `boolean isAborted()`: Verifica si el evento ha sido abortado
- `void abort(boolean)`: Establece si el evento debe ser abortado

**Registro:**
```java
// Evento al hacer clic en el botón "Anterior"
inventory.onBack(event -> {
    Player player = event.getPlayer();
    player.sendMessage("Navegando a la página anterior");
    
    // Si quieres cancelar la navegación predeterminada
    event.abort(true);
    
    // Puedes implementar tu propia lógica aquí
    // Por ejemplo, reproducir un sonido antes de navegar
    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
});

// Evento al hacer clic en el botón "Siguiente"
inventory.onNext(event -> {
    // Lógica personalizada para navegación a siguiente página
    if (condicionEspecial) {
        event.abort(true); // Cancelar navegación predeterminada
        // Hacer otra cosa en su lugar
    }
});
```

**Cuándo se activa:**
- `onBack`: Al hacer clic en el botón de navegación "Anterior"
- `onNext`: Al hacer clic en el botón de navegación "Siguiente"
- Solo están disponibles en inventarios de tipo `PAGINATION`

### Uso Avanzado de Eventos

#### Cadena de Callbacks

Puedes registrar múltiples callbacks para un mismo evento:

```java
NextItem multiButton = inventory.CustomItem("Acción Múltiple", "", Material.EMERALD, 0)
    .button(true)
    .insert();

// Primer callback
multiButton.onClick(event -> {
    event.getPlayer().sendMessage("Acción 1");
});

// Segundo callback (ambos se ejecutarán)
multiButton.onClick(event -> {
    event.getPlayer().sendMessage("Acción 2");
});
```

#### Eventos Condicionales

Puedes implementar lógica condicional dentro de tus callbacks:

```java
NextItem conditionalButton = inventory.CustomItem("Condicional", "", Material.GOLD_INGOT, 0)
    .button(true)
    .insert();

conditionalButton.onClick(event -> {
    Player player = event.getPlayer();
    
    if (player.hasPermission("myplugin.admin")) {
        // Acciones para administradores
        player.sendMessage("Acceso admin concedido");
    } else {
        // Acciones para usuarios normales
        player.sendMessage("No tienes permisos suficientes");
    }
});
```

#### Callbacks de Un Solo Uso

Implementa callbacks que se ejecuten una sola vez:

```java
NextItem oneTimeButton = inventory.CustomItem("Un solo uso", "", Material.REDSTONE, 0)
    .button(true)
    .insert();

// Variable para rastrear si ya se usó
final boolean[] used = {false};

oneTimeButton.onClick(event -> {
    if (!used[0]) {
        event.getPlayer().sendMessage("¡Botón activado!");
        used[0] = true;
        
        // Cambiar apariencia para mostrar que está usado
        oneTimeButton.setMaterialType(Material.GRAY_DYE);
        oneTimeButton.setName("Ya usado");
    } else {
        event.getPlayer().sendMessage("Este botón ya ha sido usado");
    }
});
```

### Eventos Compuestos

Combina varios eventos para crear interacciones complejas:

```java
// Ejemplo: Confirmación en dos pasos
NextItem dangerButton = inventory.CustomItem("¡Peligro!", "Haz clic para confirmar", Material.TNT, 0)
    .button(true)
    .insert();

dangerButton.onClick(event -> {
    Player player = event.getPlayer();
    
    // Cambia el botón a un botón de confirmación
    dangerButton.setName("¿CONFIRMAR?");
    dangerButton.setDescription("Haz clic de nuevo para confirmar la acción peligrosa");
    dangerButton.setMaterialType(Material.REDSTONE_BLOCK);
    
    // Reemplaza el callback con uno nuevo
    // Nota: Esto requiere una implementación personalizada,
    // ya que no puedes reemplazar directamente los callbacks existentes
    
    // Una solución es usar una variable de estado
    final boolean[] confirmationMode = {true};
    
    dangerButton.onClick(confirmEvent -> {
        if (confirmationMode[0]) {
            player.sendMessage("¡Acción peligrosa ejecutada!");
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f);
            
            // Restaurar botón después de un tiempo
            Bukkit.getScheduler().runTaskLater(NextInventoryProvider.plugin, () -> {
                dangerButton.setName("¡Peligro!");
                dangerButton.setDescription("Haz clic para confirmar");
                dangerButton.setMaterialType(Material.TNT);
                confirmationMode[0] = false;
            }, 60L); // 3 segundos
        }
    });
});
```

### Mejores Prácticas para Eventos

1. **Organización**: Mantén los registros de eventos cerca de la creación de los ítems para mejor legibilidad.

2. **Manejo de Excepciones**: Siempre maneja posibles excepciones dentro de tus callbacks para evitar errores que puedan afectar al servidor.
   ```java
   button.onClick(event -> {
       try {
           // Tu lógica aquí
       } catch (Exception e) {
           System.out.println("Error en callback: " + e.getMessage());
           e.printStackTrace();
       }
   });
   ```

3. **Referencias Débiles**: Evita crear memory leaks manteniendo referencias fuertes a objetos temporales dentro de tus callbacks.

4. **Separación de Responsabilidades**: Extrae la lógica compleja a métodos separados para mantener tus callbacks limpios y legibles.
   ```java
   button.onClick(event -> handleButtonClick(event.getPlayer()));
   
   private void handleButtonClick(Player player) {
       // Lógica compleja aquí
   }
   ```

## 🔌 Referencia de API

### Métodos de NextInventory

| Método | Descripción |
|--------|-------------|
| `builder()` | Crea un nuevo constructor de inventario |
| `title(String)` | Establece el título del inventario |
| `size(InventorySize)` | Establece el tamaño del inventario |
| `type(InventoryType)` | Establece el tipo de inventario |
| `open(UUID)` | Abre el inventario para un jugador |
| `close(UUID)` | Cierra el inventario para un jugador |
| `pages(int)` | Crea múltiples páginas para paginación |
| `current(int)` | Navega a una página específica |
| `CustomItem(...)` | Crea un nuevo ítem en este inventario |
| `destroy()` | Limpia y destruye el inventario |
| `render()` | Actualiza la visualización del inventario |
| `onBack(Consumer<NextInventoryEvent>)` | Establece callback para evento de navegación hacia atrás |
| `onNext(Consumer<NextInventoryEvent>)` | Establece callback para evento de navegación hacia adelante |

### Métodos de NextItem

| Método | Descripción |
|--------|-------------|
| `button(boolean)` | Establece si el ítem es interactivo |
| `draggable(boolean)` | Establece si el ítem puede ser arrastrado |
| `onClick(Consumer<NextClickEvent>)` | Registra un manejador de clics |
| `swap(Material, Material, Long)` | Crea una animación de alternancia |
| `cycle(List<Material>, Long)` | Crea una animación cíclica |
| `insert()` | Añade el ítem al inventario |
| `insert(int)` | Añade el ítem a una página específica |
| `move(int)` | Mueve el ítem a una nueva posición |
| `remove()` | Elimina el ítem del inventario |
| `stopSwap()` | Detiene la animación de alternancia |
| `stopCycle()` | Detiene la animación cíclica |

## 🛑 Limitaciones y Compatibilidad

- **Versiones de Minecraft**: NextInventory está diseñado y probado para Minecraft 1.21.4. La compatibilidad con versiones anteriores no está garantizada.

- **Limitaciones de Paginación**: Se recomienda un máximo de 100 páginas para evitar problemas de rendimiento.

- **Animaciones**: Las animaciones consumen recursos del servidor. Limita el número de ítems animados por inventario.

- **Referencias Circulares**: Ten cuidado con las referencias circulares en callbacks que pueden causar memory leaks.

Versiones futuras incluirán soporte para:
- Minecraft 1.21.X
- Minecraft 1.20.X (planificado para versiones posteriores)

## 🔧 Integración con Ejemplo

Aquí hay un ejemplo completo de cómo integrar NextInventory en un plugin:

```java
package com.tuPlugin;

import com.sebxstt.nextinventory.NextInventory;
import com.sebxstt.nextinventory.NextInventoryProvider;
import com.sebxstt.nextinventory.enums.InventorySize;
import com.sebxstt.nextinventory.enums.InventoryType;
import com.sebxstt.nextinventory.instances.NextItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TuPlugin extends JavaPlugin {
    
    private NextInventory mainMenu;
    
    @Override
    public void onEnable() {
        // Inicializar NextInventory
        NextInventoryProvider.setup(this);
        
        // Configurar los menús
        setupMenus();
        
        // Registrar comando
        getCommand("menu").setExecutor(new MenuCommand());
        
        getLogger().info("¡Plugin habilitado con NextInventory!");
    }
    
    private void setupMenus() {
        // Crear el menú principal
        mainMenu = NextInventory.builder()
                .title("Menú Principal")
                .size(InventorySize.NORMAL)
                .type(InventoryType.NORMAL);
        
        // Añadir ítems al menú
        NextItem infoButton = mainMenu.CustomItem("Información", "Muestra información del servidor", Material.BOOK, 11)
                .button(true)
                .insert();
                
        NextItem settingsButton = mainMenu.CustomItem("Configuración", "Ajustar preferencias", Material.COMPASS, 13)
                .button(true)
                .insert();
                
        NextItem exitButton = mainMenu.CustomItem("Salir", "Cerrar este menú", Material.BARRIER, 15)
                .button(true)
                .insert();
        
        // Configurar eventos de click
        infoButton.onClick(event -> {
            Player player = event.getPlayer();
            player.sendMessage("§aInformación del servidor:");
            player.sendMessage("§7- Versión: 1.21.4");
            player.sendMessage("§7- Jugadores: " + getServer().getOnlinePlayers().size());
            player.sendMessage("§7- TPS: " + getServer().getTPS()[0]);
        });
        
        settingsButton.onClick(event -> {
            // Aquí podrías abrir otro inventario con opciones de configuración
            player.sendMessage("§aMenú de configuración próximamente");
        });
        
        exitButton.onClick(event -> {
            mainMenu.close(event.getPlayer().getUniqueId());
        });
    }
    
    private class MenuCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cSolo los jugadores pueden usar este comando.");
                return true;
            }
            
            Player player = (Player) sender;
            mainMenu.open(player.getUniqueId());
            return true;
        }
    }
}
```

## ❓ Solución de Problemas

### Los ítems no aparecen

- Asegúrate de llamar a `insert()` después de crear un ítem
- Verifica que el índice del ítem esté dentro del rango válido para el tamaño del inventario
- Asegúrate de llamar a `render()` si has hecho cambios que requieren una actualización manual

### Los eventos no se disparan

- Comprueba que has marcado el ítem como botón con `button(true)`
- Verifica que has registrado correctamente el callback con `onClick()`
- Asegúrate de que NextInventory está inicializado correctamente con `NextInventoryProvider.setup(plugin)`

### Problemas con las animaciones

- Las animaciones solo se ejecutan cuando el inventario está abierto
- Si necesitas detener las animaciones, utiliza `stopSwap()` o `stopCycle()`
- Para un mejor rendimiento, usa intervalos más largos para las animaciones (> 10 ticks)

### Problemas de concurrencia

- NextInventory maneja las actualizaciones de inventario de forma asíncrona
- Evita modificar el mismo ítem o inventario desde múltiples hilos
- Si tienes problemas de concurrencia, considera usar el scheduler de Bukkit para sincronizar cambios

## 🚀 Mejores Prácticas

### Rendimiento

- No crees demasiados inventarios dinámicamente; reutiliza instancias cuando sea posible
- Limita el número de animaciones por inventario
- Llama a `destroy()` cuando un inventario ya no sea necesario

### Organización del Código

- Define inventarios como variables estáticas si son reutilizables
- Separa la lógica de eventos en métodos para mayor claridad
- Utiliza enums para categorizar diferentes tipos de menús en tu plugin

### Experiencia de Usuario

- Usa nombres descriptivos para los ítems y descripciones claras
- Implementa efectos de sonido para la interacción con botones
- Proporciona retroalimentación visual cuando los botones son presionados
- Mantén una navegación coherente entre los menús

## 🗺️ Roadmap

- **v0.1.0-alpha**
  - Versión inicial con funcionalidades básicas
  - Soporte para Minecraft 1.21.4

- **v0.2.0**
  - Mejoras de rendimiento
  - Inventarios con desplazamiento (Scrolling)
  - Soporte para atributos de ítem adicionales (encantamientos, flags)

- **v1.0.0**
  - API estabilizada
  - Soporte completo para todas las características de inventarios
  - Documentación exhaustiva y más ejemplos

- **v1.1.0**
  - Soporte para versiones de Minecraft 1.20.X
  - Sistema de condiciones para mostrar/ocultar ítems basado en permisos
  - Carga de diseños de GUI desde archivos de configuración (YAML/JSON)

- **v1.3.0**
  - Soporte para versiones anteriores (1.19.X)
  - Sistema de plantillas de menú
  - Integración con PlaceholderAPI

## 🔍 Ejemplos de Proyectos

Aquí hay algunos plugins que utilizan NextInventory:

- [**Sebxstt**](https://github.com/GarcesSebastian/Sebxstt-Teams) - Sistema de gestión de equipos con menús interactivos
- *¿Utilizas NextInventory en tu plugin? ¡Házmelo saber para añadirlo aquí!*

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! No dudes en abrir issues o enviar pull requests.

### Pasos para contribuir

1. Fork el repositorio
2. Crea una rama para tu característica (`git checkout -b feature/nueva-funcion`)
3. Realiza tus cambios y añade pruebas si es posible
4. Commit los cambios (`git commit -m 'Añade nueva función'`)
5. Push a la rama (`git push origin feature/nueva-funcion`)
6. Abre un Pull Request

---

<div align="center">

**Made with ❤️ by [GarcesSebastian](https://github.com/GarcesSebastian)**

</div>
