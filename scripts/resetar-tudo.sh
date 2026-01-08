#!/bin/bash
# Reseta containers E banco (apaga volumes)
cd "$(dirname "$0")"
git pull
cd docker
docker-compose -f docker-compose.prod.yml down -v
docker-compose -f docker-compose.prod.yml up -d --build
