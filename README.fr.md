# 1 SLOT SURVIVAL

[English](README.md) · **Français**

> Un seul slot utilisable. Tout le reste est bloqué.

Dans **1 SLOT SURVIVAL**, tu ne peux utiliser que le **premier slot de ta barre rapide**. Les 35 autres emplacements de
l'inventaire sont remplis de barrières **« SLOT BLOQUÉ »** inutilisables. Si un objet atterrit dans un slot bloqué
(ramassage, craft, glisser-déposer, commande…), il est **éjecté au sol** devant toi : jamais supprimé, jamais dupliqué,
et la barrière revient.

Disponible en **mod Fabric** et en **plugin Paper**, traduit en **51 langues**.

## Téléchargement

Les `.jar` sont dans l'onglet **[Releases](../../releases)**.

| Fichier | Pour | Minecraft | Prérequis |
|---------|------|-----------|-----------|
| `1-slot-survival-fabric-1.21.11-*.jar` | Fabric (solo ou serveur) | 1.21.11 | Fabric Loader, **Fabric API**, Java 21 |
| `1-slot-survival-fabric-26.x-*.jar` | Fabric (solo ou serveur) | 26.1 et plus récent | Fabric Loader, **Fabric API**, Java 25 |
| `1-slot-survival-paper-*.jar` | Serveurs Paper | 1.21.11 et 26.x | Paper, Java 21 (1.21.11) ou 25 (26.x) |

Chaque release est compilée par GitHub Actions et testée sur de vrais serveurs (**Fabric 1.21.11 et 26.3, Paper 1.21.11 et 26.3**)
avant publication. Les autres versions 26.x devraient fonctionner mais ne sont pas testées une par une.

## Installation

**Fabric** : installe [Fabric Loader](https://fabricmc.net/use/), puis mets **Fabric API** et le bon `1-slot-survival-fabric-*.jar`
dans `mods/` (client en solo, serveur en multijoueur). Seul le serveur (ou le solo) a besoin du mod ; installe-le aussi chez les joueurs
pour avoir les messages dans leur langue.

**Paper** : mets `1-slot-survival-paper-*.jar` dans `plugins/`, redémarre le serveur (pas de `/reload`).
Les réglages sont dans `plugins/SlotSurvival/config.yml`.

## Commandes

La commande est la même sur les deux plateformes : **`/slotsurvival`**.

| Commande | Effet |
|----------|-------|
| `/slotsurvival enable` | Active le système |
| `/slotsurvival pause` | Met en pause |
| `/slotsurvival offhand on` | Bloque aussi la main secondaire |
| `/slotsurvival offhand off` | Libère la main secondaire (défaut) |
| `/slotsurvival status` | Affiche l'état et le nombre de barrières dans ton inventaire |
| `/slotsurvival uninstall` | Désactive le système et retire toutes les barrières, avant de supprimer le mod/plugin |

- **Paper** : permission `slotsurvival.admin` (opérateurs par défaut). Alias : `/slotsurv`, `/1slot`. Diagnostic : `/slotsurvival selftest`.
- **Fabric** : opérateurs uniquement. En solo, crée le monde avec les **triches activées**.

**Exempter un joueur** : Paper → permission `slotsurvival.exempt`. Fabric → `/tag <joueur> add sl_exempt`, puis
`/clear <joueur> minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]` pour retirer ses barrières.

## Langues

Les messages (chat, barre d'action, nom et description de la barrière) sont traduits en **51 langues (67 variantes)**,
l'anglais étant la langue par défaut. **Paper** traduit côté serveur, pour chaque joueur, quel que soit son client.
**Fabric** traduit côté joueur : le mod doit être installé chez lui, sinon il voit l'anglais.

Détail et contribution : voir le [README anglais](README.md#languages).

## Dépannage

| Problème | Solution |
|----------|----------|
| `/slotsurvival` ne répond pas | Tu n'es pas opérateur (`/op <pseudo>` depuis la console), ou en solo Fabric les triches sont désactivées. |
| Le mod Fabric ne se charge pas | Vérifie **Fabric API**, le bon jar (1.21.11 ou 26.x) et le bon Java (21 ou 25). |
| Le plugin Paper ne se charge pas | Vérifie la version de Java et cherche `SlotSurvival` dans les logs. |
| Messages en anglais (Fabric) | Le mod doit aussi être installé chez le joueur. |

Pour les développeurs, la compilation et la publication : voir le [README anglais](README.md#for-developers).
