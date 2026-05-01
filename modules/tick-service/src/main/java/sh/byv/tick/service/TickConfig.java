package sh.byv.tick.service;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.tick")
public interface TickConfig {

    long interval();
}
