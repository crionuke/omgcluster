package sh.byv.server.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class ServerRepository implements PanacheRepository<ServerEntity> {

    ServerEntity create(final String name) {
        final var server = new ServerEntity();
        server.setCreatedAt(OffsetDateTime.now());
        server.setUpdatedAt(OffsetDateTime.now());
        server.setName(name);
        server.setStatus(ServerStatus.PENDING);
        persist(server);
        return server;
    }

    Optional<ServerEntity> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }
}
