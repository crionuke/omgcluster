package sh.byv.runtime.stub;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.runtime.context.AggregationContext;
import sh.byv.runtime.context.ZoneCreatedContext;
import sh.byv.runtime.context.MigrationContext;
import sh.byv.runtime.context.SimulationContext;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.sim.result.SimResult;
import sh.byv.zone.state.ZoneState;

@Slf4j
@DefaultBean
@ApplicationScoped
@AllArgsConstructor
public class RuntimeStub implements RuntimeService {

    @Override
    public void onMigration(final MigrationContext context) {
        final var version = context.getVersion();

        if (version == 1) {
            final var world = context.newWorld("stub_world");

            world.newLayer("game_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("game_sim");
        }
    }

    @Override
    public ZoneState onZoneCreated(final ZoneCreatedContext context) {
        return new ZoneStateStub();
    }

    @Override
    public SimResult onSimulation(final SimulationContext context) {
        return new SimResultStub();
    }

    @Override
    public ZoneState onAggregation(final AggregationContext context) {
        return context.getState();
    }
}
