# Exécuté en tant que le joueur, à sa position. Vérifie les 35 slots à bloquer (+ main secondaire en option).
# Un slot est "correct" s'il contient NOTRE barrière (custom_data.slot_lock = 1b).
# Vide ou autre objet -> slot_lock:fix (éjection sécurisée si besoin, puis barrière).

# Slot 1 de la hotbar (index 0) : doit rester libre -> on retire uniquement NOTRE barrière si elle y a été déposée
execute if items entity @s hotbar.0 minecraft:barrier[minecraft:custom_data~{slot_lock:1b}] run item replace entity @s hotbar.0 with minecraft:air

# Hotbar slots 2 à 9 (index 1 à 8)
execute unless items entity @s hotbar.1 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.1",path:"Inventory[{Slot:1b}]"}
execute unless items entity @s hotbar.2 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.2",path:"Inventory[{Slot:2b}]"}
execute unless items entity @s hotbar.3 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.3",path:"Inventory[{Slot:3b}]"}
execute unless items entity @s hotbar.4 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.4",path:"Inventory[{Slot:4b}]"}
execute unless items entity @s hotbar.5 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.5",path:"Inventory[{Slot:5b}]"}
execute unless items entity @s hotbar.6 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.6",path:"Inventory[{Slot:6b}]"}
execute unless items entity @s hotbar.7 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.7",path:"Inventory[{Slot:7b}]"}
execute unless items entity @s hotbar.8 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"hotbar.8",path:"Inventory[{Slot:8b}]"}

# Inventaire principal : 27 slots (index NBT 9 à 35)
execute unless items entity @s inventory.0 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.0",path:"Inventory[{Slot:9b}]"}
execute unless items entity @s inventory.1 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.1",path:"Inventory[{Slot:10b}]"}
execute unless items entity @s inventory.2 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.2",path:"Inventory[{Slot:11b}]"}
execute unless items entity @s inventory.3 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.3",path:"Inventory[{Slot:12b}]"}
execute unless items entity @s inventory.4 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.4",path:"Inventory[{Slot:13b}]"}
execute unless items entity @s inventory.5 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.5",path:"Inventory[{Slot:14b}]"}
execute unless items entity @s inventory.6 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.6",path:"Inventory[{Slot:15b}]"}
execute unless items entity @s inventory.7 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.7",path:"Inventory[{Slot:16b}]"}
execute unless items entity @s inventory.8 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.8",path:"Inventory[{Slot:17b}]"}
execute unless items entity @s inventory.9 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.9",path:"Inventory[{Slot:18b}]"}
execute unless items entity @s inventory.10 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.10",path:"Inventory[{Slot:19b}]"}
execute unless items entity @s inventory.11 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.11",path:"Inventory[{Slot:20b}]"}
execute unless items entity @s inventory.12 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.12",path:"Inventory[{Slot:21b}]"}
execute unless items entity @s inventory.13 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.13",path:"Inventory[{Slot:22b}]"}
execute unless items entity @s inventory.14 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.14",path:"Inventory[{Slot:23b}]"}
execute unless items entity @s inventory.15 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.15",path:"Inventory[{Slot:24b}]"}
execute unless items entity @s inventory.16 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.16",path:"Inventory[{Slot:25b}]"}
execute unless items entity @s inventory.17 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.17",path:"Inventory[{Slot:26b}]"}
execute unless items entity @s inventory.18 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.18",path:"Inventory[{Slot:27b}]"}
execute unless items entity @s inventory.19 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.19",path:"Inventory[{Slot:28b}]"}
execute unless items entity @s inventory.20 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.20",path:"Inventory[{Slot:29b}]"}
execute unless items entity @s inventory.21 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.21",path:"Inventory[{Slot:30b}]"}
execute unless items entity @s inventory.22 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.22",path:"Inventory[{Slot:31b}]"}
execute unless items entity @s inventory.23 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.23",path:"Inventory[{Slot:32b}]"}
execute unless items entity @s inventory.24 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.24",path:"Inventory[{Slot:33b}]"}
execute unless items entity @s inventory.25 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.25",path:"Inventory[{Slot:34b}]"}
execute unless items entity @s inventory.26 *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"inventory.26",path:"Inventory[{Slot:35b}]"}

# Main secondaire (optionnel, réglage #offhand) - stockée dans equipment.offhand depuis 1.21.5
execute if score #offhand sl_cfg matches 1 unless items entity @s weapon.offhand *[minecraft:custom_data~{slot_lock:1b}] run function slot_lock:fix {slot:"weapon.offhand",path:"equipment.offhand"}
execute unless score #offhand sl_cfg matches 1 if items entity @s weapon.offhand minecraft:barrier[minecraft:custom_data~{slot_lock:1b}] run item replace entity @s weapon.offhand with minecraft:air
