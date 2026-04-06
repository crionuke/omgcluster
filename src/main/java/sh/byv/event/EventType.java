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
    SERVER_CREATED(9),
    SERVER_DELETED(10),
    CONN_CREATED(11),
    CONN_DELETED(12),
    CONN_ZONE_REL_CREATED(13),
    CONN_ZONE_REL_DELETED(14),
    SIM_SERVER_REL_CREATED(15),
    SIM_SERVER_REL_DELETED(16),
    ZONE_SERVER_REL_CREATED(17),
    ZONE_SERVER_REL_DELETED(18);

    final int id;

    public static EventType fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
