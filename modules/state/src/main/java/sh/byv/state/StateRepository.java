package sh.byv.state;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class StateRepository implements PanacheRepository<StateEntity> {

    StateEntity create(final StateType type, final JsonNode body) {
        final var state = new StateEntity();
        state.setCreatedAt(OffsetDateTime.now());
        state.setUpdatedAt(OffsetDateTime.now());
        state.setType(type);
        state.setBody(body);
        persist(state);
        return state;
    }

    Optional<StateEntity> findByType(final StateType type) {
        return find("type = ?1", type).firstResultOptional();
    }
}
