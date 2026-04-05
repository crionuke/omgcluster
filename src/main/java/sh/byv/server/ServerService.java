package sh.byv.server;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    final ServerRepository repository;
    final ServerConfig config;

    @Transactional
    void onStart(@Observes final StartupEvent event) {
        final var internalAddress = config.internalAddress();
        final var existing = repository.findByInternalAddress(internalAddress);
        if (existing.isPresent()) {
            log.info("Server already registered, id={}, internalAddress={}", existing.get().getId(), internalAddress);
            return;
        }

        final var server = repository.create(internalAddress, config.externalAddress());
        log.info("Server created, id={}, internalAddress={}", server.getId(), internalAddress);
    }
}
