create table if not exists omgc_command (
    id bigint generated always as identity primary key,
    server_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type integer not null,
    body jsonb not null,
    status integer not null,
    constraint fk_omgc_command_server foreign key (server_id) references omgc_server(id) on delete restrict on update restrict
);
