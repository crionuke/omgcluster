package sh.byv.runtime.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.sim.entity.SimService;
import sh.byv.zone.entity.ZoneEntity;

@ApplicationScoped
@AllArgsConstructor
public class ZoneContextBuilder {

    final SimContextBuilder builder;
    final SimService service;

    public ZoneContext build(final ZoneEntity zone) {
        return new ZoneContext(zone);
    }

    public class ZoneContext {

        final ZoneEntity zone;

        public ZoneContext(final ZoneEntity zone) {
            this.zone = zone;
        }

        public SimContextBuilder.SimContext newSim(final String name) {
            final var sim = service.create(zone, name);
            return builder.build(sim);
        }
    }
}
