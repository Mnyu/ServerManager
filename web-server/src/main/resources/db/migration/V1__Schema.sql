CREATE TABLE SERVER
(
    id         serial primary key,
    name       text,
    ip_address text unique,
    type       text,
    status     integer,
    memory     text,
    image_url  text
)