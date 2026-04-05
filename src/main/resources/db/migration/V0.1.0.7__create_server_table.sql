create table if not exists omgc_server (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    internal_address text not null,
    external_address text not null,
    status integer not null,
    constraint omgc_server_internal_address_unique unique (internal_address)
);
