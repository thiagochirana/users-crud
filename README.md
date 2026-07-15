# Cadastro de Usuarios

Aplicacao de cadastro de usuarios com:

- API REST em Java com Spring Boot
- frontend em Vue 3 + Vite
- banco PostgreSQL

Atende os fluxos de:

- cadastro de usuario
- listagem paginada
- edicao de usuario
- exclusao de usuario
- validacoes no frontend e backend

## Sumario

- [Requisitos](#requisitos)
- [Estrutura](#estrutura)
- [Banco de dados local](#banco-de-dados-local)
- [Executando localmente sem Docker](#executando-localmente-sem-docker)
- [Executando com Docker Compose (Para facilitar)](#executando-com-docker-compose-para-facilitar)
- [Validacoes implementadas](#validacoes-implementadas)
- [Endpoints principais](#endpoints-principais)
- [Comandos uteis](#comandos-uteis)

## Requisitos

- Java 26 para execucao local da API
- Node.js 22 ou superior
- PostgreSQL 14 ou superior

## Estrutura

- `api/`: backend Spring Boot com JPA, Flyway e PostgreSQL
- `frontend/`: interface Vue consumindo a API REST
- `docker-compose.yml`: ambiente de desenvolvimento com Postgres, API e frontend

## Banco de dados local

Crie um banco PostgreSQL com os dados abaixo:

- database: `users_api`
- user: `postgres`
- password: sua senha local

O schema e a tabela sao criados automaticamente pelo Flyway.

## Executando localmente sem Docker

### 1. Subir o PostgreSQL

Garanta que exista um banco `users_api` acessivel em `localhost:5432`.

### 2. Configurar a API

A API usa estas configuracoes em `api/src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/users_api`
- `spring.datasource.username=postgres`
- `spring.datasource.password=${DB_PASSWORD:}`

Defina a senha do banco antes de iniciar a API:

```bash
export DB_PASSWORD=sua_senha
```

### 3. Iniciar a API

```bash
./gradlew bootRun
```

Execute dentro da pasta `api/`.

API disponivel em:

- `http://localhost:8080`

### 4. Configurar o frontend

O frontend usa o arquivo `frontend/.env` com:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

### 5. Iniciar o frontend

```bash
npm install
npm run dev
```

Execute dentro da pasta `frontend/`.

Frontend disponivel em:

- `http://localhost:5173`

## Executando com Docker Compose (Para facilitar)

O projeto possui implementado Docker compose de desenvolvimento com:

- `postgres`
- `api`
- `frontend`

Suba tudo com:

```bash
docker compose up --build
```

Acessos:

- frontend: `http://localhost:5173`
- api: `http://localhost:8080`
- postgres: `localhost:5432`

## Validacoes implementadas

- campos obrigatorios no frontend e backend
- formato basico de data no frontend e backend
- formato basico de documento CPF/ID no frontend
- mascara automatica de CPF apenas quando o valor possui exatamente 11 digitos numericos
- validacao de CPF no backend quando o documento for numerico com 11 digitos
- validacao basica de ID alfanumerico no backend
- validacao de CEP e UF no frontend
- mensagens claras de erro e sucesso

## Endpoints principais

- `GET /api/v1/users`
- `GET /api/v1/users/{id}`
- `POST /api/v1/users`
- `PATCH /api/v1/users/{id}`
- `DELETE /api/v1/users/{id}`

## Comandos uteis

### API

```bash
./gradlew test
./gradlew bootRun
```

### Frontend

```bash
npm run lint
npm run type-check
npm run build
```
