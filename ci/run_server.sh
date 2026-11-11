#!/usr/bin/env bash
# Usage: run_server.sh <dossier> <commande de démarrage...>   (commandes lues dans $CMDS, séparées par |)
# Démarre un serveur, attend "Done", envoie les commandes, arrête, puis affiche le log filtré en annotation.
set -u
DIR="$1"; shift
cd "$DIR"
echo "eula=true" > eula.txt
rm -f in; mkfifo in
"$@" < in > server.log 2>&1 &
PID=$!
exec 3> in
for i in $(seq 1 120); do
  grep -q 'Done (' server.log 2>/dev/null && break
  kill -0 $PID 2>/dev/null || break
  sleep 3
done
IFS='|' read -ra LIST <<< "$CMDS"
for c in "${LIST[@]}"; do echo "$c" >&3; sleep 2; done
echo "stop" >&3
sleep 8
kill $PID 2>/dev/null || true
exec 3>&-
echo "===== LOG (fin) ====="; tail -n 80 server.log
