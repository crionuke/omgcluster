create table if not exists omgc_event (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type integer not null,
    entity_id bigint not null,
    status integer not null
);