package sh.byv.runtime.service;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
@DefaultBean
@ApplicationScoped
@AllArgsConstructor
public class RuntimeStub implements RuntimeService {

    @Override
    public void migrate(final MigrationContext context) {
        final var version = context.getVersion();

        if (version == 1) {
            final var world = context.newWorld("stub_world");

            world.newLayer("game_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("game_sim");
        }
    }

    @Override
    public Object initialize(final InitialiationContext context) {
        return new HashMap<>();
    }

    @Override
    public Object simulate(final SimulationContext context) {
        return new HashMap<>();
    }

    @Override
    public Object aggregate(final AggregationContext context) {
        return context.getState();
    }
}
