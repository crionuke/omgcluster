package sh.byv.state.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sh.byv.sim.entity.SimEntity;
import sh.byv.zone.entity.ZoneEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class StateBody {

    final Map<Long, ZoneState> zones;
    final Map<Long, SimState> sims;

    public StateBody() {
        zones = new HashMap<>();
        sims = new HashMap<>();
    }

    void addZone(final ZoneEntity zone) {
        final var zoneState = new ZoneState(zone);
        zones.put(zone.getId(), zoneState);
    }

    void removeZone(final Long zoneId) {
        zones.remove(zoneId);
    }

    void addSim(final SimEntity sim) {
        final var simState = new SimState(sim);
        sims.put(sim.getId(), simState);
    }

    void removeSim(final Long simId) {
        sims.remove(simId);
    }

    public record ZoneState(long id, String layer, String world) {

        ZoneState(final ZoneEntity zone) {
            this(zone.getId(), zone.getLayer().getName(), zone.getLayer().getWorld().getName());
        }
    }

    public record SimState(long id, long zoneId, String name) {

        SimState(final SimEntity sim) {
            this(sim.getId(), sim.getZone().getId(), sim.getName());
        }
    }
}