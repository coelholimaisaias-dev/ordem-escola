# Caminho onde está o projeto atual
$basePath = "C:\workspace\code-pessoal\ordem-escola"

# Criar estrutura de pastas
New-Item -Path "$basePath\backend" -ItemType Directory -Force
New-Item -Path "$basePath\frontend" -ItemType Directory -Force
New-Item -Path "$basePath\docker" -ItemType Directory -Force

# Mover pasta docker existente para a pasta correta
If (Test-Path "$basePath\docker\postgres") {
    Write-Host "Docker postgres já existe, mantendo..."
} ElseIf (Test-Path "$basePath\postgres") {
    Move-Item "$basePath\postgres" "$basePath\docker\postgres"
}

# Descompactar backend zipado para a pasta backend
$backendZip = "$basePath\ordem-escola.zip"
If (Test-Path $backendZip) {
    Expand-Archive -Path $backendZip -DestinationPath "$basePath\backend" -Force
    Write-Host "Backend descompactado com sucesso."
} Else {
    Write-Host "Arquivo backend.zip não encontrado, pulei esta etapa."
}

# Criar README.md
$readmePath = "$basePath\README.md"
If (!(Test-Path $readmePath)) {
    "## Ordem Escola - Monorepo`nEste repositório contém Backend, Frontend e Docker." | Out-File $readmePath
}

# Criar .gitignore básico
$gitignorePath = "$basePath\.gitignore"
If (!(Test-Path $gitignorePath)) {
    @"
# Node / frontend
node_modules/
dist/

# Backend
target/

# Docker
docker/*/postgres/data/
*.env
"@ | Out-File $gitignorePath
}

Write-Host "Estrutura inicial do monorepo criada com sucesso!"
