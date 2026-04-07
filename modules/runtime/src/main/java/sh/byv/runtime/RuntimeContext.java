package sh.byv.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.world.WorldService;

@ApplicationScoped
@AllArgsConstructor
public class RuntimeContext {

    final WorldContextBuilder builder;
    final WorldService worlds;

    public WorldContextBuilder.WorldContext newWorld(final String name) {
        final var world = worlds.create(name);
        return builder.build(world);
    }

    public WorldContextBuilder.WorldContext getWorld(final String name) {
        final var world = worlds.getByNameRequired(name);
        return builder.build(world);
    }
}
