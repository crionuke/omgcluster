package sh.byv.server;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import sh.byv.event.EntityStatus;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_server", uniqueConstraints = @UniqueConstraint(columnNames = {"internal_address"}))
public class ServerEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;

    @Column(name = "internal_address", nullable = false)
    String internalAddress;

    @Column(name = "external_address", nullable = false)
    String externalAddress;

    @Column(name = "status", nullable = false)
    EntityStatus status;
}
