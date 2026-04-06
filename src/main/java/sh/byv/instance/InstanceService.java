package sh.byv.instance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InstanceService {

    final InstanceRepository repository;
    final EventService events;

    public InstanceEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Instance not found: " + id));
    }

    public InstanceEntity getByInternalAddressRequired(final String internalAddress) {
        return repository.findByInternalAddress(internalAddress)
                .orElseThrow(() -> new NotFoundException("Instance not found: " + internalAddress));
    }

    @Transactional
    public InstanceEntity create(final String internalAddress,
                               final String externalAddress) {
        final var instance = repository.create(internalAddress, externalAddress);
        events.create(EventType.INSTANCE_CREATED, instance.getId());
        log.info("Instance {} created with id {}", internalAddress, instance.getId());
        return instance;
    }

    @Transactional
    public InstanceEntity getOrCreate(final String internalAddress,
                                    final String externalAddress) {
        final var existing = repository.findByInternalAddress(internalAddress);
        if (existing.isPresent()) {
            log.debug("Instance {} already exists with id {}", internalAddress, existing.get().getId());
            return existing.get();
        }

        return create(internalAddress, externalAddress);
    }
}
