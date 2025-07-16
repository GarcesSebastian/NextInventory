# NextInventory

<div align="center">

<img src="https://github.com/GarcesSebastian/NextInventory/blob/main/src/main/java/com/sebxstt/nextinventory/assets/logo.png?raw=true" width="150"/>

<br><br>

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

**Una biblioteca moderna y elegante para crear menús GUI en Minecraft**

</div>

## 🚀 Descripción

NextInventory es una biblioteca eficiente y fácil de usar para crear interfaces gráficas de usuario basadas en inventarios para plugins de Minecraft. Está diseñada para simplificar la creación de menús interactivos complejos con características como paginación, animaciones de ítems y manejo dinámico de eventos.

## ✨ Características Principales

- 🧩 **API Fluida** - Sintaxis clara y encadenada para crear inventarios
- 📑 **Paginación** - Soporte integrado para inventarios multipágina
- ⚡ **Eventos Personalizados** - Sistema robusto de manejo de eventos
- 🎬 **Animaciones** - Animaciones de ítems con ciclos y alternancia
- 🔄 **Contenido Dinámico** - Actualiza los inventarios en tiempo real
- 🎨 **Personalización Completa** - Control total sobre los diseños y apariencia
- 🧰 **Compatibilidad con MiniMessage** - Soporte para textos formateados y gradientes

## 📦 Instalación

### Maven

```xml
<repository>
    <id>nextinventory-repo</id>
    <url>https://repo.example.com/maven-public/</url>
</repository>

<dependency>
    <groupId>com.sebxstt</groupId>
    <artifactId>nextinventory</artifactId>
    <version>0.1.0-alpha</version>
    <scope>compile</scope>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url "https://repo.example.com/maven-public/" }
}

dependencies {
    implementation 'com.sebxstt:nextinventory:0.1.0-alpha'
}
```

## 🔧 Primeros Pasos

### Paso 1: Inicializar la biblioteca

```java
import com.sebxstt.nextinventory.NextInventoryProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Inicializar NextInventory con la instancia de tu plugin
        NextInventoryProvider.setup(this);
        
        // Tu otro código de inicialización...
    }
}
```

### Paso 2: Crear un inventario básico

```java
// Crear un inventario básico
NextInventory inventory = NextInventory.builder()
    .title("Mi Inventario")
    .size(InventorySize.NORMAL) // 36 slots (4 filas)
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

La clase principal para gestionar tus inventarios. Proporciona métodos para crear y configurar el inventario, gestionar páginas, agregar ítems y controlar la visualización.

### NextItem

Representa un ítem dentro del inventario. Proporciona métodos para configurar propiedades de los ítems, añadir manejadores de eventos y crear animaciones.

### Enumeraciones

- **InventorySize**: Define tamaños de inventario predefinidos:
  - `LITTLE` (9 slots)
  - `NORMAL` (36 slots)
  - `MEDIUM` (45 slots)
  - `LARGE` (54 slots)

- **InventoryType**: Define el comportamiento del inventario:
  - `NORMAL` (Inventario estándar)
  - `PAGINATION` (Inventario con múltiples páginas)
  - `SCROLLING` (Inventario con desplazamiento, próximamente)

## 🎨 Características y Ejemplos

### Paginación

Crea inventarios multipágina para organizar grandes cantidades de contenido:

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
    .swap(Material.DIAMOND, Material.EMERALD, 10L) // Alternar cada 10 ticks
    .insert();

// Detener la animación de alternancia cuando sea necesario
// animatedItem.stopSwap();

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
            subEvent.getPlayer().sendMessage("Ítem seleccionado " + index);
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
    // Mover el ítem a una nueva posición
    movableItem.move(8); // Mover a la posición 8
    
    // También puedes mover ítems a diferentes páginas
    // Primero reinsertar en la página deseada, luego mover
    movableItem.remove();
    movableItem.insert(2); // Insertar en la página 2
    movableItem.move(4);   // Mover a la posición 4 en la página 2
});
```

### Diseños Avanzados

Crear diseños complejos con ítems de relleno:

```java
// Crear un inventario con un diseño de borde
NextInventory inventory = NextInventory.builder()
    .title("Inventario con Borde")
    .size(InventorySize.LARGE)
    .type(InventoryType.NORMAL);

// Crear ítems de borde
Material borderMaterial = Material.BLACK_STAINED_GLASS_PANE;
// Borde superior
for (int i = 0; i < 9; i++) {
    inventory.CustomItem(" ", "", borderMaterial, i).insert();
}
// Bordes izquierdo y derecho
for (int row = 1; row < 5; row++) {
    inventory.CustomItem(" ", "", borderMaterial, row * 9).insert();
    inventory.CustomItem(" ", "", borderMaterial, row * 9 + 8).insert();
}
// Borde inferior
for (int i = 45; i < 54; i++) {
    inventory.CustomItem(" ", "", borderMaterial, i).insert();
}

// Ahora añadir ítems dentro del borde
inventory.CustomItem("Ítem Central", "Este ítem está en el centro", Material.NETHER_STAR, 22)
    .button(true)
    .insert();
```

### Personalización de Ítems

Personaliza los ítems con MiniMessage para textos con formato:

```java
// Usar MiniMessage para texto formateado
NextItem customItem = inventory.CustomItem(
    "<rainbow>Arcoíris Increíble</rainbow>", // Título con MiniMessage
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

Actualmente, NextInventory solo es compatible con **Minecraft 1.21.4**.

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
