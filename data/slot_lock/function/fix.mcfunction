# Macros : slot (nom du slot pour /item), path (chemin NBT de l'objet dans les données du joueur)
#   - inventaire / hotbar : Inventory[{Slot:Nb}]
#   - main secondaire     : equipment.offhand   (depuis 1.21.5, plus dans Inventory)
# Cas 1 : le slot contient un objet étranger -> éjection sécurisée (qui remet aussi la barrière)
$execute if data entity @s $(path) run function slot_lock:eject {slot:"$(slot)",path:"$(path)"}
# Cas 2 : le slot est vide -> on remet la barrière
$execute unless data entity @s $(path) run function slot_lock:give_barrier {slot:"$(slot)"}
