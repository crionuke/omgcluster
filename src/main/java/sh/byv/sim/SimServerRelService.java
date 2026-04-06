package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.server.ServerEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimServerRelService {

    final SimServerRelRepository repository;
    final EventService events;

    public SimServerRelEntity create(final SimEntity sim, final ServerEntity server) {
        final var rel = repository.create(sim, server);
        events.create(EventType.SIM_SERVER_REL_CREATED, rel.getId());
        log.info("Sim {} relation to server {} created", sim.getId(), server.getId());
        return rel;
    }

    public SimServerRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public ServerEntity getLeastPopulatedServer() {
        return repository.findLeastPopulatedServer()
                .orElseThrow(() -> new NotFoundException("No servers found"));
    }
}
