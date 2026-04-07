package sh.byv.state;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.instance.InstanceEntity;
import sh.byv.sim.SimEntity;
import sh.byv.zone.ZoneEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Data
public class ClusterState {

    final Map<Long, InstanceState> instances;
    final Map<Long, ZoneState> zones;

    public ClusterState() {
        instances = new HashMap<>();
        zones = new HashMap<>();
    }

    void addInstance(final InstanceEntity instance) {
        final var instanceState = new InstanceState(instance);
        instances.put(instance.getId(), instanceState);
    }

    void addZone(final InstanceEntity instance, final ZoneEntity zone) {
        final var zoneState = new ZoneState(zone);
        zones.put(zone.getId(), zoneState);

        final var instanceState = instances.get(instance.getId());
        if (instanceState != null) {
            instanceState.addZone(zone);
        } else {
            log.warn("Instance {} not found in cluster state, zone {} not linked", instance.getId(), zone.getId());
        }
    }

    void removeZone(final Long zoneId) {
        final var zone = zones.remove(zoneId);
        if (zone != null) {
            for (final var instanceState : instances.values()) {
                instanceState.zones.remove(zoneId);
                instanceState.sims.removeAll(zone.sims.keySet());
            }
        }
    }

    void addSim(final InstanceEntity instance, final SimEntity sim) {
        final var zone = zones.get(sim.getZone().getId());
        if (zone != null) {
            zone.addSim(sim);

            final var instanceState = instances.get(instance.getId());
            if (instanceState != null) {
                instanceState.addSim(sim);
            } else {
                log.warn("Instance {} not found in cluster state, sim {} not linked", instance.getId(), sim.getId());
            }
        } else {
            log.warn("Zone {} not found in cluster state, sim {} not added", sim.getZone().getId(), sim.getId());
        }
    }

    void removeSim(final Long simId) {
        for (final var zone : zones.values()) {
            zone.sims.remove(simId);
        }
        for (final var instanceState : instances.values()) {
            instanceState.sims.remove(simId);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class InstanceState {

        Long id;
        Set<Long> zones;
        Set<Long> sims;

        InstanceState(final InstanceEntity instance) {
            id = instance.getId();
            zones = new HashSet<>();
            sims = new HashSet<>();
        }

        void addZone(final ZoneEntity zone) {
            zones.add(zone.getId());
        }

        void addSim(final SimEntity sim) {
            sims.add(sim.getId());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ZoneState {

        long id;
        int x1;
        int y1;
        int x2;
        int y2;
        Map<Long, SimState> sims;

        ZoneState(final ZoneEntity zone) {
            id = zone.getId();
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