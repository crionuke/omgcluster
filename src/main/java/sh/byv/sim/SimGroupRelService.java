package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.group.GroupEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimGroupRelService {

    final SimGroupRelRepository repository;

    public SimGroupRelEntity create(final SimEntity sim, final GroupEntity group) {
        final var rel = repository.create(sim, group);
        log.info("Sim {} relation to group {} created", sim.getId(), group.getName());
        return rel;
    }

    public GroupEntity getLeastPopulatedGroup() {
        return repository.findLeastPopulatedGroup()
                .orElseThrow(() -> new NotFoundException("No groups found"));
    }
}
