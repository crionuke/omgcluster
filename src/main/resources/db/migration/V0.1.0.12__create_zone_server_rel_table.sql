create table if not exists omgc_zone_server_rel (
    id bigint generated always as identity primary key,
    zone_id bigint not null,
    server_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_zone_server_rel_zone_server_unique unique (zone_id, server_id),
    constraint fk_omgc_zone_server_rel_zone foreign key (zone_id) references omgc_zone(id) on delete restrict on update restrict,
    constraint fk_omgc_zone_server_rel_server foreign key (server_id) references omgc_server(id) on delete restrict on update restrict
);
