# Le joueur a posé un bloc barrière avec notre item : on retire les barrières proches (portée de pose)
scoreboard players set @s sl_placed 0
execute anchored eyes positioned ^ ^ ^ run fill ~-6 ~-6 ~-6 ~6 ~6 ~6 minecraft:air replace minecraft:barrier
