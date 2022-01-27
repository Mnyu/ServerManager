#!/usr/bin/env bash

set -Eeuo pipefail

THIS_DIR=$(cd "$(dirname "$0")" && pwd)

"$THIS_DIR"/../docker/web-ui/build.sh
"$THIS_DIR"/../docker/web-server/build.sh