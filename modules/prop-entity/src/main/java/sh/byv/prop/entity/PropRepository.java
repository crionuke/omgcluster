package sh.byv.prop.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
class PropRepository implements PanacheRepository<PropEntity> {

    PropEntity create(final PropType type, final JsonNode value) {
        final var now = OffsetDateTime.now();
        final var prop = new PropEntity();
        prop.setCreatedAt(now);
        prop.setUpdatedAt(now);
        prop.setType(type);
        prop.setValue(value);
        persist(prop);
        return prop;
    }

    Optional<PropEntity> findByType(final PropType type) {
        return find("type", type).firstResultOptional();
    }
}