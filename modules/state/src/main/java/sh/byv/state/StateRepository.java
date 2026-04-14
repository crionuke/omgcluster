package sh.byv.state;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.node.NodeEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class StateRepository implements PanacheRepository<StateEntity> {

    StateEntity create(final NodeEntity node, final StateBody body) {
        final var state = new StateEntity();
        state.setNode(node);
        state.setCreatedAt(OffsetDateTime.now());
        state.setUpdatedAt(OffsetDateTime.now());
        state.setBody(body);
        persist(state);
        return state;
    }

    Optional<StateEntity> findByNode(final NodeEntity node) {
        return find("node", node).firstResultOptional();
    }
}
