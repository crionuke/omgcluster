package sh.byv.zone.executor;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.executor.zone")
public interface ZoneExecutorConfig {

    int delay();
}
