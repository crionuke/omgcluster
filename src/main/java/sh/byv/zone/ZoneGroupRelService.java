package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.group.GroupEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneGroupRelService {

    final ZoneGroupRelRepository repository;
    final EventService events;

    public ZoneGroupRelEntity create(final ZoneEntity zone, final GroupEntity group) {
        final var rel = repository.create(zone, group);
        events.create(EventType.ZONE_GROUP_REL_CREATED, rel.getId());
        log.info("Zone {} relation to group {} created", zone.getId(), group.getName());
        return rel;
    }

    public ZoneGroupRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Zone group rel not found: " + id));
    }

    public GroupEntity getLeastPopulatedGroup() {
        return repository.findLeastPopulatedGroup()
                .orElseThrow(() -> new NotFoundException("No groups found"));
    }
}
