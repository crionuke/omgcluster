package sh.byv.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ServerRelType {
    ZONE(1),
    SIM(2);

    final int id;

    public static ServerRelType fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
