#!/usr/bin/env python3
"""Usage: summarize.py <titre> <fichier-log> <regex>  -> annotation ::notice avec les lignes utiles du log."""
import re, sys
title, path, pattern = sys.argv[1:4]
lines = [l for l in open(path, errors="replace").read().splitlines() if re.search(pattern, l)]
msg = "\n".join(lines[:80]).replace("%", "%25").replace("\r", "").replace("\n", "%0A")
print(f"::notice title={title}::{msg[:6500]}")
