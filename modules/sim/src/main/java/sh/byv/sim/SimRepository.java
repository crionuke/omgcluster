package sh.byv.sim;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;
import sh.byv.zone.ZoneEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class SimRepository implements PanacheRepository<SimEntity> {

    public SimEntity create(final ZoneEntity zone, final String name) {
        final var sim = new SimEntity();
        sim.setZone(zone);
        sim.setCreatedAt(OffsetDateTime.now());
        sim.setUpdatedAt(OffsetDateTime.now());
        sim.setName(name);
        sim.setStatus(EntityStatus.PENDING);
        persist(sim);
        return sim;
    }
}
