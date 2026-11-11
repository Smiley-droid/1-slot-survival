scoreboard players set #enabled sl_cfg 0
execute as @a run clear @s minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]
kill @e[type=item,nbt={Item:{components:{"minecraft:custom_data":{slot_lock:1b}}}}]
advancement revoke @a only slot_lock:inventory_changed
tag @a remove sl_init
tellraw @a [{"text":"[1 SLOT SURVIVAL] ","color":"gold","bold":true},{"text":"Barrières retirées. Tu peux maintenant faire : /datapack disable \"file/1_slot_survival\"","color":"yellow","bold":false}]
