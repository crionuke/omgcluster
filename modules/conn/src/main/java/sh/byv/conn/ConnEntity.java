package sh.byv.conn;

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
import sh.byv.node.NodeEntity;
import sh.byv.world.WorldEntity;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "omgc_conn", uniqueConstraints = @UniqueConstraint(columnNames = {"node_id", "world_id"}))
public class ConnEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "node_id")
    NodeEntity node;

    @ManyToOne
    @JoinColumn(name = "world_id")
    WorldEntity world;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    OffsetDateTime updatedAt;

    @Column(name = "status", nullable = false)
    ConnStatus status;
}
