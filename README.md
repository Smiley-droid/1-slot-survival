# 1 SLOT SURVIVAL

> Un seul slot utilisable. Tout le reste est bloqué.

Dans **1 SLOT SURVIVAL**, tu ne peux utiliser que le **premier slot de ta barre rapide**. Les 35 autres
emplacements de l'inventaire sont remplis de barrières **« SLOT BLOQUÉ »** impossibles à utiliser.
Si un objet atterrit dans un slot bloqué (ramassage, craft, glisser-déposer, commande…), il est
**éjecté au sol** devant toi : il n'est jamais supprimé ni dupliqué, et la barrière revient.

Disponible en deux versions :

| Version | Pour | Minecraft | Prérequis |
|---------|------|-----------|-----------|
| **Mod Fabric** | Solo et serveurs Fabric | 1.21.11 → 26.x | Fabric Loader + **Fabric API** |
| **Plugin Paper** | Serveurs Paper | 1.21.11 → 26.x | Paper |

Java : **21** pour Minecraft 1.21.11, **25** pour Minecraft 26.x.

## Téléchargement

Les fichiers `.jar` sont dans l'onglet **[Releases](../../releases)** :

- `1-slot-survival-fabric-<version>.jar` : mod Fabric
- `1-slot-survival-paper-<version>.jar` : plugin Paper

Ils sont compilés automatiquement par GitHub Actions à chaque nouvelle version, et testés sur de vrais serveurs **Paper 26.3** et **Fabric 26.3** (chargement, commandes, éjection d'objets).

## Installation

### Mod Fabric
1. Installe [Fabric Loader](https://fabricmc.net/use/) pour ta version de Minecraft.
2. Mets **Fabric API** et `1-slot-survival-fabric-*.jar` dans le dossier `mods/`
   (du client en solo, ou du serveur en multijoueur).
3. Lance le jeu : le système est actif dès l'entrée dans le monde.

Seul le serveur (ou le solo) a besoin du mod : les joueurs n'ont rien à installer côté client.

### Plugin Paper
1. Mets `1-slot-survival-paper-*.jar` dans le dossier `plugins/` du serveur.
2. Redémarre le serveur (pas de `/reload`).
3. Le réglage se trouve dans `plugins/SlotSurvival/config.yml`.

## Langues

Les messages du mod Fabric (chat, barre d'action, nom et description de la barrière) sont **traduits dans 51 langues**
(67 variantes régionales). Le texte s'affiche dans la langue choisie dans les options de Minecraft.

| | |
|---|---|
| Europe de l'Ouest | anglais (US, GB, AU, CA, NZ), français (FR, CA), espagnol (ES, MX, AR, CL, EC, UY, VE), allemand (DE, AT, CH), italien, portugais (PT, BR), néerlandais (NL, BE), catalan, galicien, basque |
| Europe du Nord | suédois, norvégien, danois, finnois, islandais, estonien, letton, lituanien |
| Europe centrale et de l'Est | polonais, tchèque, slovaque, slovène, croate, bosniaque, serbe, macédonien, bulgare, roumain, hongrois, russe, ukrainien, biélorusse, albanais, grec |
| Moyen-Orient et Asie | turc, hébreu, arabe, persan, hindi, bengali, thaï, vietnamien, indonésien, malais, filipino, japonais, coréen, chinois (simplifié, traditionnel, Hong Kong) |
| Autre | afrikaans |

**Important :** la traduction se fait **côté joueur**. Elle fonctionne si le mod est installé chez le joueur
(solo, ou serveur où les joueurs ont aussi le mod). Un joueur sans le mod voit les messages en **anglais** (langue de repli).

Ajouter ou corriger une langue : copier `fabric/src/assets/slot_survival/lang/en_us.json` vers `<code_langue>.json`
(par exemple `ga_ie.json`), traduire les valeurs (garder le `%s` de `slot_survival.msg.status`), puis ouvrir une Pull Request.
Le script `ci/check_lang.py` vérifie les fichiers à chaque push.

## Commandes

### Plugin Paper : `/slotsurvival`

Alias : `/slotsurv`, `/1slot`. Permission : `slotsurvival.admin` (opérateurs par défaut).

| Commande | Effet |
|----------|-------|
| `/slotsurvival enable` | Active le système |
| `/slotsurvival pause` | Met en pause et retire les barrières des joueurs connectés |
| `/slotsurvival offhand on` | Bloque aussi la main secondaire |
| `/slotsurvival offhand off` | Libère la main secondaire (défaut) |
| `/slotsurvival status` | Affiche l'état du système et le nombre de barrières dans ton inventaire |
| `/slotsurvival uninstall` | Désactive le système et retire toutes les barrières, avant de supprimer le plugin |
| `/slotsurvival selftest` | Diagnostic : vérifie que les API utilisées fonctionnent sur ton serveur (affiche `PASS`/`FAIL`) |

| Permission | Effet | Défaut |
|------------|-------|--------|
| `slotsurvival.admin` | Utiliser `/slotsurvival` | op |
| `slotsurvival.exempt` | Exempter un joueur du blocage (ses barrières sont retirées) | personne |

Configuration (`config.yml`), modifiée aussi par les commandes ci-dessus :

```yaml
enabled: true         # système actif
block-offhand: false  # bloquer la main secondaire
```

### Mod Fabric : fonctions `slot_lock`

Toutes les commandes demandent d'être **opérateur** (niveau 2).

| Commande | Effet |
|----------|-------|
| `/function slot_lock:admin/enable` | Active le système |
| `/function slot_lock:admin/pause` | Met en pause (les barrières déjà présentes restent) |
| `/function slot_lock:admin/offhand_on` | Bloque aussi la main secondaire |
| `/function slot_lock:admin/offhand_off` | Libère la main secondaire (défaut) |
| `/function slot_lock:admin/status` | Compte les barrières dans ton inventaire (35 attendues, 36 avec la main secondaire) |
| `/function slot_lock:admin/uninstall` | Désactive le système et retire toutes les barrières, avant de supprimer le mod |

Exempter un joueur :

```mcfunction
/tag <joueur> add sl_exempt
/clear <joueur> minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]
```

La deuxième commande retire les barrières qu'il avait déjà. Pour annuler : `/tag <joueur> remove sl_exempt`.

## Fonctionnement et règles

- **Slot libre** : le premier slot de la barre rapide. Les 8 autres de la barre et les 27 de l'inventaire sont bloqués.
- **Éjection sûre** : un objet dans un slot bloqué est déposé au sol avec un petit délai de ramassage
  (2,5 s). Si l'éjection échoue, l'objet reste chez le joueur.
- **Vérification** : à chaque changement d'inventaire, et toutes les 0,5 s par sécurité.
- **Barrières** : elles ne peuvent pas être jetées, posées (ni en bloc) ni gardées après la mort.
  Une barrière tombée au sol est supprimée. Les objets des joueurs ne sont jamais supprimés.
- **Armures** : non concernées, elles se portent normalement.
- **Main secondaire** : libre par défaut, bloquable avec `offhand on`.
- **Créatif** : le blocage s'applique aussi. **Spectateur** : ignoré.
- **Nouveaux joueurs** : un message d'explication leur est envoyé à leur première connexion.

### Différences entre les deux versions

| | Mod Fabric | Plugin Paper |
|---|---|---|
| Clic sur une barrière | Détecté puis corrigé en moins d'une demi-seconde | **Bloqué directement** |
| `pause` | Les barrières restent dans les inventaires | Les barrières sont retirées automatiquement |
| Joueur exempté | Barrières à retirer avec `/clear` | Retirées automatiquement |
| Réglages | Scoreboard du monde | `config.yml` |

## Tester rapidement

Avec le mod Fabric (en opérateur) :

| Commande | Résultat attendu |
|----------|------------------|
| `/function slot_lock:test/slot0` | 16 émeraudes dans le slot 1 : elles **restent** |
| `/function slot_lock:test/diamond` | Des diamants dans un slot bloqué : ils sont **éjectés** et la barrière revient |
| `/function slot_lock:test/many` | Plusieurs objets dans plusieurs slots bloqués : tous éjectés |

Sur Paper, la même vérification se fait avec la commande vanilla :

```mcfunction
/item replace entity @s inventory.13 with minecraft:diamond 64
```

## Désinstaller proprement

1. Lance la commande **uninstall** (`/slotsurvival uninstall` ou `/function slot_lock:admin/uninstall`)
   **pendant que les joueurs sont connectés** : leurs barrières sont retirées.
2. Supprime ensuite le `.jar`.

Un joueur déconnecté pendant l'opération garde ses barrières. Il peut les retirer avec
`/clear @s minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]` à sa reconnexion
(sur Paper, elles sont retirées automatiquement s'il se reconnecte alors que le système est désactivé).

## Dépannage

**Les commandes ne répondent pas ?** Vérifie d'abord ces points :

1. **Tu es opérateur ?** Paper : `/op <pseudo>` depuis la console. Fabric : les `/function` demandent le niveau 2.
2. **Fabric en solo** : crée le monde avec **« Autoriser les commandes : oui »** (ou « Ouvrir au réseau local » avec les triches activées). Sans ça, `/function` n'existe pas.
3. **Fabric n'a pas de commande `/slotsurvival`** : ses commandes sont les `/function slot_lock:admin/...` du tableau ci-dessus. `/slotsurvival` n'existe que sur **Paper**.
4. **Paper** : tape `/slotsurvival` seul, il affiche l'aide et l'état. Puis `/slotsurvival selftest` et envoie-moi les lignes `PASS`/`FAIL` en cas de problème.
5. Regarde les logs au démarrage (`logs/latest.log`) : cherche `SlotSurvival` (Paper) ou `slot_survival` (Fabric).


| Problème | Solution |
|----------|----------|
| Le mod Fabric ne se charge pas | Vérifie que **Fabric API** est dans `mods/` et que ta version de Minecraft est ≥ 1.21.11. |
| `Unknown function slot_lock:…` | Le mod n'est pas chargé : regarde les logs de démarrage. |
| Le plugin Paper ne se charge pas | Vérifie la version de Java (21 pour 1.21.11, 25 pour 26.x) et les logs du serveur. |
| Un joueur n'est pas bloqué | Il est peut-être exempté (tag `sl_exempt` / permission `slotsurvival.exempt`), en spectateur, ou le système est en pause. |
| Les barrières restent après `pause` (Fabric) | Normal : utilise `uninstall` ou `/clear`. |

## Pour les développeurs

```
fabric/src/            mod Fabric : fabric.mod.json + data/ (fonctions et advancement chargés par Fabric API)
paper/                 plugin Paper : projet Maven (Java 21, API Paper 1.21.11, sans NMS)
.github/workflows/     compilation des deux jars + création de la release sur tag v*
```

- **Fabric** : le mod embarque sa logique sous forme de données. Aucun code dépendant d'une version de
  Minecraft, donc un seul jar pour 1.21.11 → 26.x. Format des composants d'objets de 1.21.5+
  (SNBT, `tooltip_display`) ; la main secondaire est lue dans `equipment.offhand`.
- **Paper** : compilé contre l'API la plus ancienne supportée (1.21.11), donc compatible avec les versions suivantes.

Compiler soi-même :

```bash
# Fabric
(cd fabric/src && zip -r ../../1-slot-survival-fabric.jar .)

# Paper (Java 21 + Maven)
mvn -f paper/pom.xml package        # jar dans paper/target/
```

Publier une version : modifier le numéro de version (`fabric/src/fabric.mod.json` et `paper/pom.xml`),
puis pousser un tag :

```bash
git tag v2.0.1 && git push origin v2.0.1
```

GitHub Actions compile les jars et crée la release.
