# Exécuté au chargement du monde et à chaque /reload
scoreboard objectives add sl_cfg dummy
scoreboard objectives add sl_tmp dummy
scoreboard objectives add sl_placed minecraft.used:minecraft.barrier

# Valeurs par défaut (ne réécrase pas les réglages existants)
execute unless score #enabled sl_cfg matches 0..1 run scoreboard players set #enabled sl_cfg 1
execute unless score #offhand sl_cfg matches 0..1 run scoreboard players set #offhand sl_cfg 0
execute unless score #t sl_cfg matches 0.. run scoreboard players set #t sl_cfg 0
