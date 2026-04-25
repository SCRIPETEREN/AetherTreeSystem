#  AetherTreeSystem Usage Guide

Complete reference for commands, permissions, and core systems.

---

##  Quick Overview

```
- Interactive tree chopping system (progress-based)
- Supports ALL log types (future-proof)
- Action bar progress UI
- Smart tree detection (no building abuse)
- Axe-based speed scaling
- Anti-spam cooldown system
- Fully configurable via config.yml
```

---

##  Basic Usage

```
1. Hold an axe
2. Look at a tree log
3. Right-click repeatedly
4. Fill progress to 100%
5. Tree breaks automatically
```

---

##  Progress System

```
[██████░░░░░░] 45%
```

```
- Bigger trees = more progress required
- Better axes = faster progress
```

---

##  Commands

```
/aethertree              -> Show plugin info
/aethertree toggle       -> Enable/disable chopping system
/aethertree reload       -> Reload config.yml
/aethertree stats        -> Show your current chopping stats
```

---

##  Permissions

```
aethertree.use        -> Use chopping system (default: true)
aethertree.admin      -> Admin commands (default: op)
aethertree.bypass     -> Bypass limits (default: op)
```

---

##  Tree Detection

```
- Detects only LOG blocks
- Requires nearby LEAVES (anti-build abuse)
- Uses Bukkit Tag system (future-proof)
- Limits max blocks per tree (anti-lag)
```

```
Supported:
OAK_LOG
BIRCH_LOG
SPRUCE_LOG
JUNGLE_LOG
ACACIA_LOG
DARK_OAK_LOG
MANGROVE_LOG
CHERRY_LOG
+ future Minecraft logs
```

---

##  Axe Speed System

```
WOODEN_AXE      -> 1.0x
STONE_AXE       -> 1.3x
IRON_AXE        -> 1.7x
DIAMOND_AXE     -> 2.2x
NETHERITE_AXE   -> 2.8x
```

```
+ Efficiency enchant increases speed
+ Fully configurable in config.yml
```

---

## ⏱ Cooldown System

```
- Prevents spam clicking
- Default: ~300ms per click
- Configurable
```

---

##  Configuration (config.yml)

```
axe-speed:
  WOODEN_AXE: 1.0
  STONE_AXE: 1.3
  IRON_AXE: 1.7
  DIAMOND_AXE: 2.2
  NETHERITE_AXE: 2.8

progress:
  base-per-click: 5
  tree-size-multiplier: 1.2

tree:
  max-blocks: 100
  require-leaves: true

cooldown:
  click-delay: 300

ui:
  symbol-filled: "█"
  symbol-empty: "░"
  length: 12
```

---

##  System Flow

```
1. Player right-clicks log
2. System validates tree
3. Creates chopping session
4. Each click:
   - Adds progress
   - Updates action bar
5. At 100%:
   - Breaks all logs
```

---

##  Notes

```
- Does not affect player-built structures (requires leaves)
- Has max block limit for performance
- Custom trees may not be detected unless using log materials
```

---

##  Tips

```
- Use higher tier axes for faster chopping
- Avoid spam clicking (cooldown applies)
- Best for survival / RPG servers
```

---

##  Developer Notes

```
Core Classes:

TreeDetectionManager -> Tree detection (BFS)
ChoppingManager      -> Progress system
AxeManager           -> Speed calculation
```

```
Designed for easy extension:
- Add skills
- Add economy rewards
- Add animations
```

---

##  Info

```
Plugin   : AetherTreeSystem
Version  : 1.0.0
Author   : YansProject
Platform : Spigot / Paper 1.21+
```
