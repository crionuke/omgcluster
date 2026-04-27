package sh.byv.runtime.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.sim.entity.SimService;
import sh.byv.zone.entity.ZoneEntity;

public class ZoneContext {

    final SimContext.Builder simBuilder;
    final SimService service;
    final ZoneEntity zone;

    ZoneContext(final SimContext.Builder simBuilder, final SimService service, final ZoneEntity zone) {
        this.simBuilder = simBuilder;
        this.service = service;
        this.zone = zone;
    }

    public SimContext newSim(final String name) {
        final var sim = service.create(zone, name);
        return simBuilder.build(sim);
    }

    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        final SimContext.Builder simBuilder;
        final SimService service;

        public ZoneContext build(final ZoneEntity zone) {
            return new ZoneContext(simBuilder, service, zone);
        }
    }
}
