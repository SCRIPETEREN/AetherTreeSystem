# AetherTreeSystem Project Structure

AetherTreeSystem uses a clean, modular architecture focused on performance, scalability, and safe block handling. The system is designed to be lightweight while still allowing easy expansion and customization.

---

## Package Overview (`me.aether.treesystem`)

| Package            | Responsibility                                                      |
| :----------------- | :------------------------------------------------------------------ |
| `AetherTreeSystem` | Plugin lifecycle, initialization, command and listener registration |
| `listener`         | Handles player interaction events (right-click chopping trigger)    |
| `manager`          | Core logic: tree detection, chopping system, axe speed calculation  |
| `model`            | Data models for active chopping sessions                            |
| `util`             | Helper utilities (action bar, block checks, math calculations)      |
| `config`           | Configuration loader and value management                           |
| `command`          | Command handling for player and admin interactions                  |

---

## Core Architecture

The plugin is divided into clear layers:

```text
Player Input (Listener)
        ↓
ChoppingManager (Session Control)
        ↓
TreeDetectionManager (Tree Scan)
        ↓
AxeManager (Speed Calculation)
        ↓
ActionBar + Block Break Execution
```

---

## Data Model

### `ChoppingSession`

Stores active chopping state per player:

```java
Player player
List<Block> logs
int progress
int maxProgress
Location origin
```

### Session Management

Handled in `ChoppingManager`:

* `Map<UUID, ChoppingSession>` — Active sessions
* `Map<UUID, Long>` — Click cooldown tracking

---

## Tree Detection System

### `TreeDetectionManager`

Responsible for detecting valid trees using BFS (flood-fill).

Key logic:

* Uses `Tag.LOGS` for log detection (future-proof)
* Uses `Tag.LEAVES` for validation
* Limits scan size to prevent lag
* Prevents breaking player-built structures

### Detection Flow

```text
Start from clicked block
    ↓
Check if block is LOG
    ↓
Scan nearby blocks (BFS)
    ↓
Collect connected LOG blocks
    ↓
Validate nearby LEAVES
    ↓
Return valid tree logs
```

---

## Chopping System

### `ChoppingManager`

Main system controller:

* Creates session on first click
* Tracks progress per player
* Applies cooldown between clicks
* Updates action bar
* Breaks tree at 100%

### Flow

```text
Player clicks log
    ↓
Check cooldown
    ↓
Get or create session
    ↓
Calculate speed (AxeManager)
    ↓
Add progress
    ↓
Update UI
    ↓
If progress >= 100% → break all logs
```

---

## Axe System

### `AxeManager`

Handles speed calculation:

* Reads values from config.yml
* Detects axe type
* Applies multiplier
* Includes enchantment scaling (e.g. Efficiency)

---

## Utility Layer

### `BlockUtil`

```java
isLog(Material type)
isLeaves(Material type)
```

Uses Bukkit `Tag` system for compatibility with future updates.

---

### `ActionBarUtil`

Sends formatted progress bar:

```text
[██████░░░░░░] 45%
```

Fully customizable via config.

---

### `MathUtil`

Handles calculations:

* Progress scaling based on tree size
* Progress per click
* Percentage conversion

---

## Configuration System

### `ConfigManager`

Loads and provides access to:

* Axe speeds
* Progress values
* Tree limits
* Cooldown settings
* UI formatting

All values are adjustable without code changes.

---

## Performance Considerations

* BFS scan limited by `max-blocks`
* No unnecessary block updates
* Session-based system reduces repeated scanning
* Cooldown prevents excessive event spam

---

## Safety Design

| Concern                    | Handling Method                  |
| :------------------------- | :------------------------------- |
| Server lag on large trees  | Max block scan limit             |
| Spam clicking              | Cooldown system                  |
| Breaking player structures | Leaves validation required       |
| Future Minecraft updates   | Uses `Tag.LOGS` and `Tag.LEAVES` |
| Multiple sessions conflict | Per-player session isolation     |

---

## Thread Safety

| Component         | Mechanism                    |
| :---------------- | :--------------------------- |
| Session map       | HashMap (main-thread only)   |
| Cooldown tracking | Timestamp-based control      |
| Block operations  | Bukkit main thread execution |

Note: All logic runs on the main server thread to comply with Bukkit API safety rules.

---

## Build & Deployment

```bash
mvn clean package
```

Output:

```bash
target/AetherTreeSystem-1.0.0.jar
```

---

## Runtime Requirements

* Java 21+
* Paper / Spigot 1.21+
* No external dependencies required

---

## Extension Possibilities

The system is designed for easy expansion:

* Skill progression system
* Economy rewards for chopping
* Tree animations or effects
* Integration with RPG plugins

---

## License

MIT License — See LICENSE file
