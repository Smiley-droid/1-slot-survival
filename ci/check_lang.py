#!/usr/bin/env python3
"""Vérifie les fichiers de langue du mod : JSON valide, mêmes clés que en_us, un seul %s dans msg.status,
et que chaque clé utilisée dans les fonctions existe."""
import json, pathlib, re, sys
root = pathlib.Path(__file__).resolve().parent.parent / "fabric" / "src"
lang = root / "assets" / "slot_survival" / "lang"
ref = json.loads((lang / "en_us.json").read_text(encoding="utf-8"))
errors = []
for f in sorted(lang.glob("*.json")):
    try:
        d = json.loads(f.read_text(encoding="utf-8"))
    except Exception as e:
        errors.append(f"{f.name}: JSON invalide ({e})"); continue
    if set(d) != set(ref):
        errors.append(f"{f.name}: clés différentes {set(d) ^ set(ref)}")
    if d.get("slot_survival.msg.status", "").count("%s") != 1:
        errors.append(f"{f.name}: msg.status doit contenir exactement un %s")
    if not f.stem.islower():
        errors.append(f"{f.name}: nom de fichier non minuscule")
used = set()
for f in (root / "data").rglob("*.mcfunction"):
    used |= set(re.findall(r'slot_survival\.[a-z_.]+', f.read_text(encoding="utf-8")))
for k in sorted(used):
    if k not in ref:
        errors.append(f"clé utilisée mais absente de en_us.json : {k}")
print(f"{len(list(lang.glob('*.json')))} fichiers de langue, {len(ref)} clés, {len(used)} clés utilisées")
if errors:
    print("\n".join(errors)); sys.exit(1)
print("OK")
