# Macro : slot -> place la barrière "SLOT BLOQUÉ" (format composants 1.21.5+ / 1.21.11 / 26.x)
# "return run" renvoie 1 si réussi.
$return run item replace entity @s $(slot) with minecraft:barrier[minecraft:custom_data={slot_lock:1b},minecraft:custom_name={text:"SLOT BLOQUÉ",color:"red",bold:true,italic:false},minecraft:lore=[{text:"Cet emplacement est interdit !",color:"gray",italic:false}],minecraft:max_stack_size=1,minecraft:enchantments={"minecraft:vanishing_curse":1},minecraft:tooltip_display={hidden_components:["minecraft:enchantments"]},minecraft:enchantment_glint_override=false]
