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
    public void migrateCluster(final RuntimeContext runtime, final int version) {
        if (version == 1) {
            final var world = runtime.newWorld("stub_world");

            world.newLayer("game_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("game_sim");
        }
    }

    @Override
    public Object simulateZone(final long tick, final String sim, final Object zoneState) {
        return new HashMap<>();
    }

    @Override
    public Object initZone() {
        return new HashMap<>();
    }

    @Override
    public Object computeZone(final Object prevZoneState, final List<Object> simStates, final long tick) {
        return prevZoneState;
    }
}
