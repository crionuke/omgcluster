create table if not exists omgc_sim_server_rel (
    id bigint generated always as identity primary key,
    sim_id bigint not null,
    server_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    status integer not null,
    constraint omgc_sim_server_rel_sim_server_unique unique (sim_id, server_id),
    constraint fk_omgc_sim_server_rel_sim foreign key (sim_id) references omgc_sim(id) on delete restrict on update restrict,
    constraint fk_omgc_sim_server_rel_server foreign key (server_id) references omgc_server(id) on delete restrict on update restrict
);
