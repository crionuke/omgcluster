package sh.byv.runtime.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@AllArgsConstructor
public class AggregationContext {

    final long tick;

    final List<Object> results;
    final Object state;

    @Slf4j
    @ApplicationScoped
    @AllArgsConstructor
    public static class Builder {

        public AggregationContext build(final long tick, final List<Object> results, final Object state) {
            return new AggregationContext(tick, results, state);
        }
    }
}
