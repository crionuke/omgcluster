package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.layer.entity.LayerEntity;
import sh.byv.zone.entity.ZoneService;

public class LayerContext {

    final ZoneContext.Builder zoneBuilder;
    final ZoneService service;
    final LayerEntity layer;

    LayerContext(final ZoneContext.Builder zoneBuilder, final ZoneService service, final LayerEntity layer) {
        this.zoneBuilder = zoneBuilder;
        this.service = service;
        this.layer = layer;
    }

    public ZoneContext newZone(final int x1,
                               final int y1,
                               final int x2,
                               final int y2) {
        final var zone = service.createRoot(layer, x1, y1, x2, y2);
        return zoneBuilder.build(zone);
    }

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        final ZoneContext.Builder zoneBuilder;
        final ZoneService service;

        public LayerContext build(final LayerEntity layer) {
            return new LayerContext(zoneBuilder, service, layer);
        }
    }
}
