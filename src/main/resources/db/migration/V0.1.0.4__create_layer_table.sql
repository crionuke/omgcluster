create table if not exists omgc_layer (
    id bigint generated always as identity primary key,
    world_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_layer_world_name_unique unique (world_id, name),
    constraint fk_omgc_layer_world foreign key (world_id) references omgc_world(id) on delete restrict on update restrict
);
