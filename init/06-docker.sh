#!/bin/bash
set -e

echo "=== Installation Docker ==="

# Supprimer anciennes versions si présentes
sudo apt remove -y docker docker-engine docker.io containerd runc || true

# Dépendances
sudo apt update -y
sudo apt install -y \
  ca-certificates \
  curl \
  gnupg \
  lsb-release

# Clé GPG Docker
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
  | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Dépôt Docker
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" \
  | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Installation Docker
sudo apt update -y
sudo apt install -y \
  docker-ce \
  docker-ce-cli \
  containerd.io \
  docker-buildx-plugin \
  docker-compose-plugin

# Activer Docker
sudo systemctl enable docker
sudo systemctl start docker

# Ajouter l’utilisateur courant au groupe docker
if ! groups "$USER" | grep -q docker; then
  sudo usermod -aG docker "$USER"
  echo "ℹ️ Utilisateur ajouté au groupe docker (relogin requis)"
fi

echo "✅ Docker installé"
