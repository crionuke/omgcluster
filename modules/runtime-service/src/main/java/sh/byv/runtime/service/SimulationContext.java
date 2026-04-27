package sh.byv.runtime.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sh.byv.sim.entity.SimModel;

@Getter
@AllArgsConstructor
public class SimulationContext {

    final long tick;

    final SimModel sim;
    final Object state;

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        public SimulationContext build(final long tick, final SimModel sim, final Object state) {
            return new SimulationContext(tick, sim, state);
        }
    }
}
