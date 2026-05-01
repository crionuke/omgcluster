package sh.byv.zone.states;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "omgcluster.zone-states")
public interface ZoneStatesConfig {

    String prefix();

    Duration ttl();
}
