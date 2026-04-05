package sh.byv.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.layer.LayerService;
import sh.byv.world.WorldEntity;

@ApplicationScoped
@AllArgsConstructor
public class WorldContextBuilder {

    final LayerContextBuilder builder;
    final LayerService service;

    public WorldContext build(final WorldEntity world) {
        return new WorldContext(world);
    }

    public class WorldContext {

        final WorldEntity world;

        public WorldContext(final WorldEntity world) {
            this.world = world;
        }

        public LayerContextBuilder.LayerContext newLayer(final String name) {
            final var layer = service.create(world, name);
            return builder.build(layer);
        }

        public LayerContextBuilder.LayerContext getLayer(final String name) {
            final var layer = service.getByNameRequired(world, name);
            return builder.build(layer);
        }
    }
}
