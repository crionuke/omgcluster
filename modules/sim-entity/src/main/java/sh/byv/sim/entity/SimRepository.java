package sh.byv.sim.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.zone.entity.ZoneEntity;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class SimRepository implements PanacheRepository<SimEntity> {

    public SimEntity create(final ZoneEntity zone, final String name) {
        final var sim = new SimEntity();
        sim.setZone(zone);
        sim.setCreatedAt(OffsetDateTime.now());
        sim.setUpdatedAt(OffsetDateTime.now());
        sim.setName(name);
        sim.setStatus(SimStatus.PENDING);
        persist(sim);
        return sim;
    }

    public List<SimEntity> findByZoneAndStatus(final ZoneEntity zone, final SimStatus status) {
        return find("zone = ?1 and status = ?2", zone, status).list();
    }
}
