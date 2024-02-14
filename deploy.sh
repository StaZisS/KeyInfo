#!/bin/bash

docker compose down --volumes --remove-orphans --rmi all
docker compose up -d postgres liquibase
docker compose up -d