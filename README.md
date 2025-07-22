# FitPulse API 🏋️

API RESTful para gerenciamento de treinos pessoais (workouts) com autenticação JWT, documentação com Swagger, Redis e banco PostgreSQL.  

<p align="center">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />  <img src="https://img.shields.io/badge/Spring_Security-4DB33D?style=for-the-badge&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" />
  <img src="https://img.shields.io/badge/Lombok-E81C1C?style=for-the-badge&logo=lombok&logoColor=white" />
</p>

---

## 🔧 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL (hospedado no [Supabase](https://supabase.com))
- Redis (hospedado no [Railway](https://railway.app))
- Swagger UI (SpringDoc OpenAPI 3)
- Lombok
- Docker/Podman (opcional)

---
![defaultWorkflow.png](images/defaultWorkflow.png)

---

## 📚 Funcionalidades

- Registro e autenticação de usuários com JWT e refresh tokens no Redis (cookie HTTPOnly)
- CRUD completo de treinos (Workouts) e exercícios (Exercises)
- Controle de status do treino (`PENDING`,`ACTIVE`, `FINISHED`)
- Resposta Páginada dos Treinos
- Agrupamento de treinos finalizados por mês
- Documentação automática com Swagger UI
- Seeder opcional de dados para testes
- Redis configurado para cache do Refresh Token

![supabase-schema.png](images/supabase-schema.png)
---

## 🛠️ Como rodar localmente

### 1. Clone o projeto

```bash
git clone https://github.com/LiamFer/FitPulse-API.git
cd FitPulse-API
````

### 2. Crie um arquivo `.env` na raiz ou configure no seu sistema:

```env
DB_PASS=your_pg_password
DB_URL=jdbc:postgresql://localhost:5432/your_database
DB_USER=your_pg_user

JWT_SECRET=your_jwt_secret
JWT_REFRESH_SECRET=your_refresh_secret

REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_USER=default
REDIS_PASS=your_redis_password
```

> ⚠️ Você pode usar `application.properties` ou `application.yml` com `${VAR}` para injetar esses valores no Spring Boot.

---

## 🧪 Endpoints principais

### 🔐 Autenticação

| Método | Endpoint         | Descrição                             |
| ------ | ---------------- | ------------------------------------- |
| POST   | `/auth/register` | Cria um novo usuário                  |
| POST   | `/auth/login`    | Autentica usuário e retorna JWT token |
| POST   | `/auth/refresh`  | Gera novo JWT via refresh token       |

### 🏋️‍♀️ Workouts
| Método | Endpoint                | Descrição                                            |
|--------|-------------------------|------------------------------------------------------|
| GET    | `/workouts`             | Lista os treinos do usuário (paginado)              |
| GET    | `/workouts/report`      | Retorna contagem de treinos finalizados por mês     |
| POST   | `/workouts`             | Cria novo treino                                    |
| PATCH  | `/workouts/{id}`        | Atualiza treino                                     |
| DELETE | `/workouts/{id}`        | Deleta treino e seus exercícios                     |
> Todos os endpoints de treino requerem o header:
> `Authorization: Bearer {seu_token}`

---

## 🔐 Segurança e JWT

A aplicação usa autenticação com **JWT (Bearer Token)** e **refresh token em cookie HTTPOnly**:

* Para acessar rotas protegidas, inclua o token de acesso:

  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
  ```

* O token de refresh é salvo automaticamente no cookie `refreshToken` ao fazer login.

* Tokens são regenerados pela rota `/auth/refresh`.

![AuthenticationWorkflow.png](images/AuthenticationWorkflow.png)

---

## 📄 Documentação Swagger

* Acesse: [http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)
* JSON: `/v3/api-docs`
* YAML: `/v3/api-docs.yaml`

![Swagger.png](images/Swagger.png)

---

## 🌱 Dados de exemplo

Você pode ativar o seeder (dados de teste) com a variável:

```properties
seed.enabled=true
```

Usuário padrão criado:

```json
{
  "email": "joao@example.com",
  "senha": "123456"
}
```

---

## 🧱 Estrutura de Pastas

```
com.liamfer.workoutTracker
├── config          # Configurações (Swagger, Redis, JWT, Security)
├── controller      # Endpoints da API
├── domain          # Entidades (JPA)
├── DTO             # Objetos de transporte de dados
├── enums           # Enums de Status e Roles
├── exceptions      # Exceções personalizadas
├── repository      # Interfaces do Spring Data JPA
├── service         # Regras de negócio
├── Seeder.java     # Dados de teste
└── GlobalErrorHandling.java
```

## 📎 Projeto original

Este projeto foi desenvolvido com base no desafio da comunidade:

🔗 https://roadmap.sh/projects/fitness-workout-tracker


