package sh.byv.server;

import org.junit.jupiter.api.Test;
import sh.byv.conn.entity.ConnStatus;
import sh.byv.conn.entity.ConnZoneRelStatus;
import sh.byv.event.entity.EventStatus;
import sh.byv.event.entity.EventType;
import sh.byv.node.entity.NodeRelStatus;
import sh.byv.node.entity.NodeRelType;
import sh.byv.node.entity.NodeStatus;
import sh.byv.layer.entity.LayerStatus;
import sh.byv.sim.entity.SimStatus;
import sh.byv.world.entity.WorldStatus;
import sh.byv.zone.entity.ZoneStatus;

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
    void nodeRelStatusIds() {
        assertUniqueAndSequential(Arrays.stream(NodeRelStatus.values())
                .mapToInt(NodeRelStatus::getId).toArray());
    }

    @Test
    void nodeRelTypeIds() {
        assertUniqueAndSequential(Arrays.stream(NodeRelType.values())
                .mapToInt(NodeRelType::getId).toArray());
    }

    @Test
    void nodeStatusIds() {
        assertUniqueAndSequential(Arrays.stream(NodeStatus.values()).mapToInt(NodeStatus::getId).toArray());
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
