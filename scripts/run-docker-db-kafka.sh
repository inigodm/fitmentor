#!/usr/bin/env bash

set -e

cd ../docker
docker compose down
docker rmi -f "$(docker images -q)"

cd "$(dirname "${BASH_SOURCE[0]}")"/..
cd docker

docker compose down -v
docker compose up db kafka kafdrop --build -d

docker exec -it kafka \
  /bin/kafka-topics --create \
                    --topic snapshots.coach \
                    --bootstrap-server kafka:9092 \
                    --partitions 1 \
                    --replication-factor 1 \
                    --config cleanup.policy=compact

docker exec -it kafka \
  /bin/kafka-topics --create \
                    --topic snapshots.client \
                    --bootstrap-server kafka:9092 \
                    --partitions 1 \
                    --replication-factor 1 \
                    --config cleanup.policy=compact

docker exec -it kafka \
  /bin/kafka-topics --create \
                    --topic snapshots.plan \
                    --bootstrap-server kafka:9092 \
                    --partitions 1 \
                    --replication-factor 1 \
                    --config cleanup.policy=compact
