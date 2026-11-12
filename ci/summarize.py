#!/usr/bin/env python3
"""Usage: summarize.py <titre> <fichier-log> <regex>  -> annotation ::notice avec les lignes utiles du log."""
import re, sys
title, path, pattern = sys.argv[1:4]
tail = len(sys.argv) > 4 and sys.argv[4] == "tail"
lines = [l for l in open(path, errors="replace").read().splitlines() if re.search(pattern, l)]
lines = lines[-70:] if tail else lines[:80]
msg = "\n".join(lines).replace("%", "%25").replace("\r", "").replace("\n", "%0A")
print(f"::notice title={title}::{msg[:6500]}")
