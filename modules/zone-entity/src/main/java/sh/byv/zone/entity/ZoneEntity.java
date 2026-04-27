package sh.byv.zone.entity;

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
import sh.byv.layer.entity.LayerEntity;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_zone", uniqueConstraints = @UniqueConstraint(columnNames = {"layer_id", "x1", "y1", "x2",
        "y2"}))
public class ZoneEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "layer_id")
    LayerEntity layer;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    ZoneEntity parent;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;

    @Column(name = "status", nullable = false)
    ZoneStatus status;

    @Column(name = "x1", nullable = false)
    Integer x1;

    @Column(name = "y1", nullable = false)
    Integer y1;

    @Column(name = "x2", nullable = false)
    Integer x2;

    @Column(name = "y2", nullable = false)
    Integer y2;

    public ZoneModel toModel() {
        final var layerModel = layer.toModel();

        return new ZoneModel(id, createdAt, updatedAt, status,
                new ZoneRect(x1, y1, x2, y2),
                parent != null ? parent.getId() : null,
                layerModel.id(), layerModel.name(),
                layerModel.worldId(), layerModel.worldName());
    }
}
