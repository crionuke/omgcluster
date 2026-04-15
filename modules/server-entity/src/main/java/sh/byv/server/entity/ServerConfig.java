package sh.byv.server.entity;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.server")
public interface ServerConfig {

    String name();
}
