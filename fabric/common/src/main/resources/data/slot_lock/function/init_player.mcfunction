tag @s add sl_init
tellraw @s [{"text":"[1 SLOT SURVIVAL] ","color":"gold","bold":true},{"translate":"slot_survival.msg.welcome","fallback":"Only one slot is usable: the first slot of your hotbar. Items placed in a locked slot are dropped on the ground, never deleted.","color":"yellow","bold":false}]
