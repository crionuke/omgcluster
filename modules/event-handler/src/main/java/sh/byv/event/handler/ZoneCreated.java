package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.node.NodeRelService;
import sh.byv.node.NodeRelType;
import sh.byv.job.JobService;
import sh.byv.job.JobType;
import sh.byv.zone.ZoneService;
import sh.byv.zone.ZoneStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final NodeRelService rels;
    final ZoneService zones;
    final JobService jobs;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    @SneakyThrows
    public void execute(final EventEntity event) {
        final var zone = zones.getByIdRequired(event.getEntityId());
        if (zone.getStatus() == ZoneStatus.PENDING) {
            final var node = rels.getLeastPopulatedNode();
            rels.create(NodeRelType.ZONE, zone.getId(), node);

            jobs.schedule(JobType.ZONE, zone.getId());
            zones.activate(zone);
        }
    }
}
