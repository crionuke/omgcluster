package sh.byv.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.world.WorldService;

@ApplicationScoped
@AllArgsConstructor
public class ClusterContext {

    final WorldContextBuilder worldContextBuilder;
    final WorldService worldService;

    public WorldContextBuilder.WorldContext newWorld(final String name) {
        final var world = worldService.create(name);
        return worldContextBuilder.build(world);
    }

    public WorldContextBuilder.WorldContext getWorld(final String name) {
        final var world = worldService.getRequired(name);
        return worldContextBuilder.build(world);
    }
}
