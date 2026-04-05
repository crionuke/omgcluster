create table if not exists omgc_world (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    name text not null,
    status integer not null,
    constraint omgc_world_name_unique unique (name)
);
