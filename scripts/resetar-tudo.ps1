# Reseta containers E banco (apaga volumes)
Set-Location C:\workspace\code-pessoal\ordem-escola
git pull
Set-Location docker
docker-compose -f docker-compose.prod.yml down -v
docker-compose -f docker-compose.prod.yml up -d --build
