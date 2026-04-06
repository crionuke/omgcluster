package sh.byv.sim;

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
public class SimGroupRelService {

    final SimGroupRelRepository repository;
    final EventService events;

    public SimGroupRelEntity create(final SimEntity sim, final GroupEntity group) {
        final var rel = repository.create(sim, group);
        events.create(EventType.SIM_GROUP_REL_CREATED, rel.getId());
        log.info("Sim {} relation to group {} created", sim.getId(), group.getName());
        return rel;
    }

    public SimGroupRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sim group rel not found: " + id));
    }

    public GroupEntity getLeastPopulatedGroup() {
        return repository.findLeastPopulatedGroup()
                .orElseThrow(() -> new NotFoundException("No groups found"));
    }
}
