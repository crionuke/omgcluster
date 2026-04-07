package sh.byv.instance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InstanceService {

    final InstanceRepository repository;
    final InstanceConfig config;
    final EventService events;

    public InstanceEntity create(final String name) {
        final var instance = repository.create(name);
        events.create(EventType.INSTANCE_CREATED, instance.getId());
        log.info("Instance {} created with id {}", name, instance.getId());
        return instance;
    }

    public InstanceEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Instance not found: " + id));
    }

    public InstanceEntity getByNameRequired(final String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Instance not found: " + name));
    }

    public InstanceEntity getThisInstance() {
        return getByNameRequired(config.name());
    }

    public InstanceEntity getOrCreate(final String name) {
        final var existing = repository.findByName(name);
        if (existing.isPresent()) {
            log.info("Instance {} already exists with id {}", name, existing.get().getId());
            return existing.get();
        }

        return create(name);
    }
}
