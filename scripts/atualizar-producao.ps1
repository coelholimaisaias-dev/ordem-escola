# Conecta no servidor e atualiza containers (mantém banco)
ssh root@159.89.34.102 'cd /srv/app && git pull && chmod +x scripts/atualizar-containers.sh && ./scripts/atualizar-containers.sh'
