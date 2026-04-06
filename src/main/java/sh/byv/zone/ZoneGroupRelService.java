package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.group.GroupEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneGroupRelService {

    final ZoneGroupRelRepository repository;

    public ZoneGroupRelEntity create(final ZoneEntity zone, final GroupEntity group) {
        final var rel = repository.create(zone, group);
        log.info("Zone {} relation to group {} created", zone.getId(), group.getName());
        return rel;
    }

    public GroupEntity getLeastPopulatedGroup() {
        return repository.findLeastPopulatedGroup()
                .orElseThrow(() -> new NotFoundException("No groups found"));
    }
}
