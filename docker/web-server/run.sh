#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)

cd "${THIS_DIR}" || exit
source "${THIS_DIR}"/../../build.properties

DOCKER_IMAGE_TAG=${APP_VERSION:=0.0.1}

docker run -it -d \
  -p 8080:8080 \
  -e POSTGRES_HOSTNAME="mk" \
  -e POSTGRES_PORT=5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD="s3cr3t" \
  -e POSTGRES_DB="serverdb" \
  -e POSTGRES_SCHEMA="public" \
  "servermanager-web-server:${DOCKER_IMAGE_TAG}"