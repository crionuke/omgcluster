package sh.byv.group;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<GroupEntity> {

    GroupEntity create(final String name) {
        final var group = new GroupEntity();
        group.setCreatedAt(OffsetDateTime.now());
        group.setUpdatedAt(OffsetDateTime.now());
        group.setName(name);
        group.setStatus(EntityStatus.PENDING);
        persistAndFlush(group);
        return group;
    }

    Optional<GroupEntity> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }
}