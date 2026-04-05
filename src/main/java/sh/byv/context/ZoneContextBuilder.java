package sh.byv.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.zone.ZoneEntity;

@ApplicationScoped
@AllArgsConstructor
public class ZoneContextBuilder {

    public ZoneContextBuilder.ZoneContext build(final ZoneEntity zone) {
        return new ZoneContext(zone);
    }

    public class ZoneContext {

        final ZoneEntity zone;

        public ZoneContext(final ZoneEntity zone) {
            this.zone = zone;
        }
    }
}
