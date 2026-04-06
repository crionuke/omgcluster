package sh.byv.state;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.sim.SimEntity;
import sh.byv.zone.ZoneEntity;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
public class StateService {

    final Map<Long, ZoneEntity> zones = new ConcurrentHashMap<>();
    final Map<Long, SimEntity> sims = new ConcurrentHashMap<>();

    public void addZone(final ZoneEntity zone) {
        zones.put(zone.getId(), zone);
        log.info("Added zone to state, zoneId={}", zone.getId());
    }

    public void removeZone(final Long zoneId) {
        zones.remove(zoneId);
        log.info("Removed zone from state, zoneId={}", zoneId);
    }

    public void addSim(final SimEntity sim) {
        sims.put(sim.getId(), sim);
        log.info("Added sim to state, simId={}", sim.getId());
    }

    public void removeSim(final Long simId) {
        sims.remove(simId);
        log.info("Removed sim from state, simId={}", simId);
    }

    public Map<Long, ZoneEntity> getZones() {
        return Collections.unmodifiableMap(zones);
    }

    public Map<Long, SimEntity> getSims() {
        return Collections.unmodifiableMap(sims);
    }
}