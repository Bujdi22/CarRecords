#!/bin/bash

# Variables
CONTAINER_NAME=ec2-user-postgres-1
DB_NAME=spring
DB_USER=autojournal
BACKUP_DIR=/home/ec2-user/backups
S3_BUCKET_NAME=autojournal-db-backups
DATE=$(date +'%Y-%m-%d')
BACKUP_FILE="$BACKUP_DIR/postgres_backup_$DATE.sql.gz"

mkdir -p "$BACKUP_DIR"

docker exec "$CONTAINER_NAME" pg_dump -U "$DB_USER" --column-inserts --no-owner --no-comments "$DB_NAME" | gzip > "$BACKUP_FILE"

aws s3 cp "$BACKUP_FILE" s3://$S3_BUCKET_NAME/