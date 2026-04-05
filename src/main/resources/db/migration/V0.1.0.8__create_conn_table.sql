create table if not exists omgc_conn (
    id bigint generated always as identity primary key,
    server_id bigint not null,
    world_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_conn_server_world_unique unique (server_id, world_id),
    constraint fk_omgc_conn_server foreign key (server_id) references omgc_server(id) on delete restrict on update restrict,
    constraint fk_omgc_conn_world foreign key (world_id) references omgc_world(id) on delete restrict on update restrict
);