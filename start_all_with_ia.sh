# #!/bin/bash
# set -e

# # Ports
# PORT_STATIC=5500
# PORT_SPRING=8082
# PORT_AI=8090

# # Dossiers
# STATIC_DIR="src/main/resources/static"
# AI_FILE="ai.py"              # ai.py à la racine

# ENABLE_OLLAMA=false          # true plus tard

# open_browser() {
#   local URL="$1"
#   # évite xdg-open en root (Chrome no-sandbox)
#   if [ "$(id -u)" -eq 0 ]; then
#     if command -v powershell.exe >/dev/null 2>&1; then
#       powershell.exe start "$URL" >/dev/null 2>&1 || true
#       echo "   🪟 Ouverture demandée côté Windows: $URL"
#     else
#       echo "   ⚠️ root: ouvre manuellement $URL"
#     fi
#     return
#   fi
#   if command -v xdg-open >/dev/null 2>&1; then xdg-open "$URL" >/dev/null 2>&1 || true
#   elif command -v powershell.exe >/dev/null 2>&1; then powershell.exe start "$URL" >/dev/null 2>&1 || true
#   else echo "➡️ Ouvre: $URL"; fi
# }

# start_ollama() {
#   [ "$ENABLE_OLLAMA" = "true" ] || return 0
#   echo "==> Ollama"
#   command -v ollama >/dev/null 2>&1 || { echo "   ⚠️ ollama absent (skip)"; return 0; }
#   curl -s http://localhost:11434 >/dev/null 2>&1 && { echo "   ✅ déjà actif"; return 0; }
#   nohup ollama serve >/tmp/ollama.log 2>&1 & disown
#   sleep 1
#   curl -s http://localhost:11434 >/dev/null 2>&1 && echo "   ✅ lancé" || echo "   ⚠️ voir /tmp/ollama.log"
# }

# start_ai_service() {
#   echo "==> 3bis) AI service (8090) en arrière-plan"

#   [ -f "$AI_FILE" ] || { echo "❌ $AI_FILE introuvable à la racine"; exit 1; }

#   if command -v lsof >/dev/null 2>&1 && lsof -i :"$PORT_AI" >/dev/null 2>&1; then
#     echo "   ⚠️ Port $PORT_AI déjà utilisé (skip)"
#     return 0
#   fi

#   # Lance via python (ton ai.py contient app = FastAPI(...) et les routes /health, /ai/chat)
#   # nohup python3 "$AI_FILE" >/tmp/ai_8090.log 2>&1 & disown
#   nohup python3 -m uvicorn ai:app --host 0.0.0.0 --port $PORT_AI >/tmp/ai_8090.log 2>&1 & disown

#   # Attends /health
#   for _ in {1..20}; do
#     if curl -s "http://localhost:$PORT_AI/health" >/dev/null 2>&1; then
#       echo "   ✅ AI OK: http://localhost:$PORT_AI/health"
#       return 0
#     fi
#     sleep 1
#   done

#   echo "   ⚠️ AI lancé mais /health ne répond pas (logs: /tmp/ai_8090.log)"
# }

# echo "==> 1) MongoDB"
# sudo service mongod start >/dev/null 2>&1 || sudo service mongodb start >/dev/null 2>&1 || true

# echo "==> 2) PostgreSQL"
# sudo service postgresql start >/dev/null 2>&1 || true

# # (optionnel)
# start_ollama

# echo "==> 3) Front static (5500) en arrière-plan"
# [ -d "$STATIC_DIR" ] || { echo "❌ $STATIC_DIR introuvable"; exit 1; }

# if command -v lsof >/dev/null 2>&1 && lsof -i :"$PORT_STATIC" >/dev/null 2>&1; then
#   echo "   ⚠️ Port $PORT_STATIC déjà utilisé (skip)"
# else
#   (cd "$STATIC_DIR" && nohup python3 -m http.server "$PORT_STATIC" --bind 0.0.0.0 >/tmp/static_http.log 2>&1 & disown)
#   echo "   ✅ Front: http://localhost:$PORT_STATIC/  (ai: /ai.html)"
# fi

# # démarre l’AI avant d’ouvrir le front
# start_ai_service

# echo "==> 4) Ouvre le front"
# open_browser "http://localhost:$PORT_STATIC/"
# open_browser "http://localhost:$PORT_STATIC/ai.html"

# echo "==> 5) Build Spring Boot"
# mvn clean install

# echo "==> 6) Spring Boot (au premier plan) + Swagger auto"
# (
#   while ! curl -s "http://localhost:$PORT_SPRING/swagger-ui/index.html" >/dev/null 2>&1; do
#     sleep 2
#   done
#   open_browser "http://localhost:$PORT_SPRING/swagger-ui/index.html"
# ) &

# # mvn spring-boot:run
# mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=$PORT_SPRING"


#!/bin/bash
set -e

# Ports
PORT_STATIC=5500
PORT_SPRING=8082
PORT_AI=8091

STATIC_DIR="src/main/resources/static"
AI_FILE="ai.py"

open_browser() {
  local URL="$1"
  if command -v powershell.exe >/dev/null 2>&1; then
    powershell.exe start "$URL" >/dev/null 2>&1 || true
    echo "   🪟 Ouverture demandée côté Windows: $URL"
  else
    echo "➡️ Ouvre: $URL"
  fi
}

start_ai_service() {
  echo "==> AI service (8090)"

  if lsof -i :"$PORT_AI" >/dev/null 2>&1; then
    echo "   ⚠️ Port $PORT_AI déjà utilisé (skip)"
    return
  fi

  nohup python3 -m uvicorn ai:app --host 0.0.0.0 --port "$PORT_AI" \
    >/tmp/ai_8090.log 2>&1 & disown

  for _ in {1..20}; do
    if curl -s "http://localhost:$PORT_AI/health" >/dev/null 2>&1; then
      echo "   ✅ AI OK"
      return
    fi
    sleep 1
  done

  echo "   ⚠️ AI lancé mais /health ne répond pas"
}

echo "==> MongoDB"
sudo service mongod start >/dev/null 2>&1 || true

echo "==> PostgreSQL"
sudo service postgresql start >/dev/null 2>&1 || true

echo "==> Front static (5500)"
if ! lsof -i :"$PORT_STATIC" >/dev/null 2>&1; then
  (cd "$STATIC_DIR" && nohup python3 -m http.server "$PORT_STATIC" \
    >/tmp/static_http.log 2>&1 & disown)
fi

open_browser "http://localhost:$PORT_STATIC/"

echo "==> Build Spring Boot"
mvn clean install

echo "==> Démarrage Spring Boot (premier plan)"
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=$PORT_SPRING" &

echo "==> Attente Spring Boot..."
for _ in {1..60}; do
  if curl -s "http://localhost:$PORT_SPRING/swagger-ui/index.html" >/dev/null 2>&1; then
    echo "   ✅ Spring UP"
    break
  fi
  sleep 2
done

open_browser "http://localhost:$PORT_SPRING/swagger-ui/index.html"

start_ai_service

# Spring reste vivant tant que le script tourne
wait
