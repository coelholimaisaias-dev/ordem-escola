# Atualiza containers em modo prod local (sem resetar banco)
Set-Location C:\workspace\code-pessoal\ordem-escola
git pull
Set-Location docker
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml up -d --build
