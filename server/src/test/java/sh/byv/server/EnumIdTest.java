package sh.byv.server;

import org.junit.jupiter.api.Test;
import sh.byv.conn.ConnStatus;
import sh.byv.conn.ConnZoneRelStatus;
import sh.byv.event.EventStatus;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceRelStatus;
import sh.byv.instance.InstanceRelType;
import sh.byv.instance.InstanceStatus;
import sh.byv.layer.LayerStatus;
import sh.byv.sim.SimStatus;
import sh.byv.world.WorldStatus;
import sh.byv.zone.ZoneStatus;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumIdTest {

    @Test
    void eventTypeIds() {
        assertUniqueAndSequential(Arrays.stream(EventType.values()).mapToInt(EventType::getId).toArray());
    }

    @Test
    void eventStatusIds() {
        assertUniqueAndSequential(Arrays.stream(EventStatus.values()).mapToInt(EventStatus::getId).toArray());
    }

    @Test
    void worldStatusIds() {
        assertUniqueAndSequential(Arrays.stream(WorldStatus.values()).mapToInt(WorldStatus::getId).toArray());
    }

    @Test
    void layerStatusIds() {
        assertUniqueAndSequential(Arrays.stream(LayerStatus.values()).mapToInt(LayerStatus::getId).toArray());
    }

    @Test
    void zoneStatusIds() {
        assertUniqueAndSequential(Arrays.stream(ZoneStatus.values()).mapToInt(ZoneStatus::getId).toArray());
    }

    @Test
    void simStatusIds() {
        assertUniqueAndSequential(Arrays.stream(SimStatus.values()).mapToInt(SimStatus::getId).toArray());
    }

    @Test
    void instanceRelStatusIds() {
        assertUniqueAndSequential(Arrays.stream(InstanceRelStatus.values())
                .mapToInt(InstanceRelStatus::getId).toArray());
    }

    @Test
    void instanceRelTypeIds() {
        assertUniqueAndSequential(Arrays.stream(InstanceRelType.values())
                .mapToInt(InstanceRelType::getId).toArray());
    }

    @Test
    void instanceStatusIds() {
        assertUniqueAndSequential(Arrays.stream(InstanceStatus.values()).mapToInt(InstanceStatus::getId).toArray());
    }

    @Test
    void connStatusIds() {
        assertUniqueAndSequential(Arrays.stream(ConnStatus.values()).mapToInt(ConnStatus::getId).toArray());
    }

    @Test
    void connZoneRelStatusIds() {
        assertUniqueAndSequential(Arrays.stream(ConnZoneRelStatus.values())
                .mapToInt(ConnZoneRelStatus::getId).toArray());
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
