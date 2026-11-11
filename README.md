# 1 SLOT SURVIVAL

Un seul slot utilisable : le **premier de la barre rapide**. Les 35 autres slots sont remplis de barrières
« SLOT BLOQUÉ ». Tout objet déposé dans un slot bloqué est éjecté au sol (jamais supprimé, jamais dupliqué)
et la barrière revient.

Disponible en **mod Fabric** et en **plugin Paper**. Téléchargement : onglet [Releases](../../releases).

| Version | Minecraft | Prérequis |
|---------|-----------|-----------|
| Mod Fabric | 1.21.11 → 26.x | Fabric Loader + **Fabric API** (Java 21 pour 1.21.11, Java 25 pour 26.x) |
| Plugin Paper | 1.21.11 → 26.x | Paper (Java 21 pour 1.21.11, Java 25 pour 26.x) |

## Installation

- **Fabric** : mettre `1-slot-survival-fabric-*.jar` et Fabric API dans le dossier `mods/` (serveur ou solo).
- **Paper** : mettre `1-slot-survival-paper-*.jar` dans le dossier `plugins/` puis redémarrer.

## Commandes

**Fabric** (opérateur) :

| Commande | Effet |
|----------|-------|
| `/function slot_lock:admin/enable` / `pause` | Active / met en pause |
| `/function slot_lock:admin/offhand_on` / `offhand_off` | Bloque / libère la main secondaire |
| `/function slot_lock:admin/status` | Compte les barrières dans ton inventaire |
| `/function slot_lock:admin/uninstall` | Retire les barrières avant de supprimer le mod |

Exempter un joueur : `/tag <joueur> add sl_exempt`.

**Paper** (`/slotsurvival`, alias `/1slot`, permission `slotsurvival.admin`) :

`enable`, `pause`, `offhand on|off`, `status`, `uninstall`.
Exempter un joueur : permission `slotsurvival.exempt`.

## Comment c'est fait

- `fabric/` : le mod embarque sa logique sous forme de données (`data/`, fonctions et advancement) que Fabric API
  charge automatiquement. Aucun code Java dépendant d'une version de Minecraft : un seul jar pour 1.21.11 → 26.x.
  Le format des composants d'objets est celui de 1.21.5+ (SNBT, `tooltip_display`) et la main secondaire est lue
  dans `equipment.offhand`.
- `paper/` : plugin Java (Maven), compilé contre l'API Paper 1.21.11 avec Java 21, sans NMS, donc compatible 26.x.
- `.github/workflows/build.yml` : compile les deux jars et publie une release quand un tag `v*` est poussé.

## Compiler soi-même

```bash
# Fabric
(cd fabric/src && zip -r ../../1-slot-survival-fabric.jar .)
# Paper
mvn -f paper/pom.xml package
```
