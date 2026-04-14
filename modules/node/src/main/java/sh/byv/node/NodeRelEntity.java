package sh.byv.node;

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

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_node_rel", uniqueConstraints = @UniqueConstraint(columnNames = {"node_id", "entity_id",
        "type"}))
public class NodeRelEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    NodeEntity node;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;

    @Column(name = "type", nullable = false)
    NodeRelType type;

    @Column(name = "entity_id", nullable = false)
    Long entityId;

    @Column(name = "status", nullable = false)
    NodeRelStatus status;
}
