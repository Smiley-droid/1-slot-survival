# 1 SLOT SURVIVAL

**English** · [Français](README.fr.md)

> One usable inventory slot. Everything else is locked.

In **1 SLOT SURVIVAL** you can only use the **first slot of your hotbar**. The other 35 inventory slots are filled with
**"LOCKED SLOT"** barriers that cannot be used. If an item ends up in a locked slot (pickup, crafting, drag and drop,
commands…), it is **thrown on the ground** in front of you: items are never deleted or duplicated, and the barrier comes back.

Available as a **Fabric mod** and a **Paper plugin**, translated into **51 languages**.

## Downloads

Get the `.jar` files from the **[Releases](../../releases)** tab.

| File | For | Minecraft | Requirements |
|------|-----|-----------|--------------|
| `1-slot-survival-fabric-1.21.11-*.jar` | Fabric (singleplayer or server) | 1.21.11 | Fabric Loader, **Fabric API**, Java 21 |
| `1-slot-survival-fabric-26.x-*.jar` | Fabric (singleplayer or server) | 26.1 and newer | Fabric Loader, **Fabric API**, Java 25 |
| `1-slot-survival-paper-*.jar` | Paper servers | 1.21.11 and 26.x | Paper, Java 21 (1.21.11) or 25 (26.x) |

Every release is built by GitHub Actions and tested on real servers (**Fabric 1.21.11 and 26.3, Paper 1.21.11 and 26.3**)
before it is published. Other 26.x versions should work but are not tested individually.

## Installation

**Fabric**
1. Install [Fabric Loader](https://fabricmc.net/use/) for your Minecraft version.
2. Put **Fabric API** and the matching `1-slot-survival-fabric-*.jar` in the `mods/` folder (client for singleplayer, server for multiplayer).
3. Start the game. The system is active as soon as you enter the world.

Only the server (or singleplayer) needs the mod. Install it on clients too if you want messages in each player's language (see [Languages](#languages)).

**Paper**
1. Put `1-slot-survival-paper-*.jar` in the `plugins/` folder.
2. Restart the server (do not use `/reload`).
3. Settings are in `plugins/SlotSurvival/config.yml`.

## Commands

The command is the same on both platforms: **`/slotsurvival`**.

| Command | Effect |
|---------|--------|
| `/slotsurvival enable` | Enable the system |
| `/slotsurvival pause` | Pause the system |
| `/slotsurvival offhand on` | Also lock the offhand |
| `/slotsurvival offhand off` | Free the offhand (default) |
| `/slotsurvival status` | Show the state and how many lock barriers are in your inventory |
| `/slotsurvival uninstall` | Disable the system and remove every barrier, before deleting the mod/plugin |

**Who can use it**
- **Paper**: permission `slotsurvival.admin` (operators by default). Alias: `/slotsurv`, `/1slot`.
- **Fabric**: operators only (the command runs the mod's functions, which require the same permission as `/function`).
  In singleplayer, create the world with **cheats enabled** or open it to LAN with cheats on.

**Exempting a player**

| | |
|---|---|
| Paper | give them the permission `slotsurvival.exempt` (their barriers are removed automatically) |
| Fabric | `/tag <player> add sl_exempt`, then `/clear <player> minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]` to remove existing barriers. Undo with `/tag <player> remove sl_exempt` |

Paper also has `/slotsurvival selftest`, a diagnostic that prints `PASS`/`FAIL` for each API the plugin uses.

**Paper configuration** (`config.yml`, also changed by the commands above):

```yaml
enabled: true         # system active
block-offhand: false  # also lock the offhand
```

## How it works

- **Free slot**: the first hotbar slot. The other 8 hotbar slots and the 27 main inventory slots are locked.
- **Safe ejection**: an item found in a locked slot is dropped on the ground with a short pickup delay (2.5 s).
  If the drop fails, the item stays with the player. Nothing is ever deleted.
- **Checks**: on every inventory change, plus a safety sweep every 0.5 s.
- **Barriers**: they cannot be dropped, placed as blocks, or kept after death. A barrier lying on the ground is removed.
- **Armor**: not affected, it works normally.
- **Offhand**: free by default, lockable with `offhand on`.
- **Creative mode**: the lock applies too. **Spectator mode**: ignored.
- **New players** get an explanation message the first time they join.

### Fabric vs Paper

| | Fabric mod | Paper plugin |
|---|---|---|
| Click on a barrier | Detected and fixed within half a second | **Blocked immediately** |
| `pause` | Existing barriers stay in inventories | Barriers are removed automatically |
| Exempted player | Remove barriers with `/clear` | Removed automatically |
| Settings | World scoreboard | `config.yml` |
| Translation | On the player's side (needs the mod on the client) | On the server (works with any client) |

## Languages

Messages (chat, action bar, barrier name and lore) are translated into **51 languages (67 regional variants)**.
The text follows the language selected in the Minecraft options. English is the default.

| | |
|---|---|
| Western Europe | English (US, GB, AU, CA, NZ), French (FR, CA), Spanish (ES, MX, AR, CL, EC, UY, VE), German (DE, AT, CH), Italian, Portuguese (PT, BR), Dutch (NL, BE), Catalan, Galician, Basque |
| Northern Europe | Swedish, Norwegian, Danish, Finnish, Icelandic, Estonian, Latvian, Lithuanian |
| Central and Eastern Europe | Polish, Czech, Slovak, Slovenian, Croatian, Bosnian, Serbian, Macedonian, Bulgarian, Romanian, Hungarian, Russian, Ukrainian, Belarusian, Albanian, Greek |
| Middle East and Asia | Turkish, Hebrew, Arabic, Persian, Hindi, Bengali, Thai, Vietnamese, Indonesian, Malay, Filipino, Japanese, Korean, Chinese (Simplified, Traditional, Hong Kong) |
| Other | Afrikaans |

- **Paper**: translated by the server, per player, with any client.
- **Fabric**: translated by the client. It works when the mod is installed on the player's side (singleplayer, or a server whose players also have the mod). Players without the mod see **English**.

The translations were written without a professional translator, so rarer languages may contain mistakes.
To add or fix a language, copy `fabric/common/src/main/resources/assets/slot_survival/lang/en_us.json` to `<language_code>.json`
(for example `ga_ie.json`), translate the values (keep the `%s` in `slot_survival.msg.status`) and open a pull request.
`ci/check_lang.py` validates the files on every push. Both the Fabric mod and the Paper plugin use the same files.

## Quick test

On Fabric, as an operator:

| Command | Expected result |
|---------|-----------------|
| `/function slot_lock:test/slot0` | 16 emeralds in slot 1: they **stay** |
| `/function slot_lock:test/diamond` | Diamonds in a locked slot: they are **ejected** and the barrier returns |
| `/function slot_lock:test/many` | Several items in several locked slots: all ejected |

On Paper, the same check works with the vanilla command:

```mcfunction
/item replace entity @s inventory.13 with minecraft:diamond 64
```

## Uninstalling cleanly

1. Run `/slotsurvival uninstall` **while the players are online**: their barriers are removed.
2. Then delete the `.jar`.

A player who was offline keeps their barriers. They can remove them with
`/clear @s minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]` when they come back
(on Paper, barriers are removed automatically when a player joins while the system is disabled).

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `/slotsurvival` does nothing or is unknown | You are not an operator (`/op <name>` from the server console), or on Fabric singleplayer cheats are off. |
| The Fabric mod does not load | Check that **Fabric API** is installed, that you use the jar matching your version (1.21.11 or 26.x) and the right Java (21 or 25). |
| The Paper plugin does not load | Check the Java version (21 for 1.21.11, 25 for 26.x) and the server log for `SlotSurvival`. |
| A player is not locked | They may be exempted, in spectator mode, or the system is paused. |
| Barriers remain after `pause` (Fabric) | Expected: use `uninstall` or `/clear`. |
| Messages are in English | On Fabric, the mod must also be installed on the player's client. |

## For developers

```
fabric/common/src/main/java/        mod entry point and the /slotsurvival command
fabric/common/src/main/resources/   data/ (functions, advancement), assets/ (translations), fabric.mod.json
fabric/mc-1.21.11/  fabric/mc-26/   two Gradle builds sharing the code above (Loom, Mojang mappings)
paper/                              Paper plugin (Maven, Java 21, Paper API 1.21.11, no NMS)
ci/                                 translation checks and server-test helpers
.github/workflows/build.yml         builds, runs real-server tests, publishes the release on a v* tag
```

Build it yourself:

```bash
# Fabric (Gradle, JDK 21 for 1.21.11 and JDK 25 for 26.x)
gradle -p fabric/mc-1.21.11 build
gradle -p fabric/mc-26 build

# Paper (Maven, JDK 21)
mvn -f paper/pom.xml package
```

Release a version: bump `mod_version` in both `gradle.properties` files and `<version>` in `paper/pom.xml`, then push a tag:

```bash
git tag v3.0.1 && git push origin v3.0.1
```
