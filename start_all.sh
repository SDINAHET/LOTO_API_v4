#!/bin/bash


# scripte start_all.sh
# modifie
# http://localhost:5500/ pour servir des fichiers statiques
# http://localhost:8082/swagger-ui/index.html
# demarrer mongodb et postgresql si besoin
# demarrer spring boot au premier plan

set -e

PORT_STATIC=5500
PORT_SPRING=8082
STATIC_DIR="src/main/resources/static"
ENABLE_OLLAMA=false   # ← passe à true plus tard


open_browser() {
  local URL="$1"

  # Si tu es root, évite xdg-open (Chrome refuse no-sandbox)
  if [ "$(id -u)" -eq 0 ]; then
    if command -v powershell.exe >/dev/null 2>&1; then
      powershell.exe start "$URL" >/dev/null 2>&1 || true
      echo "   🪟 Ouverture demandée côté Windows: $URL"
    else
      echo "   ⚠️ Tu es root : ouverture navigateur désactivée pour $URL"
      echo "   ➜ Ouvre manuellement: $URL"
    fi
    return
  fi

  if command -v xdg-open >/dev/null 2>&1; then
    xdg-open "$URL" >/dev/null 2>&1 || true
  elif command -v open >/dev/null 2>&1; then
    open "$URL" >/dev/null 2>&1 || true
  elif command -v powershell.exe >/dev/null 2>&1; then
    powershell.exe start "$URL" >/dev/null 2>&1 || true
  else
    echo "⚠️ Impossible d’ouvrir automatiquement le navigateur pour $URL"
    echo "   ➜ Ouvre manuellement: $URL"
  fi
}

echo "==> 1) MongoDB"
sudo service mongod start >/dev/null 2>&1 || sudo service mongodb start >/dev/null 2>&1 || true

echo "==> 2) PostgreSQL"
sudo service postgresql start >/dev/null 2>&1 || true

start_ollama() {
  if [ "$ENABLE_OLLAMA" != "true" ]; then
    echo "==> Ollama désactivé (ENABLE_OLLAMA=false)"
    return
  fi

  echo "==> Ollama"

  if ! command -v ollama >/dev/null 2>&1; then
    echo "   ⚠️ ollama non installé (skip)"
    return
  fi

  if curl -s http://localhost:11434 >/dev/null 2>&1; then
    echo "   ✅ Ollama déjà actif sur :11434"
    return
  fi

  echo "   🚀 Lancement de ollama serve (arrière-plan)"
  nohup ollama serve >/tmp/ollama.log 2>&1 &
  sleep 1

  if curl -s http://localhost:11434 >/dev/null 2>&1; then
    echo "   ✅ Ollama lancé (logs: /tmp/ollama.log)"
  else
    echo "   ❌ Ollama ne répond pas (voir /tmp/ollama.log)"
  fi
}


echo "==> 3) Serveur HTTP (static) sur :$PORT_STATIC (en arrière-plan)"
if [ ! -d "$STATIC_DIR" ]; then
  echo "❌ Dossier $STATIC_DIR introuvable"
  exit 1
fi

if command -v lsof >/dev/null 2>&1; then
  if ! lsof -i :"$PORT_STATIC" >/dev/null 2>&1; then
    (cd "$STATIC_DIR" && nohup python3 -m http.server "$PORT_STATIC" >/tmp/static_http.log 2>&1 &)
    echo "   ✅ Static HTTP lancé : http://localhost:$PORT_STATIC/"
  else
    echo "   ⚠️ Port $PORT_STATIC déjà utilisé (skip)"
  fi
else
  echo "   ⚠️ lsof non installé. Pour l’installer: sudo apt update && sudo apt install -y lsof"
  echo "   ➜ Je lance quand même le serveur, tu verras si ça échoue..."
  (cd "$STATIC_DIR" && nohup python3 -m http.server "$PORT_STATIC" >/tmp/static_http.log 2>&1 &) || true
fi

echo "==> 4) Ouverture du frontend"
open_browser "http://localhost:$PORT_STATIC/"

echo "==> 5) Build Spring Boot"
mvn clean install

echo "==> 6) Démarrage Spring Boot (au premier plan)"
echo "   ⏳ Swagger s’ouvrira quand le port $PORT_SPRING répondra..."
(
  # Thread qui attend le démarrage puis ouvre Swagger
  while ! curl -s "http://localhost:$PORT_SPRING/swagger-ui/index.html" >/dev/null 2>&1; do
    sleep 2
  done
  echo "   ✅ Spring Boot prêt"
  open_browser "http://localhost:$PORT_SPRING/swagger-ui/index.html"
) &

# Spring Boot au premier plan (le script reste attaché)
mvn spring-boot:run
