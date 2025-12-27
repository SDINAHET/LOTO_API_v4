#!/bin/bash

echo "========================================="
echo "  Lancement intelligent PostgreSQL + API "
echo "========================================="

# Vérification si PostgreSQL tourne
if sudo service postgresql status | grep -q "online"; then
    echo "✅ PostgreSQL est déjà démarré."
else
    echo "⏳ PostgreSQL est arrêté. Démarrage en cours..."
    sudo service postgresql start

    # Petite attente de sécurité
    sleep 3

    # Vérification après démarrage
    if sudo service postgresql status | grep -q "online"; then
        echo "✅ PostgreSQL démarré avec succès."
    else
        echo "❌ ERREUR : PostgreSQL n'a pas pu démarrer."
        exit 1
    fi
fi

echo "🚀 Lancement de Spring Boot..."
mvn spring-boot:run
