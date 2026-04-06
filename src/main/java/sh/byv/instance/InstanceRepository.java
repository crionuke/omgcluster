package sh.byv.instance;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class InstanceRepository implements PanacheRepository<InstanceEntity> {

    InstanceEntity create(final String internalAddress,
                        final String externalAddress) {
        final var instance = new InstanceEntity();
        instance.setCreatedAt(OffsetDateTime.now());
        instance.setUpdatedAt(OffsetDateTime.now());
        instance.setInternalAddress(internalAddress);
        instance.setExternalAddress(externalAddress);
        instance.setStatus(EntityStatus.PENDING);
        persist(instance);
        return instance;
    }

    Optional<InstanceEntity> findByInternalAddress(final String internalAddress) {
        return find("internalAddress", internalAddress).firstResultOptional();
    }
}
