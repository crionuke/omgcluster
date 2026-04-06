create table if not exists omgc_zone_group_rel (
    id bigint generated always as identity primary key,
    zone_id bigint not null,
    group_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_zone_group_rel_zone_group_unique unique (zone_id, group_id),
    constraint fk_omgc_zone_group_rel_zone foreign key (zone_id) references omgc_zone(id) on delete restrict on update restrict,
    constraint fk_omgc_zone_group_rel_group foreign key (group_id) references omgc_group(id) on delete restrict on update restrict
);
