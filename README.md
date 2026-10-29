# saparbek-seidulla-student-api

Spring Boot CRUD application (Student API) with PostgreSQL, Docker Compose and Jenkins.

Author: Saparbek Seidulla

Status: documentation updated by Saparbek Seidulla (usage examples added)

## API

Base URL: `http://localhost:8080/saparbek-seidulla/api/students`

| Method | Path | Description |
|---|---|---|
| GET | /api/students | List all students |
| GET | /api/students/{id} | Get one student |
| POST | /api/students | Create a student |
| PUT | /api/students/{id} | Update a student |
| DELETE | /api/students/{id} | Delete a student |
| GET | /api/info | Student name and app version |

## Run

```bash
cp .env.example .env   # set POSTGRES_PASSWORD
docker compose up -d --build
```

Written answers: [docs/ANSWERS.md](docs/ANSWERS.md)

## Changes by Saparbek Seidulla

- Added the **Update** operation: `PUT /api/students/{id}` replaces full name, email, group and course.
- Returns `404 Not Found` with a JSON error body when the student does not exist.
- Covered by the automated test `updatesStudent`.

## Usage examples

```bash
curl http://localhost:8080/saparbek-seidulla/api/students
curl -X POST http://localhost:8080/saparbek-seidulla/api/students \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Saparbek Seidulla","email":"257305927+sejdullasapar-rgb@users.noreply.github.com","groupName":"IT2-2302","course":3}'
```
