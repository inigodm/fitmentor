#!/usr/bin/env bash

set -e
cd "$(dirname "${BASH_SOURCE[0]}")"/..
cd docker

docker compose down -v
docker compose up --build
