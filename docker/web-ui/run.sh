#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)

cd "${THIS_DIR}" || exit

source "${THIS_DIR}"/../../build.properties

DOCKER_IMAGE_TAG=${APP_VERSION:=0.0.1}

docker run -it -d \
  -p 4200:4200 \
  -e API_SERVER_HOST="mk" \
  -e API_SERVER_PORT=8080 \
  "servermanager-web-ui:${DOCKER_IMAGE_TAG}"