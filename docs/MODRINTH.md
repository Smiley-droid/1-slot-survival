# Modrinth listing (proposal)

## Summary (max 256 characters)

Only one usable inventory slot. Every other slot is locked by a barrier, and anything dropped into a locked slot is thrown on the ground, never lost. Fabric and Paper, Minecraft 1.21.11 to 26.x, translated into 51 languages.

## Description (Markdown)

# 1 SLOT SURVIVAL

**One usable slot. Everything else is locked.**

You can only use the **first slot of your hotbar**. The other 35 inventory slots are filled with **"LOCKED SLOT"** barriers.
If an item lands in a locked slot (pickup, crafting, drag and drop, commands…), it is **thrown on the ground** in front of you.
**Nothing is ever deleted or duplicated**, and the barrier comes back.

A tiny survival challenge, a minigame rule, or a hardcore twist for your server: how far can you go with a single slot?

## Features

- **One slot only** – the first hotbar slot; the other 35 are blocked
- **Safe by design** – items are dropped, never deleted or duplicated
- **Real commands** – `/slotsurvival enable | pause | offhand on|off | status | uninstall`
- **Optional offhand lock** – block the offhand too
- **Per-player exemptions** – let admins or builders play normally
- **Works in every mode** – survival, adventure and creative (spectators are ignored)
- **Clean uninstall** – one command removes every barrier
- **51 languages** – English, French, Spanish, German, Italian, Portuguese, Russian, Polish, Turkish, Japanese, Korean, Chinese and many more
- **Server-side logic** – no client mod required on Paper; on Fabric the mod is only needed on the client for translations
- **Tested on real servers** – every release is checked on Fabric 1.21.11 / 26.3 and Paper 1.21.11 / 26.3

## Versions

| File | Minecraft | Needs |
|------|-----------|-------|
| Fabric (1.21.11) | 1.21.11 | Fabric Loader, Fabric API, Java 21 |
| Fabric (26.x) | 26.1+ | Fabric Loader, Fabric API, Java 25 |
| Paper plugin | 1.21.11 and 26.x | Paper, Java 21 or 25 |

## Commands

| Command | Effect |
|---------|--------|
| `/slotsurvival enable` | Enable the system |
| `/slotsurvival pause` | Pause it |
| `/slotsurvival offhand on/off` | Lock or free the offhand |
| `/slotsurvival status` | Show state and barrier count |
| `/slotsurvival uninstall` | Remove all barriers before deleting the mod |

Operators only (Paper permission: `slotsurvival.admin`). Exempt a player with `slotsurvival.exempt` (Paper) or `/tag <player> add sl_exempt` (Fabric).

## Languages

Chat messages, the action bar and the barrier name follow the player's Minecraft language.
On Paper the server translates for each player. On Fabric the translation happens on the client, so install the mod there too (players without it see English).

## Links

- Source code and issues: https://github.com/Smiley-droid/1-slot-survival
- Full documentation: see the README on GitHub
