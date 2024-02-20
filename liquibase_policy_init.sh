#!/bin/sh

/liquibase/liquibase \
    --driver=org.postgresql.Driver \
    --url=jdbc:postgresql://postgres:5432/${KEY_INFO_DB} \
    --changeLogFile=changelog/db.changelog-init.yaml \
    --username=${POSTGRES_USER} \
    --password=${POSTGRES_PASSWORD} \
    update