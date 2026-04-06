create table if not exists omgc_conn_zone_rel (
    id bigint generated always as identity primary key,
    conn_id bigint not null,
    zone_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_conn_zone_rel_unique unique (conn_id, zone_id),
    constraint fk_omgc_conn_zone_rel_conn foreign key (conn_id) references omgc_conn(id) on delete restrict on update restrict,
    constraint fk_omgc_conn_zone_rel_zone foreign key (zone_id) references omgc_zone(id) on delete restrict on update restrict
);