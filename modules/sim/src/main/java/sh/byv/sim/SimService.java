package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;
import sh.byv.zone.ZoneEntity;

import java.util.List;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimService {

    final SimRepository repository;
    final EventService events;

    public SimEntity create(final ZoneEntity zone, final String name) {
        final var sim = repository.create(zone, name);
        events.create(EventType.SIM_CREATED, sim.getId());
        log.info("Created sim {} in zone {} of layer {} in world {}",
                name, zone.getId(), zone.getLayer().getName(), zone.getLayer().getWorld().getName());
        return sim;
    }

    public List<SimEntity> getActiveByZone(final ZoneEntity zone) {
        return repository.findByZoneAndStatus(zone, SimStatus.ACTIVE);
    }

    public SimEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sim not found: " + id));
    }

    public void activate(final SimEntity sim) {
        sim.setStatus(SimStatus.ACTIVE);
        events.create(EventType.SIM_ACTIVATED, sim.getId());
    }
}