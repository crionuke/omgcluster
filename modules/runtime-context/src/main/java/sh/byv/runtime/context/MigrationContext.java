package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sh.byv.world.entity.WorldService;

@AllArgsConstructor
public class MigrationContext {

    final WorldContext.Builder builder;
    final WorldService worlds;

    @Getter
    final int version;

    public WorldContext newWorld(final String name) {
        final var world = worlds.create(name);
        return builder.build(world);
    }

    public WorldContext getWorld(final String name) {
        final var world = worlds.getByNameRequired(name);
        return builder.build(world);
    }

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        final WorldContext.Builder builder;
        final WorldService worlds;

        public MigrationContext build(final int version) {
            return new MigrationContext(builder, worlds, version);
        }
    }
}
