package sh.byv.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CommandType {
    ADD_ZONE(1),
    REMOVE_ZONE(2),
    ADD_SIM(3),
    REMOVE_SIM(4);

    final int id;

    public static CommandType fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
