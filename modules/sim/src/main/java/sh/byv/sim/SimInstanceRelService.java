package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceEntity;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimInstanceRelService {

    final SimInstanceRelRepository repository;
    final EventService events;

    public SimInstanceRelEntity create(final SimEntity sim, final InstanceEntity instance) {
        final var rel = repository.create(sim, instance);
        events.create(EventType.SIM_INSTANCE_REL_CREATED, rel.getId());
        log.info("Sim {} relation to instance {} created", sim.getId(), instance.getId());
        return rel;
    }

    public SimInstanceRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public InstanceEntity getLeastPopulatedInstance() {
        return repository.findLeastPopulatedInstance()
                .orElseThrow(() -> new NotFoundException("No instances found"));
    }
}
