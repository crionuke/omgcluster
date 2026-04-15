package sh.byv.server.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventService;
import sh.byv.event.entity.EventType;
import sh.byv.exception.clazz.NotFoundException;

import java.util.Optional;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    final ServerRepository repository;
    final ServerConfig config;
    final EventService events;

    public ServerEntity create(final String name) {
        final var server = repository.create(name);
        events.create(EventType.SERVER_CREATED, server.getId());
        log.info("Server {} created with id {}", name, server.getId());
        return server;
    }

    public ServerEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Server not found: " + id));
    }

    public Optional<ServerEntity> getByNameOptional(final String name) {
        return repository.findByName(name);
    }

    public ServerEntity getByNameRequired(final String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Server not found: " + name));
    }

    public ServerEntity getThisServer() {
        return getByNameRequired(config.name());
    }

    public void activate(final ServerEntity server) {
        server.setStatus(ServerStatus.ACTIVE);
        events.create(EventType.SERVER_ACTIVATED, server.getId());
    }

    public ServerEntity getOrCreate(final String name) {
        final var existing = repository.findByName(name);
        if (existing.isPresent()) {
            log.info("Server {} already exists with id {}", name, existing.get().getId());
            return existing.get();
        }

        return create(name);
    }
}
