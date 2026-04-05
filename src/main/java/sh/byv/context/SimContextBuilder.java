package sh.byv.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.sim.SimEntity;

@ApplicationScoped
@AllArgsConstructor
public class SimContextBuilder {

    public SimContextBuilder.SimContext build(final SimEntity sim) {
        return new SimContext(sim);
    }

    public class SimContext {

        final SimEntity sim;

        public SimContext(final SimEntity sim) {
            this.sim = sim;
        }
    }
}
