package sh.byv.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventType {
    WORLD_CREATED(1),
    WORLD_DELETED(2),
    LAYER_CREATED(3),
    LAYER_DELETED(4),
    ZONE_CREATED(5),
    ZONE_DELETED(6),
    SIM_CREATED(7),
    SIM_DELETED(8),
    INSTANCE_CREATED(9),
    INSTANCE_STARTED(10),
    INSTANCE_DELETED(11),
    CONN_CREATED(12),
    CONN_DELETED(13),
    CONN_ZONE_REL_CREATED(14),
    CONN_ZONE_REL_DELETED(15),
    SIM_INSTANCE_REL_CREATED(16),
    SIM_INSTANCE_REL_DELETED(17),
    ZONE_INSTANCE_REL_CREATED(18),
    ZONE_INSTANCE_REL_DELETED(19);

    final int id;

    public static EventType fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
