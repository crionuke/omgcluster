package sh.byv.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EntityStatus {
    PENDING(1),
    CREATED(2),
    DELETING(3);

    final int id;

    public static EntityStatus fromId(final int id) {
        return Arrays.stream(values())
                .filter(status -> status.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
