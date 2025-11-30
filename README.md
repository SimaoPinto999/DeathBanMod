# DeathBan Mod [Fabric]

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.x-blue?style=flat-square)
![Fabric Loader](https://img.shields.io/badge/Loader-Fabric-yellow?style=flat-square)
![License](https://img.shields.io/github/license/SimaoPinto999/DeathBanMod?style=flat-square)

---

## 💡 What is the DeathBan Mod?

The DeathBan Mod elevates the survival experience in Minecraft multiplayer by implementing an automatic, temporary ban when a player dies. It is a **Server-Side** only mod.

**Core Functionality:**
Upon dying, the player is instantly kicked from the server and cannot rejoin until their 1-hour ban timer expires. When attempting to reconnect, the player sees a clear countdown of the exact time remaining.

## ✨ Key Features

* **Death Trigger:** Detects the player death event (`ServerLivingEntityEvents.AFTER_DEATH`) and bans them instantly.
* **Persistent Data:** Uses a **JSON file** on the server (`death_bans.json`) to store the player's UUID and unban timestamp, ensuring bans persist even after the server restarts.
* **Login Barrier:** Intercepts the player's connection attempt (`ServerPlayConnectionEvents.JOIN`) and immediately kicks them with a formatted message showing the **Hours, Minutes, and Seconds** remaining.
* **Minimal Overhead:** Achieves all functionality using stable Fabric API events without requiring complex Mixins.

## 💾 Installation

Since this is strictly a **Server-Side** mod, players are **NOT required to install it** on their client.

### Server Requirements

1.  **Minecraft Server 1.21.x**
2.  **Fabric Loader** (0.15.x or higher)
3.  **Fabric API** (Required as a dependency, must be in the `mods` folder)

### Steps

1.  Download the final compiled mod file (the `.jar` file **without** the `-dev` or `-sources` suffix).
2.  Place the mod's `.jar` file and the **Fabric API** `.jar` file into your server's **`mods`** folder.
3.  Start (or restart) the server. The ban logic is now active.

## 🛠️ Configuration

Currently, the ban duration is hardcoded to **1 hour (60 minutes)** in the source code (`DeathBanManager.java`).

> **Note:** For future updates, it is highly recommended to add a configuration file (e.g., `config/deathban.json`) to allow server administrators to change the duration without recompiling the mod.

---

## 🔨 Building and Contribution

This project uses the standard Fabric Toolchain and Gradle.

* To compile the project: `.\gradlew.bat build` (Windows) or `./gradlew build` (Linux/Mac).
* The final distributable JAR is located in the **`build/libs/`** directory.

---

## 👥 Authors & Credits

* **nophi19**
* **SimaoPinto999**

