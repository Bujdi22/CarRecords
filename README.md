# Auto Journal Project
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=flat-square&logo=postgresql&logoColor=white)


This is the server for the AutoJournal project. 
Uses Java SpringBoot and PostgreSQL. The front-end is a separate repository (https://github.com/Bujdi22/CarRecordsUI)

## Features

- User accounts: Registraton, e-mail verification, forgot password, etc
- Authentication using JWT
- Google SSO
- Vehicle and maintenance records CRUD
- Media stored on S3

## Environment variables
Create a `env.properties` file in root, and fill in .env variables
```
API_KEY=<api-key-here>
RECAPTCHA_SECRET=<recaptcha-key>

DEBUG_ENABLED=true

DB_DATABASE_URL=postgres:5432/spring
DB_USER=root
DB_PASSWORD=root

S3_BUCKET_NAME=
S3_ACCESS_KEY=
S3_SECRET_KEY=

MAIL_HOST=mailhog
MAIL_PORT=1025
MAIL_USERNAME=
MAIL_PASSWORD=
MAIL_SMTP_AUTH=false
MAIL_SMTP_TLS=false

GOOGLE_OAUTH_CLIENT=
GOOGLE_OAUTH_SECRET=

#APP_URL=https://autojournalapp.com
#SERVER_URL=https://api.autojournalapp.com

APP_URL=http://localhost:8100
SERVER_URL=http://localhost:8080
```
## Flyway migrations

create a `flyway.conf` file in root
```
flyway.url=jdbc:postgresql://localhost:5432/spring
flyway.user=root
flyway.password=root
flyway.schemas=public

; Command: mvn flyway:migrate
```

## Docker

```
 mvn clean package -DskipTests
 docker comose down
 docker compose build
 docker compose up -d
 
 
 mvn clean package -DskipTests && docker compose down && docker compose build && docker compose up -d 
```

