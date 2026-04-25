package sh.byv.state.service;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "omgcluster.state")
public interface StateConfig {

    String prefix();

    StateTtlConfig simState();

    StateTtlConfig zoneState();

    interface StateTtlConfig {
        Duration ttl();
    }
}
