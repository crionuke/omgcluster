package sh.byv.sim.result;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "omgcluster.sim-result")
public interface SimResultsConfig {

    String prefix();

    Duration ttl();
}
