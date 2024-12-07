# Auto Journal Project
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)

## environment variables
Create a `env.properties` file in root, and fill in .env variables
```
API_KEY=<api-key-here>
RECAPTCHA_SECRET=<recaptcha-key>

DB_DATABASE_URL=localhost:5432/spring
DB_USER=root
DB_PASSWORD=root

S3_BUCKET_NAME=
S3_ACCESS_KEY=
S3_SECRET_KEY=

MAIL_HOST=localhost
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