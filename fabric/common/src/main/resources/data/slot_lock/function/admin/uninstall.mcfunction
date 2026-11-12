scoreboard players set #enabled sl_cfg 0
execute as @a run clear @s minecraft:barrier[minecraft:custom_data~{slot_lock:1b}]
kill @e[type=item,nbt={Item:{components:{"minecraft:custom_data":{slot_lock:1b}}}}]
advancement revoke @a only slot_lock:inventory_changed
tag @a remove sl_init
tellraw @a [{"text":"[1 SLOT SURVIVAL] ","color":"gold","bold":true},{"translate":"slot_survival.msg.uninstall","fallback":"Barriers removed. You can now remove the 1 SLOT SURVIVAL mod from your mods folder.","color":"yellow","bold":false}]
