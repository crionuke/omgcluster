package sh.byv.cache.service;

import sh.byv.sim.entity.SimEntity;
import sh.byv.sim.entity.SimStatus;

public record CachedSim(long zoneId, String name, SimStatus status) {

    public static CachedSim from(final SimEntity sim) {
        return new CachedSim(sim.getZone().getId(), sim.getName(), sim.getStatus());
    }
}
