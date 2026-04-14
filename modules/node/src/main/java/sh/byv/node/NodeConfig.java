package sh.byv.node;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.node")
public interface NodeConfig {

    String name();
}
