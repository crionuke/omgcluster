package sh.byv.cache.service;

public record CacheEvent(Type type, long entityId) {

    public enum Type {
        SERVER_ZONES,
        SERVER_SIMS,
        ZONE_SIMS,
        CACHED_SIM
    }
}