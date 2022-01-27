#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)
source "$THIS_DIR/../build.properties"

COMMAND="up"
COMMAND_UP="up"
COMMAND_DOWN="down"
REMOVE_VOLUMES_ARG=""

printUsage() {
  local executable
  executable=$(basename "$0")
  printf '\nUsage: %s [options] [command]' "${executable}"
  printf '\n'
  printf '\nCommands:'
  printf '\n\t %-25s %-25s' "up" "Docker Compose Up"
  printf '\n\t %-25s %-25s' "down" "Docker Compose Down"
  printf '\n'
  printf '\nOptions:'
  printf '\n\t %-25s %-25s' "-h, --help" "Display this usage help"
  printf '\n\t %-25s %-25s' "-v" "When using Docker Compose Down, remove volumes too. Default - do not remove volumes"
  printf '\n\t %-25s %-25s' "--debug-bash" "Enable debug output in bash"
  printf '\n\n'
}

main() {
  if [[ "$COMMAND" == "$COMMAND_UP" ]];then
    docker-compose --env-file "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/.env -f "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/docker-compose.yml up -d
  elif [[ "$COMMAND" == "$COMMAND_DOWN" ]]; then
    if [[ -z "$REMOVE_VOLUMES_ARG" ]]; then
      docker-compose --env-file "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/.env -f "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/docker-compose.yml down
    else
      docker-compose --env-file "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/.env -f "$THIS_DIR"/../servermanager-distribution/target/servermanager-distribution-0.0.1/docker-compose/docker-compose.yml down "$REMOVE_VOLUMES_ARG"
    fi
  fi
}

parseArgs() {
  while [[ $# -ge 1 ]]; do
    local arg="$1"
    case $arg in
      --debug-bash)
        set -x
        ;;
      up)
        COMMAND="$COMMAND_UP"
        ;;
      down)
        COMMAND="$COMMAND_DOWN"
        ;;
      -v)
        REMOVE_VOLUMES_ARG="-v"
        ;;
      -h | --help)
        printUsage
        exit 0
        ;;
      *)
        echo "Unknown command or option $arg" >&2
        printUsage
        exit 0
        ;;
      esac
    shift
  done
}

parseArgs "$@"
main