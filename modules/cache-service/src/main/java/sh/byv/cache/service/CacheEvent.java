package sh.byv.cache.service;

public record CacheEvent(Type type, long entityId) {

    public enum Type {
        SERVER_ZONES,
        SERVER_SIMS,
        ZONE_SIMS,
        SIM_ENTITY,
        ZONE_ENTITY,
        LAYER_ENTITY,
        WORLD_ENTITY
    }
}
