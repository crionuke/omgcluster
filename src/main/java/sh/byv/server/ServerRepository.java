package sh.byv.server;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sh.byv.event.EntityStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class ServerRepository implements PanacheRepository<ServerEntity> {

    ServerEntity create(final String internalAddress, final String externalAddress) {
        final var server = new ServerEntity();
        server.setCreatedAt(OffsetDateTime.now());
        server.setUpdatedAt(OffsetDateTime.now());
        server.setInternalAddress(internalAddress);
        server.setExternalAddress(externalAddress);
        server.setStatus(EntityStatus.PENDING);
        persist(server);
        return server;
    }

    Optional<ServerEntity> findByInternalAddress(final String internalAddress) {
        return find("internalAddress", internalAddress).firstResultOptional();
    }
}
