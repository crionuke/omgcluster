package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.layer.entity.LayerService;
import sh.byv.world.entity.WorldEntity;

public class WorldContext {

    final LayerContext.Builder layerBuilder;
    final LayerService service;
    final WorldEntity world;

    WorldContext(final LayerContext.Builder layerBuilder, final LayerService service, final WorldEntity world) {
        this.layerBuilder = layerBuilder;
        this.service = service;
        this.world = world;
    }

    public LayerContext newLayer(final String name) {
        final var layer = service.create(world, name);
        return layerBuilder.build(layer);
    }

    public LayerContext getLayer(final String name) {
        final var layer = service.getByNameRequired(world, name);
        return layerBuilder.build(layer);
    }

    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        final LayerContext.Builder layerBuilder;
        final LayerService service;

        public WorldContext build(final WorldEntity world) {
            return new WorldContext(layerBuilder, service, world);
        }
    }
}
