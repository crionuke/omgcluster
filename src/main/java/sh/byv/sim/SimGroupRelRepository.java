package sh.byv.sim;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.group.GroupEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class SimGroupRelRepository implements PanacheRepository<SimGroupRelEntity> {

    public SimGroupRelEntity create(final SimEntity sim, final GroupEntity group) {
        final var rel = new SimGroupRelEntity();
        rel.setSim(sim);
        rel.setGroup(group);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        persist(rel);
        return rel;
    }
}
