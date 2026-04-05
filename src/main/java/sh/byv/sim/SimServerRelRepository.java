package sh.byv.sim;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.server.ServerEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class SimServerRelRepository implements PanacheRepository<SimServerRelEntity> {

    public SimServerRelEntity create(final SimEntity sim, final ServerEntity server) {
        final var rel = new SimServerRelEntity();
        rel.setSim(sim);
        rel.setServer(server);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        persist(rel);
        return rel;
    }
}
