create table if not exists omgc_sim (
    id bigint generated always as identity primary key,
    zone_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_sim_zone_name_unique unique (zone_id, name),
    constraint fk_omgc_sim_zone foreign key (zone_id) references omgc_zone(id) on delete restrict on update restrict
);
