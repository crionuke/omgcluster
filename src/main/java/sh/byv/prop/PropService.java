package sh.byv.prop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class PropService {

    final ObjectMapper objectMapper;
    final PropRepository repository;

    public int getInt(final PropType type) {
        final var prop = repository.findByType(type);
        final var raw = prop.isPresent() ? prop.get().getValue() : type.getDefaultValue();
        return objectMapper.convertValue(raw, Integer.class);
    }

    @Transactional
    public PropEntity setInt(final PropType type, final int value) {
        return set(type, value);
    }

    @Transactional
    public PropEntity setString(final PropType type, final String value) {
        return set(type, value);
    }

    private PropEntity set(final PropType type, final Object value) {
        final var node = objectMapper.valueToTree(value);
        final var prop = repository.findByType(type);
        if (prop.isPresent()) {
            log.info("Update prop, type={}", type);
            final var entity = prop.get();
            entity.setUpdatedAt(OffsetDateTime.now());
            entity.setValue(node);
            return entity;
        } else {
            log.info("Create prop, type={}", type);
            return repository.create(type, node);
        }
    }
}