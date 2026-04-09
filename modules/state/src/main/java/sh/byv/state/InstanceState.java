package sh.byv.state;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.sim.SimEntity;
import sh.byv.zone.ZoneEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class InstanceState {

    final Map<Long, ZoneState> zones;

    public InstanceState() {
        zones = new HashMap<>();
    }

    void addZone(final ZoneEntity zone) {
        final var zoneState = new ZoneState(zone);
        zones.put(zone.getId(), zoneState);
    }

    void removeZone(final Long zoneId) {
        zones.remove(zoneId);
    }

    void addSim(final SimEntity sim) {
        final var zone = zones.get(sim.getZone().getId());
        if (zone != null) {
            zone.addSim(sim);
        } else {
            log.warn("Zone {} not found in cluster state, sim {} not added", sim.getZone().getId(), sim.getId());
        }
    }

    void removeSim(final Long simId) {
        for (final var zone : zones.values()) {
            zone.sims.remove(simId);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ZoneState {

        long id;
        String name;
        String world;
        int x1;
        int y1;
        int x2;
        int y2;
        Map<Long, SimState> sims;

        ZoneState(final ZoneEntity zone) {
            id = zone.getId();
            name = zone.getLayer().getName();
            world = zone.getLayer().getWorld().getName();
            x1 = zone.getX1();
            y1 = zone.getY1();
            x2 = zone.getX2();
            y2 = zone.getY2();
            sims = new HashMap<>();
        }

        void addSim(final SimEntity sim) {
            sims.put(sim.getId(), new SimState(sim));
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SimState {

        long id;
        String name;

        SimState(final SimEntity sim) {
            id = sim.getId();
            name = sim.getName();
        }
    }
}