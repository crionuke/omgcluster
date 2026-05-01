package sh.byv.runtime.context;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sh.byv.sim.result.SimResult;
import sh.byv.zone.state.ZoneState;

import java.util.List;

@Getter
@AllArgsConstructor
public class AggregationContext {

    final long tick;

    final List<SimResult> results;
    final ZoneState state;

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        public AggregationContext build(final long tick, final List<SimResult> results, final ZoneState state) {
            return new AggregationContext(tick, results, state);
        }
    }
}
