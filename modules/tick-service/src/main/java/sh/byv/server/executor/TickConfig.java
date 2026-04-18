package sh.byv.server.executor;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.tick")
public interface TickConfig {

    long interval();
}
