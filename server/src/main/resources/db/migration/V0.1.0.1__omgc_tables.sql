create table if not exists omgc_prop (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type text not null,
    value jsonb not null,
    constraint omgc_prop_type_unique unique (type)
);

create table if not exists omgc_event (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type integer not null,
    entity_id bigint not null,
    status integer not null
);

create table if not exists omgc_world (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_world_name_unique unique (name)
);

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

create table if not exists omgc_zone (
    id bigint generated always as identity primary key,
    layer_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    x1 integer not null,
    y1 integer not null,
    x2 integer not null,
    y2 integer not null,
    constraint omgc_zone_layer_coords_unique unique (layer_id, x1, y1, x2, y2),
    constraint omgc_zone_x_check check (x1 < x2),
    constraint omgc_zone_y_check check (y1 < y2),
    constraint fk_omgc_zone_layer foreign key (layer_id) references omgc_layer(id) on delete restrict on update restrict
);

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

create table if not exists omgc_instance (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_instance_name_unique unique (name)
);

create table if not exists omgc_state (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type text not null,
    body jsonb not null,
    constraint omgc_state_type_unique unique (type)
);

create table if not exists omgc_conn (
    id bigint generated always as identity primary key,
    instance_id bigint not null,
    world_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_conn_instance_world_unique unique (instance_id, world_id),
    constraint fk_omgc_conn_instance foreign key (instance_id) references omgc_instance(id) on delete restrict on update restrict,
    constraint fk_omgc_conn_world foreign key (world_id) references omgc_world(id) on delete restrict on update restrict
);

create table if not exists omgc_sim_instance_rel (
    id bigint generated always as identity primary key,
    sim_id bigint not null,
    instance_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_sim_instance_rel_sim_instance_unique unique (sim_id, instance_id),
    constraint fk_omgc_sim_instance_rel_sim foreign key (sim_id) references omgc_sim(id) on delete restrict on update restrict,
    constraint fk_omgc_sim_instance_rel_instance foreign key (instance_id) references omgc_instance(id) on delete restrict on update restrict
);

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

create table if not exists omgc_zone_instance_rel (
    id bigint generated always as identity primary key,
    zone_id bigint not null,
    instance_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_zone_instance_rel_zone_instance_unique unique (zone_id, instance_id),
    constraint fk_omgc_zone_instance_rel_zone foreign key (zone_id) references omgc_zone(id) on delete restrict on update restrict,
    constraint fk_omgc_zone_instance_rel_instance foreign key (instance_id) references omgc_instance(id) on delete restrict on update restrict
);

