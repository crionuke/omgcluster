package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sh.byv.zone.entity.ZoneModel;

@Getter
@AllArgsConstructor
public class InitialiationContext {

    final ZoneModel zone;

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        public InitialiationContext build(final ZoneModel zone) {
            return new InitialiationContext(zone);
        }
    }
}
