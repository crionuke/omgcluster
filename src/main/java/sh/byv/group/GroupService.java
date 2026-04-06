package sh.byv.group;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class GroupService {

    final GroupRepository repository;
    final EventService events;

    public GroupEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Group not found: " + id));
    }

    public GroupEntity getByNameRequired(final String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Group not found: " + name));
    }

    @Transactional
    public GroupEntity create(final String name) {
        final var group = repository.create(name);
        events.create(EventType.GROUP_CREATED, group.getId());
        log.info("Group created, id={}, name={}", group.getId(), name);
        return group;
    }

    @Transactional
    public GroupEntity getOrCreate(final String name) {
        final var existing = repository.findByName(name);
        if (existing.isPresent()) {
            log.debug("Group already exists, id={}, name={}", existing.get().getId(), name);
            return existing.get();
        }

        return create(name);
    }
}