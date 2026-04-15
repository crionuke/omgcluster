package sh.byv.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ServerStatus {
    PENDING(1),
    ACTIVE(2),
    SUSPENDED(3),
    DELETED(4);

    final int id;

    public static ServerStatus fromId(final int id) {
        return Arrays.stream(values())
                .filter(status -> status.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
