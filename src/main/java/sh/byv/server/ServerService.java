package sh.byv.server;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;
import sh.byv.group.GroupEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    final ServerRepository repository;
    final EventService events;

    public ServerEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Server not found: " + id));
    }

    public ServerEntity getByInternalAddressRequired(final String internalAddress) {
        return repository.findByInternalAddress(internalAddress)
                .orElseThrow(() -> new NotFoundException("Server not found: " + internalAddress));
    }

    @Transactional
    public ServerEntity create(final GroupEntity group,
                               final String internalAddress,
                               final String externalAddress) {
        final var server = repository.create(group, internalAddress, externalAddress);
        events.create(EventType.SERVER_CREATED, server.getId());
        log.info("Server {} created with id {}", internalAddress, server.getId());
        return server;
    }

    @Transactional
    public ServerEntity getOrCreate(final GroupEntity group,
                                    final String internalAddress,
                                    final String externalAddress) {
        final var existing = repository.findByInternalAddress(internalAddress);
        if (existing.isPresent()) {
            log.debug("Server {} already exists with id {}", internalAddress, existing.get().getId());
            return existing.get();
        }

        return create(group, internalAddress, externalAddress);
    }
}