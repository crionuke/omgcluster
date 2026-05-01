package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.sim.entity.SimEntity;

public class SimContext {

    final SimEntity sim;

    SimContext(final SimEntity sim) {
        this.sim = sim;
    }

    @ApplicationScoped
    public static class Builder {

        public SimContext build(final SimEntity sim) {
            return new SimContext(sim);
        }
    }
}
