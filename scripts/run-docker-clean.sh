#!/usr/bin/env bash

set -e

cd ../docker
docker compose down
docker rmi -f "$(docker images -q)"

cd "$(dirname "${BASH_SOURCE[0]}")"/..
cd docker

docker compose down -v
docker system prune -a --volumes
