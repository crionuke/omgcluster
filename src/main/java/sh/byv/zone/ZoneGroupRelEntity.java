package sh.byv.zone;

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
import sh.byv.group.GroupEntity;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_zone_group_rel", uniqueConstraints = @UniqueConstraint(columnNames = {"zone_id", "group_id"}))
public class ZoneGroupRelEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    ZoneEntity zone;

    @ManyToOne
    @JoinColumn(name = "group_id")
    GroupEntity group;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;
}
