# OrdemEscola

![OrdemEscola Banner](./frontend/src/assets/banner.png)

OrdemEscola é uma aplicação full-stack organizada como monorepo, com **Spring Boot** no backend, **Angular** no frontend e **PostgreSQL** como banco de dados. O repositório inclui configuração para desenvolvimento local com Docker e instruções para execução sem container.

---

## Sumário

- [Visão geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Execução rápida (Docker)](#execução-rápida-docker)
- [Execução local (sem Docker)](#execução-local-sem-docker)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Variáveis de ambiente principais](#variáveis-de-ambiente-principais)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Visão geral

Este monorepo contém dois principais serviços:
- Backend: aplicação Spring Boot (Java) em [backend](backend)
- Frontend: aplicação Angular em [frontend](frontend)

Além disso, há configurações Docker em [docker](docker) para orquestrar Postgres, backend e frontend em containers.

## Tecnologias

- Backend: Spring Boot (Java 21), Maven
- Frontend: Angular 21, TypeScript, SCSS
- Banco: PostgreSQL 16
- Containerização: Docker, Docker Compose

## Pré-requisitos

- Docker (recomendado) e Docker Compose
- Node.js >= 20 e npm (para desenvolvimento frontend local)
- JDK 21 e Maven (ou use o wrapper `mvnw` / `mvnw.cmd`) para desenvolvimento backend

## Execução rápida (Docker)

Usa o arquivo de compose em [docker/docker-compose.yml](docker/docker-compose.yml).

1. A partir da raiz do repositório:

```bash
docker compose -f docker/docker-compose.yml up --build -d
```

2. Serviços disponíveis:

- Backend: http://localhost:8080
- Frontend (container): http://localhost:3000
- PostgreSQL: host `localhost`, porta `5433`, usuário `postgres`, senha `postgres`

3. Parar e remover containers:

```bash
docker compose -f docker/docker-compose.yml down
```

> Observação: o container frontend serve os artefatos estáticos via Nginx (porta interna 80 mapeada para 3000 no host).

## Execução local (sem Docker)

Backend (modo desenvolvimento):

Windows:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
cd backend
./mvnw spring-boot:run
```

Frontend (modo desenvolvimento):

```bash
cd frontend
npm install
npm start
# abre em http://localhost:4200 (ng serve)
```

Build para produção (frontend):

```bash
cd frontend
npm run build
```

Build para produção (backend):

```bash
cd backend
./mvnw -DskipTests package
```

## Testes

Backend:

```bash
cd backend
./mvnw test
```

Frontend:

```bash
cd frontend
npm test
```

## Estrutura do projeto

- [backend](backend) — código do Spring Boot e configurações Maven
- [frontend](frontend) — aplicação Angular
- [docker](docker) — compose e scripts para Postgres e suporte a containers

## Variáveis de ambiente principais

- `SPRING_DATASOURCE_URL` — JDBC URL do banco (ex.: `jdbc:postgresql://postgres:5432/postgres` quando em Docker)
- `SPRING_DATASOURCE_USERNAME` — usuário do banco (padrão: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` — senha do banco (padrão: `postgres`)

Ao usar Docker Compose, essas variáveis já são definidas em [docker/docker-compose.yml](docker/docker-compose.yml).

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch: `git checkout -b feature/minha-feature`.
3. Faça commits claros e pequenos.
4. Abra um Pull Request descrevendo as mudanças.

## Licença

MIT © Isaias Coelho