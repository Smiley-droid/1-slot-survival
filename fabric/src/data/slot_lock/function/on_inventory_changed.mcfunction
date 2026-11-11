# Appelé par l'advancement slot_lock:inventory_changed (en tant que le joueur)
# On le révoque tout de suite pour qu'il puisse se redéclencher.
advancement revoke @s only slot_lock:inventory_changed
execute unless score #enabled sl_cfg matches 1 run return 0
execute unless entity @s[tag=sl_init,tag=!sl_exempt,gamemode=!spectator] run return 0
function slot_lock:check_all
