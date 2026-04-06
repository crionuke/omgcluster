package sh.byv.event;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumIdTest {

    @Test
    void eventTypeIds() {
        assertUniqueAndSequential(Arrays.stream(EventType.values()).mapToInt(EventType::getId).toArray());
    }

    @Test
    void entityStatusIds() {
        assertUniqueAndSequential(Arrays.stream(EntityStatus.values()).mapToInt(EntityStatus::getId).toArray());
    }

    @Test
    void eventStatusIds() {
        assertUniqueAndSequential(Arrays.stream(EventStatus.values()).mapToInt(EventStatus::getId).toArray());
    }

    void assertUniqueAndSequential(final int[] ids) {
        assertEquals(ids.length, Arrays.stream(ids).distinct().count(), "duplicate ids");
        final var sorted = Arrays.stream(ids).sorted().toArray();
        final var expected = new int[sorted.length];
        for (var i = 0; i < expected.length; i++) {
            expected[i] = sorted[0] + i;
        }
        assertArrayEquals(expected, sorted, "ids are not sequential");
    }
}
