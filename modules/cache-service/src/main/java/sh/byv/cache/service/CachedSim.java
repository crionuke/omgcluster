package sh.byv.cache.service;

import sh.byv.sim.entity.SimStatus;

public record CachedSim(long zoneId, String name, SimStatus status) {
}