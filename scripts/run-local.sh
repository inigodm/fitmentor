#!/usr/bin/env bash

set -e
cd ..
./gradlew bootRun --args="--spring.profiles.active=dev" $@
