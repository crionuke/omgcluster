package sh.byv.zone.state;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "omgcluster.zone-state")
public interface ZoneStatesConfig {

    String prefix();

    Duration ttl();
}
