FROM openjdk:8-jdk-slim-buster

RUN apt-get update && apt-get install -y wget curl postgresql-client inotify-tools && \
    apt install -y debian-keyring debian-archive-keyring apt-transport-https && \
    curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | apt-key add - && \
    curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | tee -a /etc/apt/sources.list.d/caddy-stable.list && \
    apt update && \
    apt install caddy && \
    apt-get clean && \
    useradd -M newuser

USER newuser
WORKDIR /opt/servermanager

COPY --chown=newuser:newuser ["app/", "/opt/servermanager" ]

COPY --chown=newuser:newuser ["entrypoint.sh", "/"]

ENTRYPOINT ["/entrypoint.sh"]