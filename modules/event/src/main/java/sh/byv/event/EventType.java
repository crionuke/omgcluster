package sh.byv.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventType {
    WORLD_CREATED(1),
    WORLD_ACTIVATED(2),
    WORLD_SUSPENDED(3),
    WORLD_DELETED(4),
    LAYER_CREATED(5),
    LAYER_ACTIVATED(6),
    LAYER_SUSPENDED(7),
    LAYER_DELETED(8),
    ZONE_CREATED(9),
    ZONE_ACTIVATED(10),
    ZONE_SUSPENDED(11),
    ZONE_DELETED(12),
    SIM_CREATED(13),
    SIM_ACTIVATED(14),
    SIM_SUSPENDED(15),
    SIM_DELETED(16),
    INSTANCE_CREATED(17),
    INSTANCE_STARTED(18),
    INSTANCE_ACTIVATED(19),
    INSTANCE_SUSPENDED(20),
    INSTANCE_DELETED(21),
    CONN_CREATED(22),
    CONN_ACTIVATED(23),
    CONN_SUSPENDED(24),
    CONN_DELETED(25),
    CONN_ZONE_REL_CREATED(26),
    CONN_ZONE_REL_ACTIVATED(27),
    CONN_ZONE_REL_SUSPENDED(28),
    CONN_ZONE_REL_DELETED(29),
    INSTANCE_REL_CREATED(30),
    INSTANCE_REL_ACTIVATED(31),
    INSTANCE_REL_SUSPENDED(32),
    INSTANCE_REL_DELETED(33);

    final int id;

    public static EventType fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
