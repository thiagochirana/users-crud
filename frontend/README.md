# Frontend de Usuarios

Interface administrativa em Vue 3 para gerenciamento de usuarios, consumindo a API REST em `/api/v1/users`.

## Requisitos

- Node.js 22+
- API backend rodando

## Configuracao

Crie um arquivo `.env` na raiz do projeto com:

```sh
VITE_API_BASE_URL=http://localhost:8080
```

Tambem existe o arquivo `.env.example` como referencia.

## Comandos

```sh
npm install
npm run dev
```

Para validar o projeto:

```sh
npm run lint
npm run type-check
npm run build
```
