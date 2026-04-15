package sh.byv.state.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.server.entity.ServerEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class StateRepository implements PanacheRepository<StateEntity> {

    StateEntity create(final ServerEntity server, final StateBody body) {
        final var state = new StateEntity();
        state.setServer(server);
        state.setCreatedAt(OffsetDateTime.now());
        state.setUpdatedAt(OffsetDateTime.now());
        state.setBody(body);
        persist(state);
        return state;
    }

    Optional<StateEntity> findByServer(final ServerEntity server) {
        return find("server", server).firstResultOptional();
    }
}
