# Exécuté chaque tick. Le travail lourd est volontairement évité ici.
execute unless score #enabled sl_cfg matches 1 run return 0

# Joueur qui vient de poser une barrière (bloc) -> on retire le bloc posé
execute as @a[scores={sl_placed=1..}] at @s run function slot_lock:barrier_placed

# Filet de sécurité : balayage toutes les 10 ticks (0,5 s)
scoreboard players add #t sl_cfg 1
execute if score #t sl_cfg matches 10.. run function slot_lock:sweep
