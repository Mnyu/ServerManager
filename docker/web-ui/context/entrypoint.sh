#!/usr/bin/env bash

set -Eeuo pipefail

dockerize -template /tmp/nginx/my-nginx-proxy.tmpl:/etc/nginx/conf.d/my-nginx-proxy.conf

# Append "daemon off;" to the beginning of the configuration
echo "daemon off;" >> /etc/nginx/nginx.conf

nginx


