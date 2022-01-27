#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)

source "${THIS_DIR}"/../../build.properties

cd "${THIS_DIR}" || exit

if [[ ! -d "${THIS_DIR}"/../../web-ui/dist/web-ui ]];then
  echo "web-ui build directory ${THIS_DIR}/../../web-ui/dist/web-ui does not exist."
  echo "web-ui build must be executed before building this."
  echo "Failed."
  exit 1
fi

cleanup() {
  rm -rf "${THIS_DIR}"/context/app
}

trap cleanup SIGINT SIGTERM ERR

cp -r "${THIS_DIR}"/../../web-ui/dist/web-ui context/app

cd "${THIS_DIR}/context" || exit

DOCKER_IMAGE_TAG=${APP_VERSION:=0.0.1}

docker build . -t "servermanager-web-ui:${DOCKER_IMAGE_TAG}"

cleanup

