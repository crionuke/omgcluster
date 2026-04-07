package sh.byv.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CommandStatus {
    PENDING(1),
    PROCESSED(2),
    FAILED(3);

    final int id;

    public static CommandStatus fromId(final int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
    }
}
