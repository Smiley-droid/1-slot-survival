# Balayage de sécurité (toutes les 10 ticks)
scoreboard players set #t sl_cfg 0

# 1) Nouveaux joueurs (amis qui rejoignent, premier lancement du pack)
execute as @a[tag=!sl_init] at @s run function slot_lock:init_player

# 2) Vérification de l'inventaire de tous les joueurs concernés
execute as @a[tag=sl_init,tag=!sl_exempt,gamemode=!spectator] at @s run function slot_lock:check_all

# 3) Rappel si le joueur tient une barrière en main
execute as @a[tag=sl_init,tag=!sl_exempt,gamemode=!spectator] if items entity @s weapon.mainhand minecraft:barrier[minecraft:custom_data~{slot_lock:1b}] run title @s actionbar {"translate":"slot_survival.actionbar.locked","fallback":"Locked slot! Use slot 1 of your hotbar.","color":"red"}

# 4) Nettoyage des barrières-placeholder jetées au sol (jamais les objets des joueurs)
kill @e[type=item,nbt={Item:{components:{"minecraft:custom_data":{slot_lock:1b}}}}]
