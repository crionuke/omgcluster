create table if not exists omgc_sim_group_rel (
    id bigint generated always as identity primary key,
    sim_id bigint not null,
    group_id bigint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint omgc_sim_group_rel_sim_group_unique unique (sim_id, group_id),
    constraint fk_omgc_sim_group_rel_sim foreign key (sim_id) references omgc_sim(id) on delete restrict on update restrict,
    constraint fk_omgc_sim_group_rel_group foreign key (group_id) references omgc_group(id) on delete restrict on update restrict
);
