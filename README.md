# saparbek-seidulla-student-api

Spring Boot CRUD application (Student API) with PostgreSQL, Docker Compose and Jenkins.

Author: Saparbek Seidulla

Status: initial version

## API

Base URL: `http://localhost:8080/saparbek-seidulla/api/students`

| Method | Path | Description |
|---|---|---|
| GET | /api/students | List all students |
| GET | /api/students/{id} | Get one student |
| POST | /api/students | Create a student |
| DELETE | /api/students/{id} | Delete a student |
| GET | /api/info | Student name and app version |

## Run

```bash
cp .env.example .env   # set POSTGRES_PASSWORD
docker compose up -d --build
```

Written answers: [docs/ANSWERS.md](docs/ANSWERS.md)
