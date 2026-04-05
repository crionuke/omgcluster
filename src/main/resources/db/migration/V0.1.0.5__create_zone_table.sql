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
