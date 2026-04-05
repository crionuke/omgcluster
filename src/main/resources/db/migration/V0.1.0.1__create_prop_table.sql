create table if not exists omgc_prop (
    id bigint generated always as identity primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    type text not null,
    value jsonb not null,
    constraint omgc_prop_type_unique unique (type)
);
