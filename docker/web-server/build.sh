#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)

source "${THIS_DIR}"/../../build.properties

cd "${THIS_DIR}" || exit
SERVERMANAGER_DIST_DIR="${THIS_DIR}/../../web-server-distribution/target/web-server-distribution-${APP_VERSION}"
if [[ ! -d "${SERVERMANAGER_DIST_DIR}" ]]; then
  echo "${SERVERMANAGER_DIST_DIR} does not exist."
  echo "web-server-distribution build must be executed before building this."
  echo "Failed."
  exit 1
fi

cleanup() {
  rm -rf "${THIS_DIR}"/context/app
}

trap cleanup SIGINT SIGTERM ERR

cp -r "${SERVERMANAGER_DIST_DIR}" context/app

cd "${THIS_DIR}/context" || exit
DOCKER_IMAGE_TAG=${APP_VERSION:=0.0.1}

docker build . -t "servermanager-web-server:${DOCKER_IMAGE_TAG}"

cleanup
