# Atualiza containers local sem resetar banco
Set-Location C:\workspace\code-pessoal\ordem-escola
git pull
Set-Location docker
docker-compose down
docker-compose up -d --build
