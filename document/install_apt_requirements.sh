#!/bin/bash
set -e

REQ_FILE="apt.requirements.txt"

if [ ! -f "$REQ_FILE" ]; then
  echo "❌ $REQ_FILE introuvable"
  exit 1
fi

echo "=== Mise à jour APT ==="
sudo apt update -y

echo "=== Installation des paquets APT ==="
sudo xargs -a "$REQ_FILE" apt install -y

echo "=== Vérifications ==="
java -version
javac -version
mvn -version
python3 --version

echo "✅ Installation terminée"
