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
    parent_id bigint,
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
    constraint fk_omgc_zone_layer foreign key (layer_id) references omgc_layer(id) on delete restrict on update restrict,
    constraint fk_omgc_zone_parent foreign key (parent_id) references omgc_zone(id) on delete restrict on update restrict
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

create table if not exists omgc_node (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_node_name_unique unique (name)
);

create table if not exists omgc_state (
    id bigint generated always as identity primary key,
    node_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    body jsonb not null,
    constraint fk_omgc_state_node foreign key (node_id) references omgc_node(id) on delete cascade on update restrict
);

create table if not exists omgc_node_rel (
    id bigint generated always as identity primary key,
    node_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type integer not null,
    entity_id bigint not null,
    status integer not null,
    constraint omgc_node_rel_type_entity_node_unique unique (node_id, entity_id, type),
    constraint fk_omgc_node_rel_node foreign key (node_id) references omgc_node(id) on delete restrict on update restrict
);

create table if not exists omgc_conn (
    id bigint generated always as identity primary key,
    node_id bigint not null,
    world_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_conn_node_world_unique unique (node_id, world_id),
    constraint fk_omgc_conn_node foreign key (node_id) references omgc_node(id) on delete restrict on update restrict,
    constraint fk_omgc_conn_world foreign key (world_id) references omgc_world(id) on delete restrict on update restrict
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
