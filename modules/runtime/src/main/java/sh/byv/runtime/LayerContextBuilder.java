package sh.byv.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.layer.LayerEntity;
import sh.byv.zone.ZoneService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LayerContextBuilder {

    final ZoneContextBuilder builder;
    final ZoneService service;

    public LayerContext build(final LayerEntity layer) {
        return new LayerContext(layer);
    }

    public class LayerContext {

        final LayerEntity layer;

        public LayerContext(final LayerEntity layer) {
            this.layer = layer;
        }

        public ZoneContextBuilder.ZoneContext newZone(final int x1,
                                                      final int y1,
                                                      final int x2,
                                                      final int y2) {
            final var zone = service.create(layer, x1, y1, x2, y2);
            return builder.build(zone);
        }
    }
}
