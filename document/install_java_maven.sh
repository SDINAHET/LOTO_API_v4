#!/bin/bash

echo "=============================="
echo " Installation Java 21 + Maven "
echo "=============================="

# Mise à jour du système
sudo apt update -y

# Installation de Java JDK 21
echo ">> Installation de OpenJDK 21..."
sudo apt install -y openjdk-21-jdk

# Installation de Maven
echo ">> Installation de Maven..."
sudo apt install -y maven

# Définition de JAVA_HOME si absent
JAVA_HOME_PATH="/usr/lib/jvm/java-21-openjdk-amd64"

if ! grep -q "JAVA_HOME" ~/.bashrc; then
  echo ">> Configuration de JAVA_HOME..."
  echo "export JAVA_HOME=$JAVA_HOME_PATH" >> ~/.bashrc
  echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
fi

# Recharge du shell
source ~/.bashrc

echo ""
echo "=============================="
echo " Vérifications "
echo "=============================="

java -version
javac -version
mvn -version

echo ""
echo "✅ Installation terminée avec succès"
