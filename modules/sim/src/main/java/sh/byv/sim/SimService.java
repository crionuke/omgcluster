package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;
import sh.byv.zone.ZoneEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimService {

    final SimRepository simRepository;
    final EventService eventService;

    @Transactional
    public SimEntity create(final ZoneEntity zone, final String name) {
        final var sim = simRepository.create(zone, name);
        eventService.create(EventType.SIM_CREATED, sim.getId());
        log.info("Created sim {} in zone {} of layer {} in world {}",
                name, zone.getId(), zone.getLayer().getName(), zone.getLayer().getWorld().getName());
        return sim;
    }

    public SimEntity getByIdRequired(final Long id) {
        return simRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sim not found: " + id));
    }
}