# Macros : slot, path. Exécuté en tant que le joueur, à sa position.
# Principe anti-perte / anti-duplication :
#   1. on crée une entité objet "placeholder" (pierre marquée)
#   2. on y COPIE l'objet exact du slot (id, quantité, tous les composants)
#   3. si la copie a échoué (placeholder encore présent) -> on supprime le placeholder
#      et on NE TOUCHE PAS au slot : l'objet reste chez le joueur
#   4. sinon on remplace le slot par la barrière ; si ce remplacement échoue,
#      on supprime la COPIE au sol (l'original est resté dans le slot)
#   => dans tous les cas, l'objet existe exactement une fois.
scoreboard players set #ok sl_tmp 0
summon item ~ ~0.6 ~ {Tags:["sl_new"],PickupDelay:50s,Motion:[0.0d,0.2d,0.0d],Item:{id:"minecraft:stone",count:1,components:{"minecraft:custom_data":{sl_placeholder:1b}}}}
$data modify entity @n[type=item,tag=sl_new,distance=..3] Item set from entity @s $(path)
execute if entity @n[type=item,tag=sl_new,distance=..3,nbt={Item:{components:{"minecraft:custom_data":{sl_placeholder:1b}}}}] run return run kill @n[type=item,tag=sl_new,distance=..3]
$execute store result score #ok sl_tmp run function slot_lock:give_barrier {slot:"$(slot)"}
execute if score #ok sl_tmp matches 0 run return run kill @n[type=item,tag=sl_new,distance=..3]
tag @n[type=item,tag=sl_new,distance=..3] remove sl_new
