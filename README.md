# AetherTreeSystem

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Paper/Spigot](https://img.shields.io/badge/API-Paper%2FSpigot%201.21%2B-green.svg)](https://papermc.io/)
[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)]
[![Open Source](https://img.shields.io/badge/Open%20Source-Yes-success.svg)]

AetherTreeSystem is a modern, performance-focused tree chopping plugin for Minecraft (Paper/Spigot 1.21+) designed to replace instant tree breaking with a progressive, interactive system. Built with scalability and clean architecture in mind, it provides a smooth gameplay experience while remaining lightweight and fully configurable.

> Latest: v2.0.0 — Initial stable release

---

## Core Features

* Progressive tree chopping system (no instant break)
* Supports all log types using Bukkit Tag system (future-proof)
* Action bar progress UI with configurable style
* Smart tree detection (prevents building abuse)
* Axe-based speed scaling with enchantment support
* Cooldown system to prevent spam clicking
* Config-driven balancing (speed, limits, UI)
* Lightweight and optimized for performance
* Modular architecture for easy expansion

---

## How It Works

1. Player right-clicks a log block
2. System validates if it is a natural tree
3. A chopping session is created
4. Each click increases progress
5. Action bar updates in real-time
6. At 100% progress, all logs are broken

---

## Command Reference

```bash
/aethertree              # Show plugin info
/aethertree toggle       # Enable or disable system
/aethertree reload       # Reload configuration
/aethertree stats        # View player chopping stats
```

---

## Permissions

```bash
aethertree.use        # Use chopping system (default: true)
aethertree.admin      # Admin commands (default: op)
aethertree.bypass     # Bypass limits/restrictions (default: op)
```

---

## Installation

### Quick Setup

1. Download the latest jar file
2. Place it in the `plugins/` folder
3. Start or restart your server
4. Edit `config.yml` if needed
5. Use `/aethertree reload` after changes

---

## Configuration Highlights

```yaml
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

## Technical Overview

* Uses BFS (flood-fill) for tree detection
* Utilizes `Tag.LOGS` and `Tag.LEAVES` for compatibility
* Session-based progress tracking per player
* Optimized to prevent excessive block updates
* Designed to avoid lag on large trees

---

## Compatibility

* Paper / Spigot 1.21+
* Java 21+
* Works with all vanilla wood types
* Compatible with most survival and RPG servers

---

## Limitations

* Requires leaves nearby to validate natural trees
* Custom trees may not be detected if not using log materials
* Large structures are limited by max-blocks setting

---

## Developer Notes

Core structure:

```bash
TreeDetectionManager   # Handles tree scanning (BFS)
ChoppingManager        # Handles progress and sessions
AxeManager             # Calculates speed modifiers
```

Designed for extension:

* Skill systems
* Economy rewards
* Custom animations
* Integration with other plugins

---

## Building

```bash
mvn clean package
```

---

## Documentation

* GUIDE.md — Usage and command reference

---

## Author

YansProject

---

## License

This project is licensed under the MIT License.
