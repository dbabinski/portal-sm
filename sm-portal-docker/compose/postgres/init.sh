#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER softmedica WITH PASSWORD '19P8VqX8OEcRZDp6' SUPERUSER LOGIN;
    CREATE DATABASE smportal;
    GRANT ALL PRIVILEGES ON DATABASE smportal TO softmedica;
EOSQL

pg_restore -U "$POSTGRES_USER" -d smportal -v "/docker-entrypoint-initdb.d/sm-portal.backup"