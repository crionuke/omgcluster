create table if not exists omgc_server (
    id bigint generated always as identity primary key,
    group_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    internal_address text not null,
    external_address text not null,
    status integer not null,
    constraint omgc_server_internal_address_unique unique (internal_address),
    constraint fk_omgc_server_group foreign key (group_id) references omgc_group(id) on delete restrict on update restrict
);