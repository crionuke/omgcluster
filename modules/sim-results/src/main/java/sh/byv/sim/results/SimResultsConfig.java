package sh.byv.sim.results;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "omgcluster.sim-results")
public interface SimResultsConfig {

    String prefix();

    Duration ttl();
}
