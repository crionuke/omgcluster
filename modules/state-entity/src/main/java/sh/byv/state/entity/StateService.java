package sh.byv.state.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.exception.clazz.NotFoundException;
import sh.byv.server.entity.ServerEntity;
import sh.byv.sim.entity.SimEntity;
import sh.byv.zone.entity.ZoneEntity;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class StateService {

    final StateRepository repository;
    final StateCache cache;

    public StateEntity create(final ServerEntity server) {
        final var state = repository.create(server, new StateBody());
        log.info("State created for server {}", server.getName());
        return state;
    }

    public Optional<StateEntity> getByServerOptional(final ServerEntity server) {
        return repository.findByServer(server);
    }

    public StateEntity getByServerRequired(final ServerEntity server) {
        return repository.findByServer(server)
                .orElseThrow(() -> new NotFoundException("Server %s state not found"
                        .formatted(server.getName())));
    }

    public void addZone(final ServerEntity server, final ZoneEntity zone) {
        updateState(server, body -> body.addZone(zone));
        log.info("Added zone {} to server {} state", zone.getId(), server.getId());
    }

    public void removeZone(final ServerEntity server, final Long zoneId) {
        updateState(server, body -> body.removeZone(zoneId));
        log.info("Removed zone {} from server {} state", zoneId, server.getId());
    }

    public void addSim(final ServerEntity server, final SimEntity sim) {
        updateState(server, body -> body.addSim(sim));
        log.info("Added sim {} to server {} state", sim.getId(), server.getId());
    }

    public void removeSim(final ServerEntity server, final Long simId) {
        updateState(server, body -> body.removeSim(simId));
        log.info("Removed sim {} from server {} state", simId, server.getId());
    }

    void updateState(final ServerEntity server, final Consumer<StateBody> mutator) {
        final var state = getByServerRequired(server);
        mutator.accept(state.getBody());
        state.setUpdatedAt(OffsetDateTime.now());
        cache.cacheServerState(server, state);
    }
}
