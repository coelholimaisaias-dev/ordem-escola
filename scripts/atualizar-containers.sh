#!/bin/bash
# Atualiza containers em produção (servidor remoto)
cd "$(dirname "$0")"
git pull
cd docker
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml up -d --build
