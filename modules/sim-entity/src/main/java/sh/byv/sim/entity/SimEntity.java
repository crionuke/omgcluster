package sh.byv.sim.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import sh.byv.zone.entity.ZoneEntity;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_sim", uniqueConstraints = @UniqueConstraint(columnNames = {"zone_id", "name"}))
public class SimEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    ZoneEntity zone;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "status", nullable = false)
    SimStatus status;

    public SimModel toModel() {
        final var zoneModel = zone.toModel();

        return new SimModel(id, createdAt, updatedAt, name, status,
                zoneModel.id(), zoneModel.rect(),
                zoneModel.layerId(), zoneModel.layerName(),
                zoneModel.worldId(), zoneModel.worldName());
    }
}
