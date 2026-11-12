#!/usr/bin/env python3
"""Usage: resolve_versions.py <minecraft_version>
Affiche loader=... et api=... (dernières versions Fabric compatibles) pour GITHUB_OUTPUT."""
import json, sys, urllib.parse, urllib.request
mc = sys.argv[1]
H = {"User-Agent": "slot-survival-ci"}
def get(u): return json.loads(urllib.request.urlopen(urllib.request.Request(u, headers=H)).read())
loader = [x for x in get("https://meta.fabricmc.net/v2/versions/loader") if x.get("stable")][0]["version"]
q = ("https://api.modrinth.com/v2/project/fabric-api/version?game_versions=" + urllib.parse.quote(json.dumps([mc]))
     + "&loaders=" + urllib.parse.quote('["fabric"]'))
vs = get(q)
if not vs:
    sys.exit(f"Aucune version de Fabric API pour {mc}")
print(f"loader={loader}")
print(f"api={vs[0]['version_number']}")
