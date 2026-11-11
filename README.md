# 1 SLOT SURVIVAL

Datapack Minecraft Java : **un seul slot utilisable** (le premier de la barre rapide).
Les 35 autres slots sont remplis de barrières « SLOT BLOQUÉ ». Tout objet déposé dans un slot
bloqué est éjecté au sol (jamais supprimé, jamais dupliqué) et la barrière revient.

## Versions supportées

| Minecraft | Data pack format |
|-----------|------------------|
| 1.21.11   | 94.1             |
| 26.1.x    | 101.1            |
| 26.2      | 107.1            |
| 26.3      | 121.0            |

`pack.mcmeta` déclare `min_format: [94, 1]` et `max_format: 121`.
Les versions 1.21.9 / 1.21.10 et antérieures ne sont **pas** supportées (syntaxe des composants différente).

## Installation

1. Télécharger le dépôt en ZIP (ou le dossier du pack) et le placer dans `<monde>/datapacks/`
   (le `pack.mcmeta` doit être à la racine du ZIP / du dossier).
2. `/reload` ou relancer le monde.

## Commandes admin

| Commande | Effet |
|----------|-------|
| `/function slot_lock:admin/enable` | Active le système |
| `/function slot_lock:admin/pause` | Met en pause |
| `/function slot_lock:admin/offhand_on` / `offhand_off` | Bloque / libère la main secondaire |
| `/function slot_lock:admin/status` | Compte les barrières dans ton inventaire |
| `/function slot_lock:admin/uninstall` | Retire toutes les barrières avant de désactiver le pack |

Pour exempter un joueur : `/tag <joueur> add sl_exempt`.

## Changements par rapport à la version 1.21.1

- `pack.mcmeta` : `pack_format` → `min_format` / `max_format`.
- Composants de la barrière au format 1.21.5+ : `custom_name` / `lore` en SNBT,
  `enchantments` simplifié, `tooltip_display` à la place de `show_in_tooltip`.
- Main secondaire lue dans `equipment.offhand` (n'est plus dans `Inventory` depuis 1.21.5).
- Les macros `fix` / `eject` utilisent désormais un chemin NBT (`path`) au lieu d'un index de slot.

## Tests rapides (en jeu)

`/function slot_lock:test/slot0`, `test/diamond`, `test/many`.
