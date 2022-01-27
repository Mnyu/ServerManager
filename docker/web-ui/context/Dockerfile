FROM debian:buster-slim
RUN apt update && apt install -y wget curl nginx && apt clean all

ENV DOCKERIZE_VERSION v0.6.1

RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY ["nginx-conf/my-nginx-proxy.tmpl", "/tmp/nginx/"]
COPY ["app/", "/app"]
COPY ["entrypoint.sh", "/"]

ENTRYPOINT [ "/entrypoint.sh"]