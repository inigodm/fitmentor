#!/usr/bin/env bash

set -e
cd "$(dirname "${BASH_SOURCE[0]}")"/..
cd docker

docker compose down -v
docker compose up db kafka kafdrop --build -d

docker exec -it kafka \
  /bin/kafka-topics --create \
                    --topic fitmentor.projections \
                    --bootstrap-server kafka:9092 \
                    --partitions 1 \
                    --replication-factor 1